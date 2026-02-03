<template>
  <li class="tx-item">
    <div class="left">
      <div class="icon" :class="type">
        <v-icon
          :icon="type === 'INCOME' ? 'mdi-arrow-down-left' : 'mdi-arrow-up-right'"
          size="20"
        />
      </div>
      <div class="stack">
        <div class="title">{{ fromOrTo }}</div>
        <div class="sub">{{ description }}</div>
        <div class="date">{{ date }}</div>
      </div>
    </div>
    <div class="amount" :class="type">
      {{ type === 'INCOME' ? '+' : '-' }}{{ formatCurrency(amount) }}
    </div>
  </li>
</template>

<script setup lang="ts">
type TxType = 'INCOME' | 'EXPENSE'

interface Props {
  id?: string
  fromOrTo: string
  description: string
  date: string
  amount: number
  type: TxType
  currency?: string
}

const props = withDefaults(defineProps<Props>(), {
  currency: 'RON'
})

function formatCurrency(n: number) {
  return `${Math.abs(n).toLocaleString('ro-RO', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })} ${props.currency}`
}
</script>

<style scoped>
.tx-item {
  padding: 10px 14px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.left { display: flex; align-items: center; gap: 12px; }
.icon {
  width: 36px; height: 36px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
}
.icon.INCOME { background: rgba(34,197,94,0.15); color: #22c55e; }
.icon.EXPENSE { background: rgba(239,68,68,0.15); color: #ef4444; }
.stack { display: grid; gap: 2px; }
.title { font-weight: 600; }
.sub { font-size: 0.85rem; color: #97a3b6; }
.date { font-size: 0.75rem; color: #6b7280; }
.amount { font-weight: 800; }
.amount.INCOME { color: #22c55e; }
.amount.EXPENSE { color: #ef4444; }
</style>
