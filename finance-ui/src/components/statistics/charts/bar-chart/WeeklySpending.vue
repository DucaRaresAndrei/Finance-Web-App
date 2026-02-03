<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import {
  Chart as ChartJS,
  CategoryScale, LinearScale,
  BarElement,
  BarController,
  Tooltip, Legend,
  type ChartOptions
} from 'chart.js'
ChartJS.register(CategoryScale, LinearScale, BarElement, BarController, Tooltip, Legend)

type WeeklyBarDTO = { day: string; total: number }
const props = defineProps<{ data: WeeklyBarDTO[] }>()

const canvasEl = ref<HTMLCanvasElement | null>(null)
let chart: ChartJS | null = null

const order = ['MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY']
const labels = () => order.map(d => d.slice(0,3))
const values = () => {
  const m = new Map(props.data.map(i => [i.day.toUpperCase(), i.total]))
  return order.map(d => Number(m.get(d) ?? 0))
}
const money = (v: number) =>
  new Intl.NumberFormat('ro-RO',{style:'currency',currency:'RON',maximumFractionDigits:0}).format(v)

function render() {
  if (!canvasEl.value) return
  const ctx = canvasEl.value.getContext('2d'); if (!ctx) return
  if (chart) { chart.destroy(); chart = null }

  const options: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        backgroundColor: '#0f1115',
        borderColor: '#334155',
        borderWidth: 1,
        titleColor: '#e5e7eb',
        bodyColor: '#e5e7eb',
        callbacks: { label: (c) => ` ${money(Number(c.parsed.y))}` }
      }
    },
    scales: {
      x: { ticks: { color: '#e5e7eb'}, grid: { display: false } },
      y: { beginAtZero: true, ticks: { color: '#9ca3af', callback: (v) => money(Number(v)) },
        grid: { color: 'rgba(148,163,184,0.15)' } }
    },
    normalized: true
  }

  chart = new ChartJS(ctx, {
    type: 'bar',
    data: {
      labels: labels(),
      datasets: [{
        data: values(),
        backgroundColor: '#0ea5e9',
        borderRadius: 8,
        borderSkipped: false
      }]
    },
    options
  })
}

watch(() => props.data, render, { deep: true })
onMounted(render)
onUnmounted(() => { chart?.destroy() })
</script>

<template>
  <div class="chart-wrap">
    <canvas ref="canvasEl" class="chart-canvas"></canvas>
  </div>
</template>

<style scoped>
.chart-wrap{
  position: relative;
  height: 320px;
  width: 100%;
}
.chart-canvas{
  display: block;
  width: 100%;
  height: 100%;
}
</style>
