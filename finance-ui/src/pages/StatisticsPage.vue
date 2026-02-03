<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

import StatsHeader from '@/components/statistics/header/StatsHeader.vue'
import SummaryCards from '@/components/statistics/summary-cards/SummaryCards.vue'
import WeeklySpendingCard from '@/components/statistics/charts/bar-chart/WeeklySpendingCard.vue'
import SpendingByCategoryAndLegendCard
  from '@/components/statistics/charts/pie-chart/SpendingByCategoryAndLegendCard.vue'

import { StatsControllerService } from '@/services/services/StatsControllerService'
import type { DashboardStatsDTO } from '@/services/models/DashboardStatsDTO'

type Period = 'month' | 'week'

const period = ref<Period>('month')
const stats  = ref<DashboardStatsDTO | null>(null)
const loading = ref(false)
const error   = ref<string | null>(null)

const weekOffset = ref(0)

async function load() {
  loading.value = true
  error.value = null
  try {
    stats.value = await StatsControllerService.getDashboardStats(weekOffset.value)
  } catch (e: any) {
    error.value = e?.message ?? 'Failed to load stats'
  } finally {
    loading.value = false
  }
}
function prevWeek() {
  weekOffset.value -= 1;
  load()
}
function nextWeek() {
  if (weekOffset.value < 0) {
    weekOffset.value += 1;
    load()
  }
}
onMounted(load)

const summary = computed(() => {
  if (!stats.value) return { incomes: 0, expense: 0, netSavings: 0 }
  return period.value === 'month' ? stats.value.monthSummary : stats.value.weekSummary
})

const summaryData = computed(() => ({
  income: summary.value?.incomes ?? 0,
  expenses: summary.value?.expense ?? 0,
  net: summary.value?.netSavings ?? 0,
}))

const weeklyBars = computed(() => stats.value?.weeklySpending ?? [])

const categoryRows = computed(() =>
  (stats.value?.spendingByCategory ?? [])
    .filter(c => c.category !== 'USER')
    .map(c => ({ category: c.category, amount: c.total }))
)

const weekRange = computed(() => {
  const today = new Date()
  const start = startOfWeekMonday(new Date(today.getFullYear(), today.getMonth(), today.getDate() + weekOffset.value * 7))
  const end = new Date(start)
  end.setDate(start.getDate() + 6)
  const fmt = (d: Date) => d.toLocaleDateString('en-US', { month: 'short', day: '2-digit' })
  return `${fmt(start)} - ${fmt(end)}`
})

function startOfWeekMonday(date: Date) {
  const day = date.getDay() === 0 ? 7 : date.getDay()
  const monday = new Date(date)
  monday.setDate(date.getDate() - (day - 1))
  monday.setHours(0, 0, 0, 0)
  return monday
}

function onPeriodChange(p: Period) {
  period.value = p
}
</script>

<template>
  <v-container class="stats-page" fluid>
    <StatsHeader v-model:period="period" @change="onPeriodChange" />
    <v-alert
      v-if="error"
      type="error"
      class="mb-3"
      density="comfortable"
      variant="tonal"
    >
      {{ error }}
      <v-btn size="small" class="ml-2" @click="load">Retry</v-btn>
    </v-alert>
    <v-skeleton-loader
      v-if="loading && !stats"
      type="article, card, card"
      class="mb-3"
    />

    <template v-else>
      <SummaryCards :period="period" :data="summaryData" />
      <WeeklySpendingCard
        :data="weeklyBars"
        :range="weekRange"
        :loading="loading"
        @prev="prevWeek"
        @next="nextWeek"
        class="mt-4" />
      <SpendingByCategoryAndLegendCard :rows="categoryRows" class="mt-4" />
    </template>
  </v-container>
</template>

<style scoped>
.stats-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}
</style>
