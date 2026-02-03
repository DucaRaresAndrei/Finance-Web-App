<template>
  <v-container>
    <v-row no-gutters class="mb-3">
      <v-col cols="12" md="4" class="pr-2">
        <AccountBalanceCard
          :balance="balance"
          :owner="owner"
          :card-number-masked="cardNumberMasked"
          :card-number="cardNumber"
          :expiration-date="expirationDate"
          :cvv-number="cvvNumber"
          currency-code="RON"
          locale="ro-RO"
        />
      </v-col>

      <v-col cols="12" md="8" class="pl-2">
        <div class="mb-4 text-h5 font-weight-bold">
          Current Month
        </div>
        <SummaryCards :period='period' :data="summaryData" />

      </v-col>
      <div class="mt-5">
        <v-btn
          class="add-money-btn"
          size="large"
          prepend-icon="mdi-plus"
          @click="showAddDialog = true"
        >
          Add money
        </v-btn>
      </div>
    </v-row>

    <v-row class="list-row" no-gutters>
      <v-col cols="12">
        <TransactionList :items="transactions" :count="5">
        </TransactionList>
      </v-col>
    </v-row>

    <v-dialog v-model="showAddDialog" max-width="520" >
      <v-card class="dialog-glass">
        <div class="dialog-header">
          <div class="dialog-badge">
            <v-icon size="26">mdi-cash-plus</v-icon>
          </div>
          <div class="dialog-title">Add money</div>
          <v-btn icon variant="text" class="close" @click="showAddDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </div>

        <v-divider class="divider" />

        <v-card-text class="dialog-body">
          <v-text-field
            v-model="addAmount"
            label="Amount"
            suffix="RON"
            variant="outlined"
            density="comfortable"
            hide-details="auto"
          />
          <v-text-field
            v-model="description"
            label="Description"
            variant="outlined"
            density="comfortable"
            hide-details="auto"
          />
        </v-card-text>

        <v-card-actions class="dialog-actions">
          <v-btn variant="text" class="btn-cancel" @click="showAddDialog = false">
            Cancel
          </v-btn>
          <v-spacer />
          <v-btn class="btn-primary" @click="handleAddMoney">
            Add
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>


  </v-container>
</template>

<!--<script lang="ts" src="./MainPage.ts"></script>-->
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'

import AccountBalanceCard from '@/components/main/AccountBalanceCard.vue'
import SummaryStat from '@/components/main/SummaryStat.vue'
import TransactionList from '@/components/main/TransactionList.vue'
import SummaryCards from '@/components/statistics/summary-cards/SummaryCards.vue'
import StatsHeader from '@/components/statistics/header/StatsHeader.vue'

import {
  CardControllerService,
  HistoryControllerService,
  StatsControllerService,
  UserControllerService,
  type DashboardStatsDTO,
  type HistorySummaryDTO,
  type TopupRequestDTO,
  type UserResponseDTO,
} from '@/services'

const user = ref<UserResponseDTO | null>(null)
const history = ref<HistorySummaryDTO | null>(null)

const loading = ref(false)
const error = ref<string | null>(null)

const stats = ref<DashboardStatsDTO | null>(null)
const selectedPeriod = ref<'week' | 'month'>('month')

const refreshData = async () => {
  try {
    loading.value = true
    const [u, h, s] = await Promise.all([
      UserControllerService.getCurrentUser(),
      HistoryControllerService.getHistory(),
      StatsControllerService.getDashboardStats(), // daca cere offset, pune 0
    ])
    user.value = u
    history.value = h
    stats.value = s
  } catch (e: any) {
    error.value = e?.message ?? 'Failed to load data.'
  } finally {
    loading.value = false
  }
}

onMounted(refreshData)

const period = computed(() => selectedPeriod.value)
const summary = computed(() => {
  if (!stats.value) return { incomes: 0, expense: 0, netSavings: 0 }
  return period.value === 'month' ? stats.value.monthSummary : stats.value.weekSummary
})

const summaryData = computed(() => ({
  income: summary.value?.incomes ?? 0,
  expenses: summary.value?.expense ?? 0,
  net: summary.value?.netSavings ?? 0,
}))

//const transactions = computed(() => history.value?.lastTransactions ?? [])
const transactions = computed(() => {
  const list = history.value?.lastTransactions ?? []

  return list.map((t, idx) => ({
    id: `${t.type}-${t.date}-${t.ibanSender ?? ''}-${t.ibanReceiver ?? ''}-${idx}`,

    // trebuie sa ramana INCOME/EXPENSE ca in TransactionList.vue
    type: t.type as 'INCOME' | 'EXPENSE',

    amount: Number(t.amount ?? 0),
    description: t.description ?? '',
    date: t.date,
    fromOrTo: t.fromOrTo,
    // optional daca vrei, altfel poti sterge:
    currency: 'RON',
  }))
})

const balance = computed(() => user.value?.account?.balance ?? 0)
const owner = computed(() => user.value?.fullName ?? '')
const cardNumberMasked = computed(() => user.value?.account?.card?.cardNumberMasked ?? '')
const cardNumber = computed(() => user.value?.account?.card?.cardNumber ?? '')
//const expirationDate = computed(() => user.value?.account?.card?.expirationDate ?? '')
const expirationDate = computed(() => {
  const exp: any = user.value?.account?.card?.expirationDate
  if (!exp) return ''

  if (typeof exp === 'string') return exp

  const year = exp.year ?? 0
  const month = exp.monthValue ?? exp.month ?? 0
  if (!year || !month) return ''

  const mm = String(month).padStart(2, '0')
  const yy = String(year).slice(-2)
  return `${mm}/${yy}`
})

const cvvNumber = computed(() => user.value?.account?.card?.cvvNumber ?? '')

const showAddDialog = ref(false)
const addAmount = ref<number | null>(null)
const description = ref<string>('')

const handleAddMoney = async () => {
  if (!addAmount.value || addAmount.value <= 0) {
    error.value = 'Please enter a valid amount.'
    return
  }

  try {
    await CardControllerService.topup({
      amount: addAmount.value,
      description: description.value || 'Top up',
    } as TopupRequestDTO)

    await refreshData()

    showAddDialog.value = false
    addAmount.value = null
    description.value = ''
  } catch (e: any) {
    error.value = e?.message ?? 'Topup failed.'
  }
}
</script>

<style src="./MainPage.css"></style>
