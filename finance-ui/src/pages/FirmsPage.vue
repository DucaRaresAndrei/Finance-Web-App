<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import UpgradeToFirm from '@/components/firms/UpgradeToFirm.vue'
import SearchFirmsByCategory from '@/components/firms/SearchFirmsByCategory.vue'
import RecentPayments from '@/components/firms/RecentPayments.vue'
import FirmsList from '@/components/firms/FirmsList.vue'
import { lowBalanceAlert } from '@/components/alert/lowBalanceAlert'
import LowBalance from '@/components/alert/LowBalance.vue'
import NoticeBanner from '@/components/ui/NoticeBanner.vue'

// import {
//   FirmControllerService,
//   ExpenseControllerService,
//   HistoryControllerService,
//   UserControllerService,
//   type FirmDTO,
//   type TransactionDTO,
// } from '@/services'

import {
  FirmControllerService,
  ExpenseControllerService,
  HistoryControllerService,
  UserControllerService,
  type FirmDTO,
  type TransactionDTO,
} from '@/services'

type Category = 'BILLS' | 'ENTERTAINMENT' | 'FOOD' | 'SHOPPING' | 'TRANSPORTATION' | 'USER';
type OnlyFirm = Exclude<Category, 'USER'>

// type Category = UpgradeFirmRequestDTO.category
// type OnlyFirm = Exclude<Category, UpgradeFirmRequestDTO.category.USER>

type TxType = 'INCOME' | 'EXPENSE'
type RecentItem = { id: string; title: string; subtitle: string; amount: number; type: TxType }

const loading = ref(false)
const errorMsg = ref<string | null>(null)

// const myAccountCategory = ref<Category>()
// const categories: OnlyFirm[] = ['BILLS', 'ENTERTAINMENT', 'FOOD', 'SHOPPING', 'TRANSPORTATION']

// const categories: OnlyFirm[] = [
//   UpgradeFirmRequestDTO.category.BILLS,
//   UpgradeFirmRequestDTO.category.ENTERTAINMENT,
//   UpgradeFirmRequestDTO.category.FOOD,
//   UpgradeFirmRequestDTO.category.SHOPPING,
//   UpgradeFirmRequestDTO.category.TRANSPORTATION,
// ]

const myAccountCategory = ref<Category>('USER')
const categories: OnlyFirm[] = ['BILLS', 'ENTERTAINMENT', 'FOOD', 'SHOPPING', 'TRANSPORTATION']


// const filterCategory = ref<'ALL' | OnlyFirm>('ALL')
type FirmFilter = 'ALL' | OnlyFirm
const filterCategory = ref<FirmFilter>('ALL')


const firms = ref<FirmDTO[]>([])
type Firm = { fullName: string; email: string; iban: string; category: OnlyFirm }

const firmsUi = computed<Firm[]>(() =>
  (firms.value ?? [])
    .filter(f => f.category !== 'USER')
    .map(f => ({
      fullName: f.fullName,
      email: f.email,
      iban: f.iban,
      category: f.category as OnlyFirm,
    }))
)


const firmsPage = ref(1)
const isFiltering = ref(false)

const recent = ref<RecentItem[]>([])
let pollTimer: number | null = null

const nbOpen = ref(false)
const nbType = ref<'error'|'success'>('success')
const nbMsg = ref('')
let nbTimer: number | null = null

function clearNbTimer(){ if (nbTimer){ window.clearTimeout(nbTimer); nbTimer = null } }

function showError(msg: string, ms = 7000){
  clearNbTimer(); nbType.value = 'error'; nbMsg.value = msg; nbOpen.value = true
  nbTimer = window.setTimeout(() => nbOpen.value = false, ms) as unknown as number
}
function showSuccess(msg: string, ms = 5000){
  clearNbTimer(); nbType.value = 'success'; nbMsg.value = msg; nbOpen.value = true
  nbTimer = window.setTimeout(() => nbOpen.value = false, ms) as unknown as number
}
onBeforeUnmount(clearNbTimer)

const { open: lbOpen, message: lbMsg, checkAfterTransaction, close: lbClose } = lowBalanceAlert()

function toRecentItemsFromDTO(list: TransactionDTO[]): RecentItem[] {
  return list
    .sort((a, b) => new Date(b.date as any).getTime() - new Date(a.date as any).getTime())
    .slice(0, 4)
    .map((t, idx) => ({
      id: `${t.type}-${t.ibanSender ?? ''}-${t.ibanReceiver ?? ''}-${idx}`,
      title: `To ${t.fromOrTo ?? '—'}`,
      subtitle: t.description ?? '',
      amount: Number(t.amount ?? 0),
      type: 'EXPENSE' as const,
    }))
}

