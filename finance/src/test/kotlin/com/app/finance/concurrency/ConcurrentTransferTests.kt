package com.app.finance.concurrency

import com.app.finance.enums.Category
import com.app.finance.models.Account
import com.app.finance.models.User
import com.app.finance.models.requests.ExpenseRequestDTO
import com.app.finance.repositories.AccountRepository
import com.app.finance.repositories.UserRepository
import com.app.finance.services.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class ConcurrentTransferTests {

    companion object {
        // PostgreSQL real in containerul de teste
        @JvmField
        @Container
        val postgres = PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")

        // Injectam proprietatile de conexiune catre container
        @JvmStatic
        @DynamicPropertySource
        fun props(reg: DynamicPropertyRegistry) {
            reg.add("spring.datasource.url", postgres::getJdbcUrl)
            reg.add("spring.datasource.username", postgres::getUsername)
            reg.add("spring.datasource.password", postgres::getPassword)
            reg.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
            reg.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
            reg.add("spring.sql.init.mode") { "never" }
        }
    }

    @Autowired lateinit var expenseService: ExpenseService
    @Autowired lateinit var accountRepository: AccountRepository

    // Mock-uri pentru dependintele irelevante in unit teste
    @MockBean lateinit var currentUser: CurrentUserService
    @MockBean lateinit var friendService: FriendService
    @MockBean lateinit var cardService: CardService
    @MockBean lateinit var emailService: EmailNotificationService
    @MockBean lateinit var userRepository: UserRepository

    // User minimal pentru currentUser si userReceiver
    private fun mkUser(keycloakId: String, email: String, fullName: String, accountId: Long): User =
        User(
            id = null,
            keycloakId = keycloakId,
            email = email,
            fullName = fullName,
            accountId = accountId
        )

    @Test
    fun `two concurrent transfers - only one succeeds when balance is not enough`() {
        // Sender are 100 RON
        // Ruleaza 2 request-uri simultan, fiecare incearca sa trimita 60 RON
        // Astfel, doar unul poate reusi, celalalt trebuie sa dea eroare
        val sender = accountRepository.save(
            Account(
                iban = generateIban(),
                balance = BigDecimal("100.00"),
                category = Category.USER
            )
        )
        val receiver = accountRepository.save(
            Account(
                iban = generateIban(),
                balance = BigDecimal("0.00"),
                category = Category.USER
            )
        )

        // Mock pentru currentUser
        whenever(currentUser.getLoggedAccount()).thenAnswer {
            accountRepository.findById(sender.id!!).get()
        }
        whenever(currentUser.getLoggedUser()).thenReturn(
            mkUser("k1", "me@test.com", "Me", sender.id!!)
        )

        // Mock friendService
        whenever(friendService.addFriendAfterTransaction(any())).thenAnswer {
            accountRepository.findById(sender.id!!).get()
        }

        // Mock cardService
        whenever(cardService.verifyPaymentToFirm(any(), any(), anyOrNull()))
            .thenReturn(true)

        // Mock pentru owner-ul receiver-ului
        whenever(userRepository.findByAccountId(receiver.id)).thenReturn(
            mkUser("k2", "recv@test.com", "Receiver", receiver.id!!)
        )

        val req = ExpenseRequestDTO(
            description = "t",
            amount = BigDecimal("60.00"),
            ibanReceiver = receiver.iban,
            pin = null
        )

        // Pornim ambele thread-uri in acelasi timp, pentru a forta concurenta reala
        val start = CountDownLatch(1)
        val pool = Executors.newFixedThreadPool(2)

        val f1 = pool.submit(Callable {
            start.await()
            runCatching { expenseService.createExpense(req) }
        })
        val f2 = pool.submit(Callable {
            start.await()
            runCatching { expenseService.createExpense(req) }
        })

        start.countDown()

        val r1 = f1.get()
        val r2 = f2.get()

        pool.shutdown()
        pool.awaitTermination(2, TimeUnit.SECONDS)

        // Validam ca doar un request a reusit si celalalt a esuat
        val successes = listOf(r1, r2).count { it.isSuccess }
        val failures = listOf(r1, r2).count { it.isFailure }
        assertEquals(1, successes)
        assertEquals(1, failures)

        // Validam soldurile finale
        val senderAfter = accountRepository.findById(sender.id!!).get()
        val receiverAfter = accountRepository.findById(receiver.id!!).get()

        assertEquals(BigDecimal("40.00"), senderAfter.balance)
        assertEquals(BigDecimal("60.00"), receiverAfter.balance)

        // Emailul de success trebuie trimis o singura data (doar o tranzactie a facut commit)
        verify(emailService, timeout(1500).times(1))
            .sendTransferSuccessEmail(
                any<String>(),
                any<String>(),
                any<BigDecimal>(),
                any<String>(),
                any<String>(),
                any<BigDecimal>()
            )
    }

    @Test
    fun `two concurrent transfers - both succeed when balance is enough`() {
        // Sender are 200 RON
        // Ruleaza 2 request-uri simultan: 60 si 70 RON
        // Ambele trebuie sa reuseasca
        val sender = accountRepository.save(
            Account(
                iban = generateIban(),
                balance = BigDecimal("200.00"),
                category = Category.USER
            )
        )
        val receiver = accountRepository.save(
            Account(
                iban = generateIban(),
                balance = BigDecimal("0.00"),
                category = Category.USER
            )
        )

        whenever(currentUser.getLoggedAccount()).thenAnswer {
            accountRepository.findById(sender.id!!).get()
        }
        whenever(currentUser.getLoggedUser()).thenReturn(
            mkUser("k1", "me@test.com", "Me", sender.id!!)
        )
        whenever(friendService.addFriendAfterTransaction(any())).thenAnswer {
            accountRepository.findById(sender.id!!).get()
        }
        whenever(cardService.verifyPaymentToFirm(any(), any(), anyOrNull()))
            .thenReturn(true)
        whenever(userRepository.findByAccountId(receiver.id)).thenReturn(
            mkUser("k2", "recv@test.com", "Receiver", receiver.id!!)
        )

        val req1 = ExpenseRequestDTO("t1", BigDecimal("60.00"), receiver.iban, null)
        val req2 = ExpenseRequestDTO("t2", BigDecimal("70.00"), receiver.iban, null)

        val start = CountDownLatch(1)
        val pool = Executors.newFixedThreadPool(2)

        val f1 = pool.submit(Callable {
            start.await()
            runCatching { expenseService.createExpense(req1) }
        })
        val f2 = pool.submit(Callable {
            start.await()
            runCatching { expenseService.createExpense(req2) }
        })

        start.countDown()

        val r1 = f1.get()
        val r2 = f2.get()

        pool.shutdown()
        pool.awaitTermination(2, TimeUnit.SECONDS)

        // Ambele trebuie sa fie success
        assertTrue(r1.isSuccess, "First transfer should succeed")
        assertTrue(r2.isSuccess, "Second transfer should succeed")

        val senderAfter = accountRepository.findById(sender.id!!).get()
        val receiverAfter = accountRepository.findById(receiver.id!!).get()

        assertEquals(BigDecimal("70.00"), senderAfter.balance)
        assertEquals(BigDecimal("130.00"), receiverAfter.balance)

        // 2 tranzactii au facut commit, deci ar trebui sa avem 2 mailuri
        verify(emailService, timeout(1500).times(2)).sendTransferSuccessEmail(
            any<String>(),
            any<String>(),
            any<BigDecimal>(),
            any<String>(),
            any<String>(),
            any<BigDecimal>()
        )

    }

    @Test
    fun `fullTest - N concurrent transfers never make balance negative and receiver sum matches successes`() {
        val senderStart = BigDecimal("500.00")
        val amount = BigDecimal("30.00")
        val n = 25 // 25 * 30 = 750 > 500, deci nu pot reusi toate

        val sender = accountRepository.save(
            Account(
                iban = generateIban(),
                balance = senderStart,
                category = Category.USER
            )
        )
        val receiver = accountRepository.save(
            Account(
                iban = generateIban(),
                balance = BigDecimal("0.00"),
                category = Category.USER
            )
        )

        whenever(currentUser.getLoggedAccount()).thenAnswer {
            accountRepository.findById(sender.id!!).get()
        }
        whenever(currentUser.getLoggedUser()).thenReturn(
            mkUser("k1", "me@test.com", "Me", sender.id!!)
        )
        whenever(friendService.addFriendAfterTransaction(any())).thenAnswer {
            accountRepository.findById(sender.id!!).get()
        }
        whenever(cardService.verifyPaymentToFirm(any(), any(), anyOrNull()))
            .thenReturn(true)
        whenever(userRepository.findByAccountId(receiver.id)).thenReturn(
            mkUser("k2", "recv@test.com", "Receiver", receiver.id!!)
        )

        val start = CountDownLatch(1)
        val pool = Executors.newFixedThreadPool(8)

        // Cream n task-uri simultane
        val futures = (1..n).map { idx ->
            val req = ExpenseRequestDTO("fullTest-$idx", amount, receiver.iban, null)
            pool.submit(Callable {
                start.await()
                runCatching { expenseService.createExpense(req) }
            })
        }

        start.countDown()

        val results = futures.map { it.get() }
        pool.shutdown()
        pool.awaitTermination(3, TimeUnit.SECONDS)

        // Doar verificam ca am obtinut n rezultate
        val successCount = results.count { it.isSuccess }
        val failCount = results.count { it.isFailure }
        assertEquals(n, successCount + failCount)

        val senderAfter = accountRepository.findById(sender.id!!).get()
        val receiverAfter = accountRepository.findById(receiver.id!!).get()

        // Balanta sender-ului nu trebuie sa fie negativa
        assertTrue(senderAfter.balance >= BigDecimal.ZERO)

        // receiver primeste fix successCount * amount
        assertEquals(amount.multiply(BigDecimal(successCount)), receiverAfter.balance)

        // sender balance scade exact cu successCount * amount
        assertEquals(senderStart.subtract(amount.multiply(BigDecimal(successCount))), senderAfter.balance)
    }

    fun generateIban(): String {
        var iban: String
        do {
            iban = "RO" + (1000000000000000..9999999999999999).random().toString()
        } while (accountRepository.existsByIban(iban))
        return iban
    }
}
