<script setup lang="ts">
import { watch, onBeforeUnmount } from 'vue'

const props = withDefaults(defineProps<{
  modelValue: boolean
  message?: string
  autoCloseMs?: number | null
}>(), {
  message: '',
  autoCloseMs: 5000,
})

const emit = defineEmits<{ (e:'update:modelValue', v:boolean): void }>()

let timer: number | null = null
function clearTimer(){ if (timer){ window.clearTimeout(timer); timer = null } }
function close(){ clearTimer(); emit('update:modelValue', false) }

watch(() => props.modelValue, (open) => {
  clearTimer()
  if (open && props.autoCloseMs && props.autoCloseMs > 0) {
    timer = window.setTimeout(() => emit('update:modelValue', false), props.autoCloseMs) as unknown as number
  }
})

onBeforeUnmount(clearTimer)
</script>

<template>
  <transition name="lb-fade">
    <div v-if="props.modelValue" class="lb" role="status" aria-live="polite">
      <div class="lb-card">
        <div class="lb-title">
          <span>Low Balance</span>
          <button class="lb-x" aria-label="Close" @click="close">×</button>
        </div>
        <div class="lb-msg">{{ props.message }}</div>
      </div>
    </div>
  </transition>
</template>

<style scoped>
.lb{
  position: fixed;
  right: 22px;
  bottom: 22px;
  z-index: 3000;
}
.lb-card{
  background: transparent;
  color: var(--txt);
  border: 1px solid var(--line);
  border-radius: 12px;
  min-width: 280px;
  max-width: 420px;
  box-shadow: var(--shadow);
  padding: 12px 12px 14px;
}
.lb-title{
  display:flex; align-items:center; justify-content:space-between;
  font-weight: 800; letter-spacing: .2px; margin-bottom: 6px;
}
.lb-x{
  appearance: none; border: 1px solid var(--line);
  background: var(--btn-bg); color: var(--txt);
  width: 28px; height: 28px; border-radius: 8px; cursor: pointer;
}
.lb-x:hover{ background: var(--btn-bg-hover); }
.lb-msg{ color: var(--txt); line-height: 1.35; }

.lb-fade-enter-active, .lb-fade-leave-active { transition: opacity 140ms ease, transform 140ms ease; }
.lb-fade-enter-from, .lb-fade-leave-to { opacity: 0; transform: translateY(6px); }
</style>
