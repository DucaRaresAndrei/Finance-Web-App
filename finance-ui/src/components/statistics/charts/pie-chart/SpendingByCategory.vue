<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch, computed } from 'vue'
import { Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  type ChartData, type ChartOptions,
  PieController
} from 'chart.js'
ChartJS.register(ArcElement, Tooltip, Legend, PieController)

type Row = { category: string; amount: number }
const props = defineProps<{ rows: Row[]; colors?: string[] }>()
const filtered = computed(() => (props.rows ?? []).filter(r => r.category !== 'USER'))
const palette  = computed(() => props.colors ?? ['#a855f7','#22c55e','#f97316','#0ea5e9','#ef4444','#8b5cf6'])

const canvasRef = ref<HTMLCanvasElement|null>(null)
const total = computed(() => filtered.value.reduce((sum, r) => sum + r.amount, 0))
const options: ChartOptions<'pie'> = {
  responsive: true,
  maintainAspectRatio: false,
  layout: {
    padding: 10
  },
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: '#0f1220', borderColor: '#3b82f6', borderWidth: 2, padding: 10,
      titleColor: '#e5e7eb', bodyColor: '#e5e7eb', displayColors: true,
      callbacks: {
        label: (c) => {
          const amount = c.parsed as number
          const percent = total.value === 0 ? 0 : (amount / total.value) * 100
          return ` ${amount.toLocaleString('ro-RO')} RON (${percent.toFixed(1)}%)`
        }
      }
    }
  }
}
let chart: ChartJS<'pie'> | null = null

function formatPercent(amount: number): string {
  if (total.value === 0) return '0%'
  return ((amount / total.value) * 100).toFixed(1) + '%'
}
function buildData(): ChartData<'pie'> {
  return {
    labels: filtered.value.map(r => r.category),
    datasets: [{
      data: filtered.value.map(r => r.amount),
      backgroundColor: palette.value,
      borderWidth: 0,
      hoverOffset: 12,
      hoverBorderWidth: 0
    }],
  }
}
function render() {
  if (!canvasRef.value) return
  const ctx = canvasRef.value.getContext('2d'); if (!ctx) return
  if (chart) { chart.destroy(); chart = null }
  chart = new ChartJS<'pie'>(ctx, { type: 'pie', data: buildData(), options })
}

onMounted(render)
onBeforeUnmount(() => chart?.destroy())
watch(() => props.rows, render, { deep: true })
</script>

<template>
  <div class="pie-wrap">
    <div class="pie">
      <canvas ref="canvasRef" class="pie-canvas"></canvas>
    </div>

    <ul class="legend">
      <li v-for="(r, i) in filtered" :key="r.category">
        <span class="dot" :style="{ backgroundColor: palette[i % palette.length] }" />
        <span class="name">{{ r.category }}</span>
        <span class="value">{{ r.amount.toLocaleString('ro-RO') }} RON {{formatPercent(r.amount)}}</span>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.pie-wrap {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 12px;
  min-height: 350px;
}
.pie{
  position: relative;
  width: 100%;
  height: 320px;
}
.pie-canvas{
  display: block;
  width: 100%;
  height: 100%;
}
.legend {
  list-style: none;
  margin: 0;
  padding: 50px;
}
.legend li {
  display: grid;
  grid-template-columns: 14px 1fr auto;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 10px;
}
.legend li:hover { background: #2a163e; }
.dot   { width: 14px; height: 14px; border-radius: 50%; }
.name  { color: #e5e7eb; font-weight: 600; letter-spacing: .2px; }
.value { color: #93c5fd; font-weight: 600; gap: 30px}
</style>
