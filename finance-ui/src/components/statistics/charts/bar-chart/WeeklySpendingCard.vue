
<script setup lang="ts">
import { computed } from 'vue'
import WeeklySpending from "@/components/statistics/charts/bar-chart/WeeklySpending.vue";

type WeeklyBarDTO = { day: string; total: number }
const props = defineProps<{
  data: WeeklyBarDTO[]
  range: string
  loading: boolean
}>()

const emit = defineEmits<{
  (e: 'prev'): void; (e: 'next'): void
}>()

const total = computed(() => props.data.reduce((s, d) => s + d.total, 0))
const avg = computed(() => total.value / 7)
const money = (v: number) => new Intl.NumberFormat('ro-RO', { style: 'currency', currency: 'RON', maximumFractionDigits: 0 }).format(v)
</script>

<template>
  <v-card class="stat-card">
    <v-card-title class="d-flex align-center ga-2">
      <div class="icon-pill">
        <v-icon size="22">mdi-currency-usd</v-icon>
      </div>
      Weekly Spending

      <div class="week-nav">
        <button class="nav-btn" @click="$emit('prev')" :disabled="loading">
          <v-icon size="18">mdi-chevron-left</v-icon>
        </button>
        <div class="range">
          <v-icon size="16" class="me-1">mdi-calendar</v-icon>
          {{ range }}
        </div>
        <button class="nav-btn" @click="$emit('next')" :disabled="loading">
          <v-icon size="18">mdi-chevron-right</v-icon>
        </button>
      </div>

    </v-card-title>

    <v-card-subtitle class="mt-2">
      📊 Daily spending breakdown for the current week
    </v-card-subtitle>

    <v-card-text class="pl-2">
      <WeeklySpending :data="data" />
      <div class="mt-4 d-flex justify-space-between text-caption">
        <div class="chip chip-blue">📅 Weekly Total: <strong>{{ money(total) }}</strong></div>
        <div class="chip chip-indigo">📈 Avg/Day: <strong>{{ money(avg) }}</strong></div>
      </div>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.stat-card{
  background: linear-gradient(135deg, rgba(30,41,59,.35), rgba(15,23,42,.25));
  border: 2px solid rgba(59,130,246,.15);
  border-radius: 16px;
}
.stat-card:hover {
  border-color: rgba(59, 130, 246,.30);
  box-shadow: 0 0 8px rgba(59,130,246,.30);
  color: #76a1f3;
}
.icon-pill{
  padding: 6px;
  border-radius: 10px;
  background: rgba(30,58,138,.35);
  color: #93c5fd;
}
.mt-4{ margin-top: 1rem; }
.mt-2 {
  color: aliceblue;
}
.pl-2{
  padding-left: .5rem;
}
.text-caption{ font-size: .85rem; }
.chip{
  padding: 6px 10px;
  border-radius: 9999px;
  font-weight: 500;
}
.chip-blue{
  background: rgba(30,58,138,.35);
  color: #93c5fd;
}
.chip-indigo{
  background: rgba(49,46,129,.35);
  color: #c7d2fe;
}
.week-nav{
  margin-left: auto;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}
.nav-btn{
  width: 28px; height: 28px;
  background: rgba(148,163,184,.10);
  border: 1px solid rgba(148,163,184,.2);
  color: #c7d2fe;
  border-radius: 9999px;
  display: grid; place-items: center;
  transition: 120ms ease;
}
.nav-btn:hover{
  background: rgba(148,163,184,.08);
  border-color: rgba(148,163,184,.30);
  color: #fff;
}
.range {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: rgba(148,163,184,.10);
  border: 1px solid rgba(148,163,184,.2);
  color: #c7d2fe;
  padding: 4px 8px;
  border-radius: 9999px;
  font-size: .90rem;
  font-weight: 700;
}

</style>
