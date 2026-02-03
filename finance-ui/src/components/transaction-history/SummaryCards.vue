<script setup lang="ts">
import StatCard from './Card.vue'
const props = defineProps<{ data: { income: number, expenses: number, net: number }}>()
</script>

<template>
  <div class="grid">
    <StatCard
      title="Total Received"
      :amount="data.income"
      icon="mdi-arrow-up"
      tone="success"
    />
    <StatCard
      title="Total Sent"
      :amount="-data.expenses"
      icon="mdi-arrow-down"
      tone="error"
    />
    <StatCard
      title="Net Flow"
      :amount="data.net"
      :icon="data.net >= 0 ? 'mdi-trending-up' : 'mdi-trending-down'"
      :tone="data.net >= 0 ? 'success' : 'error'"
    />
  </div>
</template>

<style scoped>
.grid{
  margin-top: 24px;
  display:grid;
  grid-template-columns: repeat(3, minmax(0,1fr));
  gap:16px;
}
</style>