async function loadAccountCategory() {
  try {
    const cat = (await UserControllerService.getCurrentUser()).account?.category
    myAccountCategory.value = (cat as Category) ?? 'USER'
  } catch {
    myAccountCategory.value = 'USER'
  }
}

async function loadFirms() {
  try {
    if (filterCategory.value === 'ALL') {
      firms.value = await FirmControllerService.listFirms({})
    } else {
      firms.value = await FirmControllerService.listFirms({ category: filterCategory.value as any })
    }
  } catch (e:any) {
    errorMsg.value = e?.message ?? 'Failed to load firms.'
    firms.value = []
  }
}

async function loadRecentPayments() {
  try {
    const items: TransactionDTO[] = await HistoryControllerService.getRecentFirmPayments()
    recent.value = toRecentItemsFromDTO(items)
  } catch {
    recent.value = []
  }
}

async function initialLoad() {
  loading.value = true
  errorMsg.value = null
  try {
    await Promise.all([loadAccountCategory(), loadFirms(), loadRecentPayments()])
  } catch (e: any) {
    errorMsg.value = e?.message ?? 'Failed to load data.'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await initialLoad()

  pollTimer = window.setInterval(() => {
    loadFirms()
    loadRecentPayments()
  }, 10000)
})
onBeforeUnmount(() => { if (pollTimer) window.clearInterval(pollTimer) })

async function fetchFirmsBy(cat: 'ALL' | OnlyFirm) {
  return cat === 'ALL'
    ? await FirmControllerService.listFirms({})
    : await FirmControllerService.listFirms({ category: cat as any })
}

watch(filterCategory, async (cat) => {
  isFiltering.value = true
  try {
    firms.value = await fetchFirmsBy(cat)
    firmsPage.value = 1
  } finally {
    isFiltering.value = false
  }
})

async function handleSendMoney(iban: string, amount: number, message?: string, pin?: string) {
  try {
    await ExpenseControllerService.createExpense({
      ibanReceiver: iban,
      amount,
      description: message ?? '',
      pin: pin ?? '',
    })
    await loadRecentPayments()
    await checkAfterTransaction()
    showSuccess('Payment sent successfully.')
  } catch (e:any) {
    // errorMsg.value = e?.message ?? 'Transfer failed.'
    const msg = 'Payment failed.'
    showError(msg)
  }
}

async function onUpgraded(cat: OnlyFirm) {
  try {
    await FirmControllerService.upgradeToFirm({ category: cat as any })
    myAccountCategory.value = cat
    await loadAccountCategory()
  } catch (e:any) {
    errorMsg.value = e?.message ?? 'Upgrade failed.'
  }
}
</script>


<template>
  <div class="firms-page">
    <header class="header">
      <h1>Firms & Businesses</h1>
      <p class="muted">Go pro as a firm or pay trusted businesses in seconds</p>
    </header>

    <section class="grid">
      <div class="left-col">
        <UpgradeToFirm
          class="card"
          :account-category="myAccountCategory"
          :categories="categories"
          @upgraded="onUpgraded"
        />

        <SearchFirmsByCategory
          class="card"
          :model-value="filterCategory"
          @update:model-value="(v) => (filterCategory = v as any)"
        />

        <RecentPayments
          class="card stretch"
          :items="recent"
        />
      </div>

      <FirmsList
        class="right-col card"
        :firms="firmsUi"
        v-model:page="firmsPage"
        @pay-to-firm="(p) => handleSendMoney(p.iban, p.amount, p.message, p.pin)"
        @validation-error="(msg) => showError(msg)"
      />
    </section>

    <div v-if="loading" class="muted" style="margin-top: 8px;">Loading…</div>

    <NoticeBanner v-model="nbOpen" :type="nbType" :message="nbMsg" :autoCloseMs="0" position="top-right" />

    <LowBalance v-model="lbOpen" :message="lbMsg" />
  </div>
</template>

<style scoped>
.firms-page { padding-top: 18px; }
.firms-page .header { margin-bottom: 18px; }
h1 { font-size: 1.6rem; font-weight: 800; margin: 0 0 6px; }
.muted { color: var(--muted); margin: 0; }

.grid {
  display: grid;
  grid-template-columns: 0.95fr 1.05fr;
  gap: 18px;
  align-items: stretch;
}

.left-col {
  min-height: 0; height: 100%;
  display: flex; flex-direction: column; gap: 14px;
  min-width: 0; overflow: auto; padding-right: 2px;
}
.left-col .stretch {
  flex: 1 1 auto;
  min-height: 0;
  overflow: auto;
}
.right-col {
  min-height: 0; height: 100%;
  display: flex; flex-direction: column;
  overflow: auto;
}

.card h2 {
  font-size: 1.05rem; font-weight: 800;
  margin: 0 0 10px;
}

.card :where(input, button){ font-size: 1em; }
</style>
