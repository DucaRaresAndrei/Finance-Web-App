<script setup lang="ts">
import { computed } from 'vue'
import PeriodSwitcher from './PeriodSwitcher.vue'

type Period = 'month' | 'week'

const props = defineProps<{ period?: Period }>()
const emit = defineEmits<{
  (e: 'update:period', v: Period): void
  (e: 'change', v: Period): void }>()

const inner = computed<Period>({
  get: () => props.period ?? 'month',
  set: (v) => {
    emit('update:period', v)
    emit('change', v)
  },
})
</script>


<template>
  <div class="stats-header">
    <div class="titles">
      <h1 class="title">Financial Dashboard</h1>
      <p class="subtitle">Track your income, expenses, and savings</p>
    </div>

    <div class="actions">
      <v-sheet
        class="switcher-box"
      >
        <PeriodSwitcher v-model:state="inner" />
      </v-sheet>
    </div>
  </div>
</template>


<style scoped>
.stats-header {
  display: flex;
  justify-content: space-between;
}
.title {
  letter-spacing: .2px;
}
.subtitle {
  margin: 4px 0 0;
  opacity: .8;
}
.switcher-box{
  padding: 4px;
  border-radius: 8px;
}
.actions {
  display: flex;
  align-items: center;
}
</style>
