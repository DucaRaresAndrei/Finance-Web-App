<script setup lang="ts">
import { ref, computed } from 'vue';

interface Transaction {
  amount: number;
  type: 'sent' | 'received' | 'INCOME' | 'EXPENSE';
  description?: string;
  date: string;
  fromOrTo: string;
}

const props = defineProps<{ transactions: Transaction[] }>();

const itemsPerPage = 7;
const currentPage = ref(0);

const paginatedTransactions = computed(() => {
  const start = currentPage.value * itemsPerPage;
  const end = start + itemsPerPage;
  return props.transactions.slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(props.transactions.length / itemsPerPage);
});

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++;
  }
};

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
  }
};

function formattedAmount(transaction: Transaction) {
  const sign = transaction.amount >= 0 ? '+' : '-';
  const value = Math.abs(transaction.amount).toFixed(2);
  return `${sign}${value} RON`;
}

function formattedDate(dateString: string) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
}
</script>

<template>
  <div class="transactions-container">
    <div class="header">
      <div class="title">All Transactions ({{ props.transactions.length }})</div>
      <div class="pagination-controls">
        <button
          @click="prevPage"
          :disabled="currentPage === 0"
          class="pagination-btn"
          :class="{ 'disabled': currentPage === 0 }"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="w-6 h-6"
          >
            <path
              fill-rule="evenodd"
              d="M7.72 12.53a.75.75 0 0 1 0-1.06l7.5-7.5a.75.75 0 1 1 1.06 1.06L9.31 12l6.97 6.97a.75.75 0 1 1-1.06 1.06l-7.5-7.5Z"
              clip-rule="evenodd"
            />
          </svg>
        </button>
        <button
          @click="nextPage"
          :disabled="currentPage === totalPages - 1"
          class="pagination-btn"
          :class="{ 'disabled': currentPage === totalPages - 1 }"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="w-6 h-6"
          >
            <path
              fill-rule="evenodd"
              d="M16.28 12.53a.75.75 0 0 0 0-1.06l-7.5-7.5a.75.75 0 0 0-1.06 1.06L14.69 12l-6.97 6.97a.75.75 0 1 0 1.06 1.06l7.5-7.5Z"
              clip-rule="evenodd"
            />
          </svg>
        </button>
      </div>
    </div>
    <div class="transactions-list">
      <div
        v-for="(transaction, index) in paginatedTransactions"
        :key="index"
        class="transaction-item"
        :class="{
          'sent': transaction.type === 'sent' || transaction.type === 'EXPENSE',
          'received': transaction.type === 'received' || transaction.type === 'INCOME'
        }"
      >
        <div class="icon-container">
          <v-icon
            v-if="transaction.type === 'received' || transaction.type === 'INCOME'"
            icon="mdi-arrow-down-left"
            class="received-icon"
            size="20"
          />
          <v-icon
            v-if="transaction.type === 'sent' || transaction.type === 'EXPENSE'"
            icon="mdi-arrow-up-right"
            class="sent-icon"
            size="20"
          />
        </div>
        <div class="details">
          <div class="source-name">{{ transaction.fromOrTo }}</div>
          <div class="description">{{ transaction.description }}</div>
          <div class="date">{{ formattedDate(transaction.date) }}</div>
        </div>
        <div
          class="amount"
          :class="{ 'received': transaction.type === 'received' || transaction.type === 'INCOME', 'sent': transaction.type === 'sent' || transaction.type === 'EXPENSE' }"
        >
          {{ formattedAmount(transaction) }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.transactions-container {
  background-color: var(--input-bg);
  border-radius: 12px;
  padding: 20px;
  color: #fff;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.header {
  font-weight: 500;
  color: #fff;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-weight: 800;
}

.transactions-list {
  display: flex;
  flex-direction: column;
}

.transaction-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #3a3a3c;
}

.transaction-item:last-child {
  border-bottom: none;
}

.icon-container {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.received .icon-container {
  background-color: #34493e;
}

.sent .icon-container {
  background-color: #493335;
}

.received-icon,
.sent-icon {
  width: 20px;
  height: 20px;
}

.received-icon {
  color: #4cd964;
}

.sent-icon {
  color: #ff3b30;
}

.details {
  flex-grow: 1;
}

.source-name {
  font-weight: 600;
  font-size: 16px;
  line-height: 1.2;
}

.description {
  color: #b0b0b0;
  font-size: 14px;
}

.date {
  color: #6a6a6a;
  font-size: 12px;
}

.amount {
  font-weight: 600;
  font-size: 16px;
}

.amount.received {
  color: #4cd964;
}

.amount.sent {
  color: #ff3b30;
}

.pagination-controls {
  display: flex;
  gap: 8px;
}

.pagination-btn {
  background-color: #2a2a2a;
  color: #fff;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
}

.pagination-btn:hover {
  background-color: #4a4a4a;
}

.pagination-btn.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
