<script setup lang="ts">
type Cat = 'ALL' | 'BILLS' | 'ENTERTAINMENT' | 'FOOD' | 'SHOPPING' | 'TRANSPORTATION';

const props = defineProps<{ modelValue: Cat }>();
const emit = defineEmits<{ (e: 'update:modelValue', v: Cat): void }>();

const items: Cat[] = ['ALL', 'BILLS', 'ENTERTAINMENT', 'FOOD', 'SHOPPING', 'TRANSPORTATION'];
</script>

<template>
  <div>
    <h2>Search Firms by Category</h2>
    <v-select
      :items="items"
      :model-value="props.modelValue"
      @update:model-value="(v) => emit('update:modelValue', v as Cat)"
      variant="outlined"
      density="comfortable"
      hide-details
      class="w-100"
      :style="{ '--v-theme-surface': 'var(--card)', '--v-theme-on-surface': 'var(--txt)' }"
    />
  </div>
</template>

<style scoped>
h2{ font-size: 1.05rem; font-weight: 800; margin: 0 0 12px; }

.w-100:deep(.v-field){
  border-radius: 10px;
  height: 53px;
}
.w-100:deep(.v-field__input){
  min-height: 53px;
  padding: 0 12px;
}
</style>
