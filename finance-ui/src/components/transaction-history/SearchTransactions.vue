<script setup lang="ts">
import { ref, watch } from 'vue';

const searchQuery = ref('');
const emit = defineEmits(['update:query']);

// Emite evenimentul la fiecare 300ms pentru a evita un număr mare de căutări
let timeoutId: ReturnType<typeof setTimeout> | null = null;
watch(searchQuery, (newValue) => {
  if (timeoutId) {
    clearTimeout(timeoutId);
  }
  timeoutId = setTimeout(() => {
    emit('update:query', newValue);
  }, 300);
});
</script>

<template>
  <div class="search-bar">
    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 search-icon">
      <path stroke-linecap="round" stroke-linejoin="round" d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z" />
    </svg>
    <input
      v-model="searchQuery"
      type="text"
      placeholder="Search transactions..."
      class="search-input"
    />
  </div>
</template>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background-color: var(--input-bg);
  border-radius: 8px;
  border: 1px solid #3a3a3c;
  margin-bottom: 24px;
}
.search-icon {
  width: 20px;
  height: 20px;
  color: var(--muted);
}
.search-input {
  flex-grow: 1;
  background: none;
  border: none;
  outline: none;
  color: #fff;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  font-size: 14px;
}
.search-input::placeholder {
  color: var(--muted);
}
</style>
