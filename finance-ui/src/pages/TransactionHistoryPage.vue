<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import SummaryCards from '../components/transaction-history/SummaryCards.vue'
import TransactionsList from '../components/transaction-history/TransactionsList.vue'
import SearchTransactions from '../components/transaction-history/SearchTransactions.vue'
import {
  HistoryControllerService,
  StatsControllerService,
  type HistorySummaryDTO,
  type DashboardStatsDTO
} from "@/services";

const transactions = ref<HistorySummaryDTO['lastTransactions']>([]);
const stats = ref<DashboardStatsDTO | null>(null);

const loading = ref(false);
const error = ref<string | null>(null);

const summaryData = computed(() => {
  if (!stats.value) {
    return { income: 0, expenses: 0, net: 0 };
  }
  const monthSummary = stats.value.monthSummary;
  return {
    income: monthSummary.incomes ?? 0,
    expenses: monthSummary.expense ?? 0,
    net: monthSummary.netSavings ?? 0,
  };
});

const fetchData = async () => {
  loading.value = true;
  error.value = null;
  try {
    const allTransactionsData = await HistoryControllerService.getHistory();
    const dashboardStats = await StatsControllerService.getDashboardStats();

    transactions.value = allTransactionsData.lastTransactions;
    stats.value = dashboardStats;

  } catch (e: any) {
    console.error("Failed to fetch data:", e);
    error.value = e?.message ?? "Failed to load data!";
  } finally {
    loading.value = false;
  }
};

onMounted(fetchData);

const searchQuery = ref('');

const filteredTransactions = computed(() => {
  if (!searchQuery.value) {
    return transactions.value;
  }
  const query = searchQuery.value.toLowerCase();
  return transactions.value.filter(transaction =>
    transaction.fromOrTo.toLowerCase().includes(query) ||
    transaction.description?.toLowerCase().includes(query)
  );
});
</script>

<template>
  <div>
    <h1>Transaction History</h1>
    <p class="subtitle">View and manage all your financial transactions</p>
    <SummaryCards :data="summaryData" />
    <SearchTransactions @update:query="searchQuery = $event" class="search-transactions-list-container"/>
    <TransactionsList :transactions="filteredTransactions" class="transactions-list-container" />
  </div>
</template>

<style scoped>
.subtitle {
  color: #999;
  font-size: 20px;
}

.transactions-list-container {
  margin-top: 24px;
}

.search-transactions-list-container {
  margin-top: 24px;
}
</style>
