<template>
  <div class="friends-page">
    <header class="header">
      <h1>Friends & Contacts</h1>
      <p class="muted">Manage your contacts and send money instantly</p>
    </header>

    <section class="grid">
      <div class="left-col">
        <QuickActions
          :state="quickState"
          @add-mode="quickState = 'add'"
          @send-mode="quickState = 'send'"
          @back="quickState = 'idle'"
          @add-friend="handleAddFriend"
          @send-money="handleSendMoney"
          @validation-error="(msg) => showError(msg)"
        />

        <SearchFriends class="card" v-model="query" />

        <RecentActivity class="card stretch" :items="recent" />
      </div>

      <FriendsList class="right-col card" :friends="friends" @send-to-friend="(p) => handleSendMoney(p.iban, p.amount, p.message)"
                   @validation-error="(msg) => showError(msg)"/>
    </section>

    <div v-if="loading" class="muted" style="margin-top: 8px;">Loading…</div>
    <!--    <div v-if="errorMsg" style="margin-top: 8px; color: #ff5964;">{{ errorMsg }}</div>-->

    <NoticeBanner v-model="nbOpen" :type="nbType" :message="nbMsg" :autoCloseMs="0" position="top-right" />

    <LowBalance v-model="lbOpen" :message="lbMsg" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onBeforeUnmount, ref, watch } from 'vue'
import QuickActions from '@/components/friends/QuickActions.vue'
import SearchFriends from '@/components/friends/SearchFriends.vue'
import RecentActivity from '@/components/friends/RecentActivity.vue'
import FriendsList from '@/components/friends/FriendsList.vue'
import { lowBalanceAlert } from '@/components/alert/lowBalanceAlert'
import LowBalance from '@/components/alert/LowBalance.vue'
import NoticeBanner from '@/components/ui/NoticeBanner.vue'

import {
  FriendControllerService,
  ExpenseControllerService,
  HistoryControllerService,
  type FriendResponseDTO,
  TransactionDTO,
} from '@/services'

type TxType = 'INCOME' | 'EXPENSE'
type RecentItem = { id: string; title: string; subtitle: string; amount: number; type: TxType; iban: string; }
const quickState = ref<'idle' | 'add' | 'send'>('idle')

const query = ref('')
const searchActive = computed(() => query.value.trim().length > 0)
const friends = ref<FriendResponseDTO[]>([])
const recent = ref<RecentItem[]>([])

const loading = ref(false)
const errorMsg = ref<string | null>(null)
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

async function loadFriends(text?: string) {
  try {
    const t = (text ?? '').trim()
    if (t) {
      friends.value = await FriendControllerService.searchFriends({ nameOrMail: t })
    } else {
      friends.value = await FriendControllerService.getFriends()
    }
  } catch (e:any) {
    if (!text) errorMsg.value = e?.message ?? 'Failed to load friends'
    friends.value = []
  }
}

function toRecentItemsFromDTO(list: TransactionDTO[]): RecentItem[] {
  return list
    .sort((a, b) => new Date(b.date as any).getTime() - new Date(a.date as any).getTime())
    .slice(0, 4)
    .map((t, idx) => {
      const title = (t.type === 'INCOME' ? 'From ' : 'To ') + (t.fromOrTo ?? '—')
      return {
        id: `${t.type}-${t.ibanSender ?? ''}-${t.ibanReceiver ?? ''}-${idx}`,
        title,
        subtitle: t.description ?? '',
        amount: Number(t.amount ?? 0),
        type: (t.type === 'INCOME' ? 'INCOME' : 'EXPENSE') as TxType,
        iban: (t.type === 'INCOME' ? t.ibanSender : t.ibanReceiver ) as string,
      }
    })
}

async function loadRecentActivity() {
  try {
    const items: TransactionDTO[] = await HistoryControllerService.getRecentUserTransactions()
    recent.value = toRecentItemsFromDTO(items)
  } catch (e:any) {
    recent.value = []
  }
}

async function initialLoad() {
  try {
    loading.value = true
    await Promise.all([loadFriends(), loadRecentActivity()])
  } catch (e: any) {
    errorMsg.value = e?.message ?? 'Failed to load data.'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await initialLoad()
  // refresh 10 sec
  pollTimer = window.setInterval(() => {
    loadFriends(query.value)
    loadRecentActivity()
  }, 10000)
})
onBeforeUnmount(() => { if (pollTimer) window.clearInterval(pollTimer) })

let searchTimer: number | null = null
watch(query, (q) => {
  if (searchTimer) window.clearTimeout(searchTimer)
  searchTimer = window.setTimeout(() => loadFriends(q), 200) as unknown as number
})

async function handleAddFriend(iban: string) {
  try {
    await FriendControllerService.addFriend({ iban })
    await loadFriends(query.value)
    showSuccess('Friend added successfully.')
  } catch (e:any) {
    showError('Could not add friend. The IBAN might not exist or the friend is already added.')
  }
}

async function handleSendMoney(iban: string, amount: number, message?: string) {
  try {
    await ExpenseControllerService.createExpense({
      ibanReceiver: iban,
      amount,
      description: message ?? '',
    })
    await Promise.all([loadRecentActivity(), loadFriends(query.value)])
    await checkAfterTransaction()
    showSuccess('Transfer sent successfully.')
  } catch (e:any) {
    errorMsg.value = e?.message ?? 'Transfer failed.'
    showError('Transfer failed.')
  }
}
</script>


<style scoped>
.friends-page { padding-top: 18px; }
.friends-page .header { margin-bottom: 18px; }
h1 { font-size: 1.6rem; font-weight: 800; margin: 0 0 6px; }
.muted { color: var(--muted); margin: 0; }

.grid {
  display: grid;
  grid-template-columns: 0.95fr 1.05fr;
  gap: 18px;
  align-items: stretch;
}

.left-col {
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
