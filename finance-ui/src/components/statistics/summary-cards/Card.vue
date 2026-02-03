<script setup lang="ts">
import { computed } from 'vue'

type Period = 'month' | 'week'
type Tone = 'success' | 'error' | 'info'

interface Props {
  title: string
  amount: number
  icon?: string
  tone?: Tone
  period?: Period
  showBadge?: boolean
  isPositive?: boolean
  emoji?: string
}
const props = withDefaults(defineProps<Props>(), {
  icon: 'mdi-currency-usd',
  tone: 'info',
  period: 'month',
  showBadge: false,
  isPositive: true,
  emoji: '💰',
})
const formatted = computed(() =>
  new Intl.NumberFormat('ro-RO', { style: 'currency', currency: 'RON', maximumFractionDigits: 0 })
    .format(props.amount)
)
const toneColor = computed(() => ({
  success: '#22c55e', // verde
  error:   '#ef4444', // roșu
  info:    '#3b82f6', // albastru
}[props.tone]))
</script>


<template>
  <v-card class="stat-card" :style="{'--tone': toneColor}">
    <v-card-text>
      <div class="row-top">
        <div class="title">{{ title }}</div>
        <div class="icon-pill">
          <v-icon :icon="props.icon" size="18" />
        </div>
      </div>

      <div class="amount">
        {{ formatted }}
      </div>

      <div class="row-bottom">
        <v-chip
          v-if="showBadge"
          :color="isPositive ? 'primary' : 'error'"
          size="small"
          label
          variant="elevated"
        >
          {{ isPositive ? 'Saving' : 'Overspending' }}
        </v-chip>
        <span class="period">
          {{ emoji }} {{ period === 'month' ? 'This month' : 'This week' }}
        </span>
      </div>
    </v-card-text>
  </v-card>
</template>


<style scoped>
.stat-card{
  --tone: #3b82f6;
  --bg1: color-mix(in oklab, var(--tone) 8%, #0b1220);
  --bg2: color-mix(in oklab, var(--tone) 2%, #0b1220);
  background: linear-gradient(160deg, var(--bg1), var(--bg2));
  border: 2px solid rgba(255,255,255,.06);
  border-radius: 16px;
  transition: transform .25s ease, box-shadow .25s ease, border-color .25s ease;
}
.stat-card:hover{
  transform: translateY(-2px);
  border-color: color-mix(in oklab, var(--tone) 55%, transparent);
  box-shadow: 0 0 8px var(--tone);
}
.row-top{
  display:flex;
  align-items:center;
  justify-content:space-between;
  margin-bottom: 16px;
}
.title{
  font-size:1rem;
  font-weight:600;
  letter-spacing:.2px;
}
.icon-pill{
  width:34px;
  height:34px;
  border-radius:999px;
  display:grid;
  place-items:center;
  background: color-mix(in oklab, var(--tone) 20%, #0b1220);
  color: color-mix(in oklab, var(--tone) 80%, #ffffff);
}
.amount{
  font-size: 1.6rem;
  font-weight:600;
  letter-spacing:.3px;
  color: var(--tone);
  margin: 2px 0 8px;
}
.row-bottom{
  display:flex;
  align-items:center;
  gap:10px;
}
.period{
  font-size:.75rem;
  opacity:.8
}
</style>
