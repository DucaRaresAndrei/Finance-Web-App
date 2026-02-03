<template>
  <div class="card">
    <h2>Quick Actions</h2>

    <div v-if="state === 'idle'" class="actions">
      <button class="btn invert" @click="$emit('add-mode')">Add New Friend</button>
      <button class="btn ghost" @click="$emit('send-mode')">Send Money</button>
    </div>

    <div v-else class="input-block">
      <label>{{ state === 'add' ? 'Friend IBAN' : 'Receiver IBAN' }}</label>
      <input
        class="input"
        placeholder="ROxx xxxx xxxx xxxx ..."
        v-model.trim="iban"
        @keydown.enter.prevent="confirm"
      />

      <template v-if="state === 'send'">
        <label>Amount</label>
        <input
          class="input"
          type="number"
          placeholder="e.g. 250"
          min="1"
          step="1"
          v-model.number="amount"
          @keydown.enter.prevent="confirm"
        />

        <label>Message (optional)</label>
        <input
          class="input"
          placeholder="Description..."
          v-model.trim="message"
          @keydown.enter.prevent="confirm"
        />
      </template>

      <div class="row end">
        <button class="btn ghost" @click="cancel">Cancel</button>
        <button
          class="btn"
          :disabled="state==='add' ? !iban : (!iban || !amount || amount<=0)"
          @click="confirm"
        >
          Confirm
        </button>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
const props = defineProps<{ state: 'idle' | 'add' | 'send' }>()
const emit = defineEmits<{
  (e: 'add-mode'): void
  (e: 'send-mode'): void
  (e: 'back'): void
  (e: 'add-friend', iban: string): void
  (e: 'send-money', iban: string, amount: number, message?: string): void
  (e: 'validation-error', msg: string): void
}>()

const iban = ref('')
const amount = ref<number | null>(null)
const message = ref('')

watch(() => props.state, () => { iban.value=''; amount.value=null; message.value='' })

const normIban = () => (iban.value || '').replace(/\s+/g,'').toUpperCase()

function cancel(){ emit('back') }
// function confirm(){
//   if (props.state === 'add') {
//     if (!iban.value) return
//     emit('add-friend', iban.value);
//     emit('back')
//   } else if (props.state === 'send') {
//     if (!iban.value || !amount.value || amount.value <= 0) return
//     emit('send-money', iban.value, amount.value, message.value || undefined);
//     emit('back')
//   }
// }

function confirm(){
  if (props.state === 'add') {
    const n = normIban()
    if (!n) { emit('validation-error', 'Please enter an IBAN.'); return }
    if (!n.startsWith('RO') || n.length !== 18) {
      emit('validation-error', 'IBAN must be 18 characters (RO + 16), e.g. ROxxxxxxxxxxxxxxxx.')
      return
    }
    emit('add-friend', n)
    emit('back')
  } else if (props.state === 'send') {
    const n = normIban()
    if (!n) { emit('validation-error', 'Please enter an IBAN.'); return }
    if (!n.startsWith('RO') || n.length !== 18) {
      emit('validation-error', 'IBAN must be 18 characters (RO + 16), e.g. ROxxxxxxxxxxxxxxxx.')
      return
    }
    if (!amount.value || amount.value <= 0) {
      emit('validation-error', 'Amount must be a positive number.')
      return
    }
    emit('send-money', n, Number(amount.value), (message.value || undefined))
    emit('back')
  }
}
</script>

<style scoped>
h2 { font-size: 1.05rem; font-weight: 800; margin: 0 0 12px; }

.actions { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
/* Add new friend button */
.btn.invert { background: var(--white); color: var(--black); border-color: var(--white); }
.btn.invert:hover { background: var(--muted); color: var(--btn-bg-hover); }

.input-block { display: grid; gap: 10px; }
label { font-size: .9em; color: var(--muted); }
.row { display: flex; align-items: center; justify-content: space-between; gap: 10px; }
.row.end { justify-content: flex-end; }
</style>
