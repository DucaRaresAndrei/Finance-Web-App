<template>
  <div>
    <h2>Search Friends</h2>
    <input
      class="input"
      type="text"
      placeholder="Search by name or email..."
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    />
  </div>
</template>

<script setup lang="ts">
defineProps<{ modelValue: string }>()
defineEmits<{ (e: 'update:modelValue', v: string): void }>()
</script>

<style scoped>
h2 { font-size: 1.05rem; font-weight: 800; margin: 0 0 12px; }
.input { width: 100%; }
</style>
