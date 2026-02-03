<script setup lang="ts">
import StatCard from './Card.vue'
const props = defineProps<{ period: 'month'|'week',
  data: { income: number, expenses: number, net: number }}>()
</script>


<template>
  <div class="grid">
    <StatCard
      title="Total Income"
      :amount="data.income"
      icon="mdi-trending-up"
      tone="success"
      :period="period"
      :showBadge="true"
      :isPositive="true"
      emoji="💰"
    />
    <StatCard
      title="Total Expenses"
      :amount="data.expenses"
      icon="mdi-trending-down"
      tone="error"
      :period="period"
      :showBadge="true"
      :isPositive="false"
      emoji="💸"
    />
    <StatCard
      title="Net Savings"
      :amount="data.net"
      icon="mdi-piggy-bank"
      tone="info"
      :period="period"
      :showBadge="true"
      :isPositive="data.net >= 0"
      emoji="🎯"
    />
  </div>
</template>


<style scoped>
.grid{
  display:grid;
  grid-template-columns: repeat(3, minmax(0,1fr));
  gap:16px;
}
</style>
