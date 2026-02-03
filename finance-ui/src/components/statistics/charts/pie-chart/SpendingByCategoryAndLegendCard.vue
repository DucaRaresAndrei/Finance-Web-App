<script setup lang="ts">
import SpendingByCategoryPie from './SpendingByCategory.vue'

const props = defineProps<{ rows: { category: string; amount: number }[] }>()
</script>

<template>
  <v-card class="stat-card">
    <v-card-title class="d-flex align-center ga-2">
      <div class="pill">
        <v-icon size="22">mdi-finance</v-icon>
      </div>
      Spending by Category
    </v-card-title>

    <v-card-subtitle class="mt-2">
      🎯 Breakdown of expenses by category
    </v-card-subtitle>

    <v-card-text>
      <SpendingByCategoryPie :rows="props.rows" />
    </v-card-text>
  </v-card>
</template>

<style scoped>
.stat-card{
  background: linear-gradient(135deg, rgba(45,49,75,.35), rgba(20,22,35,.25));
  border: 2px solid rgba(109,40,217,.15);
  border-radius: 16px;
}
.stat-card:hover {
  border-color: rgba(109,40,217,.30);
  box-shadow: 0 0 8px rgba(109,40,217,.30);
  color: #c592f6;
}
.pill{
  padding:6px;
  border-radius:10px;
  background: rgba(124,58,237,.18);
  color:#a78bfa;
}
.mt-2{
  color: aliceblue;
}
</style>
