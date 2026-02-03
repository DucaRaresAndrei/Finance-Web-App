<template>
  <div class="card-wrap">
    <div class="card-3d" :class="{ flipped: !hidden }">
      <div class="face front rainbow-card">
        <div class="top">
          <div class="who">
            <v-avatar size="36" class="avatar"><v-icon icon="mdi-account" size="22" /></v-avatar>
            <div class="name">{{ owner }}</div>
          </div>
          <v-btn icon variant="text" class="eye" @click="toggle()">
            <v-icon :icon="hidden ? 'mdi-eye-off-outline' : 'mdi-eye-outline'"/>
          </v-btn>
        </div>

        <div class="label">Total Balance</div>
        <div class="amount">{{ currency(balance) }}</div>

        <div class="cardline mono"> {{ cardNumberMasked }}</div>
      </div>

      <div class="face back rainbow-card">
        <div class="top back-top">
          <span class="virtual mono">Virtual</span>
          <v-btn icon variant="text" class="eye" @click="toggle()">
            <v-icon :icon="hidden ? 'mdi-eye-off-outline' : 'mdi-eye-outline'"/>
          </v-btn>
        </div>

        <div class="back-fields">
          <div class="field">
            <div class="field-label">Card number</div>
            <div class="field-value mono">{{ fullCardNumber }}</div>
          </div>

          <div class="row">
            <div class="field">
              <div class="field-label">Expiry date</div>
              <div class="field-value mono">{{ expiry }}</div>
            </div>
            <div class="field">
              <div class="field-label">CVV</div>
              <div class="field-value mono">{{ cvvNumber }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface Props {
  balance: number
  owner: string
  cardNumberMasked?: string
  cardNumber?: string
  expirationDate?: string
  cvvNumber?: string | number
}
const props = withDefaults(defineProps<Props>(), {
  cardNumberMasked: '**** **** **** ****',
  cardNumber: '',
  expirationDate: '',
  cvvNumber: '',
})

const hidden = ref(true)
const toggle = () => (hidden.value = !hidden.value)

const fullCardNumber = computed(() => {
  return props.cardNumber.replace(/\D/g, '').replace(/(\d{4})(?=\d)/g, '$1 ').trim()
})
const expiry = computed(() => {
  const [y, m] = props.expirationDate.split('-')
  return `${m}/${y.slice(-2)}`
})

function currency(n: number) {
  return new Intl.NumberFormat('ro-RO', {
    style: 'currency',
    currency: 'RON',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(n)
}
</script>

<style scoped>
.card-wrap {
  perspective: 1200px;
}
.card-3d {
  position: relative;
  width: 100%;
  min-height: 168px;
  transform-style: preserve-3d;
  transition: transform 520ms cubic-bezier(.2,.65,.25,1);
}
.card-3d.flipped {
  transform: rotateY(180deg);
}

.face {
  position: absolute;
  inset: 0;
  backface-visibility: hidden;
  border-radius: 16px;
}
.back { transform: rotateY(180deg); }

.rainbow-card{
  padding: 18px;
  background: linear-gradient(135deg,
  #7499da 0%, #2b3edc 25%, #461997 45%, #5c263a 85%, #f5576c 160%);
  box-shadow:
    inset 0 0 0 1px rgba(255,255,255,.14),
    0 20px 40px rgba(94, 109, 214, 0.11),
    0 8px 16px rgba(0,0,0,.20);
  overflow: hidden;

}
.top{
  display:flex; align-items:center; justify-content:space-between;
  margin-bottom: 8px;
}

.who{ display:flex; align-items:center; gap:10px; }
.avatar{ background: rgba(255,255,255,0.18); color:#fff; }
.name{ font-weight: 700; font-size: 16px; letter-spacing:.2px; }
.eye{ color: rgba(255,255,255,.9); min-width: 32px; }

.label{
  font-size: 13px;
  color: rgba(255,255,255,.85);
  margin-top: 4px;
}
.amount{
  font-size: 32px; font-weight: 800; margin: 6px 0 10px; letter-spacing:.2px;
  text-shadow: 0 2px 0 rgba(0,0,0,.25);
}
.cardline{
  font-size: 14px; letter-spacing: 2px; color: rgba(255,255,255,.88);
}

.back-top { margin-bottom: 6px; }
.virtual{
  margin-left: auto;
  padding: 4px 12px;
  border-radius: 9999px;
  background: rgba(255,255,255,.14);
  letter-spacing: .2px;
}
.back-fields{
  position: absolute;
  left: 18px; right: 18px; bottom: 16px;
  display: flex; flex-direction: column; gap: 10px;
}
.row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }

.field-label{
  font-size: 12px; opacity: .85; letter-spacing: .2px;
}

.field-value{
  font-weight: 700; letter-spacing: .4px; margin-top: 2px;
  text-shadow: 0 1px 0 rgba(0,0,0,.15);
  word-spacing: 2px;
}
.mono{ font-family: ui-monospace; }

@media (min-width:1280px){
  .card-3d{ min-height: 220px; }
  .rainbow-card{ padding: 22px; border-radius: 24px; }
  .amount{ font-size: 38px; }
}
</style>
