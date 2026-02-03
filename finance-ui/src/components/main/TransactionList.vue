<template>
  <v-card class="card-elevated list" variant="elevated">
    <div class="head">
      <div class="title">Recent Transactions ({{ limitedItems.length }})</div>
      <v-btn
        variant="plain"
        class="view-all-btn"
        @click="goToTransactionHistory"
      >
        View All
      </v-btn>
    </div>
    <ul class="body">
      <TransactionItem
        v-for="t in limitedItems"
        :key="t.id"
        v-bind="t"
      />
    </ul>
  </v-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import TransactionItem from '@/components/main/TransactionItem.vue'
import { useRouter } from 'vue-router'

type TxType = 'INCOME' | 'EXPENSE'
type Item = {
  id: string
  fromOrTo: string
  description: string
  date: string
  amount: number
  type: TxType
  currency?: string
}

const props = defineProps<{ items: Item[]; count?: number }>()
const limitedItems = computed(() =>
  props.count ? props.items.slice(0, props.count) : props.items
)

const router = useRouter()
const goToTransactionHistory = () => {
  router.push('/transaction-history')
}
</script>

<style scoped>
.list { padding: 10px; }
.head {
  padding: 14px 18px;
  border-bottom: 1px solid #262b33;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title { font-weight: 800; }
.body {
  list-style: none;
  margin: 0;
  padding: 12px;
  display: grid;
  gap: 10px;
}
.view-all-btn {
  background-color: transparent;
  text-transform: none;
  font-weight: 600;
  letter-spacing: .2px;
  border-radius: 16px;
  padding: 8px 12px;
  transition: background-color 0.2s ease;
}
.view-all-btn:hover {
  background-color: rgba(121, 134, 203, 0.1);
}
</style>
