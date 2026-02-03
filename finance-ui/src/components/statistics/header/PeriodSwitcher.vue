<script setup lang="ts">
import { computed } from 'vue'

type Period = 'month' | 'week'
const props = defineProps<{ state: Period }>()
const emit = defineEmits<{
  (e: 'update:state', v: Period): void
  (e: 'change', v: Period): void
}>()

const localValue = computed({
  get: () => props.state ?? 'month',
  set: (v: Period) => {
    emit('update:state', v)
    emit('change', v)
  },
})
</script>


<template>
  <v-btn-toggle
    v-model="localValue"
    mandatory
    density="comfortable"
    class="period-toggle"
    variant="elevated">
    <v-btn value="month">
      <v-icon start>mdi-calendar-month</v-icon>
      Month
    </v-btn>
    <v-btn value="week" >
      <v-icon start>mdi-chart-bar</v-icon>
      Week
    </v-btn>
  </v-btn-toggle>
</template>


<style scoped>
.period-toggle {
  border-radius: 8px;
}
.period-toggle :deep(.v-btn) {
  padding: 8px 16px;
  text-transform: none;
  letter-spacing: .2px;
  font-weight: 600;
  border-radius: 8px;
  margin:1px;
}
.period-toggle :deep(.v-btn--active) {
  color: #3b82f6;
}
</style>

