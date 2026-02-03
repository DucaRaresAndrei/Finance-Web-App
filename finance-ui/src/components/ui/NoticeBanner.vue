<script setup lang="ts">
import { watch, onBeforeUnmount } from 'vue'

const props = withDefaults(defineProps<{
  modelValue: boolean
  type?: 'error' | 'success'
  message?: string
  autoCloseMs?: number | null
  position?: 'top-right' | 'bottom-right' | 'top-center'
}>(), {
  type: 'success',
  message: '',
  autoCloseMs: null,
  position: 'top-right',
})
const emit = defineEmits<{ (e:'update:modelValue', v:boolean): void }>()

let timer: number | null = null
function clearTimer(){ if (timer) { window.clearTimeout(timer); timer = null } }

watch(() => props.modelValue, (open) => {
  clearTimer()
  if (open && props.autoCloseMs && props.autoCloseMs > 0) {
    timer = window.setTimeout(() => emit('update:modelValue', false), props.autoCloseMs) as unknown as number
  }
})

onBeforeUnmount(clearTimer)
</script>

<template>
  <transition name="nb-fade">
    <div v-if="modelValue"
         class="nb"
         :class="[position, type === 'error' ? 'nb-err' : 'nb-ok']"
         role="status" aria-live="polite">
      <div class="nb-card">
        <div class="nb-title">
          <span>{{ type === 'error' ? 'Error' : 'Success' }}</span>
          <button class="nb-x" aria-label="Close" @click="$emit('update:modelValue', false)">×</button>
        </div>
        <div class="nb-msg">{{ message }}</div>
      </div>
    </div>
  </transition>
</template>

<style scoped>
.nb{ position:fixed; z-index:3100; padding: 0 6px; }
.nb.top-right{ top: 18px; right: 18px; }
.nb.bottom-right{ bottom: 18px; right: 18px; }
.nb.top-center{ top: 18px; left: 50%; transform: translateX(-50%); }

.nb-card{
  background: transparent;
  color: var(--txt);
  border: 1px solid var(--line);
  border-radius: 12px;
  min-width: 280px; max-width: 520px;
  box-shadow: var(--shadow);
  padding: 12px 12px 14px;
}
.nb-err .nb-card{ border-color: rgba(239,68,68,.45); box-shadow: 0 0 0 2px rgba(239,68,68,.08) inset; }
.nb-ok  .nb-card{ border-color: rgba(34,197,94,.45); box-shadow: 0 0 0 2px rgba(34,197,94,.08) inset; }

.nb-title{ display:flex; align-items:center; justify-content:space-between; font-weight:800; margin-bottom:6px; }
.nb-x{
  appearance:none; border:1px solid var(--line);
  background: var(--btn-bg); color: var(--txt);
  width:28px; height:28px; border-radius:8px; cursor:pointer;
}
.nb-x:hover{ background: var(--btn-bg-hover); }
.nb-msg{ line-height:1.35; }

.nb-fade-enter-active, .nb-fade-leave-active { transition: opacity 140ms ease, transform 140ms ease; }
.nb-fade-enter-from,  .nb-fade-leave-to    { opacity:0; transform: translateY(-6px); }
</style>
