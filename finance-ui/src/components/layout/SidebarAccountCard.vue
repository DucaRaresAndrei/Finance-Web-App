<script setup lang="ts">
import {computed, onBeforeUnmount, onMounted, ref} from "vue";
import {UserControllerService, type UserResponseDTO} from "@/services";

const owner = ref<string>('')
const balance = ref<number>(0)
const iban = ref<string>('')
const money = (v: number) =>
  new Intl.NumberFormat('ro-RO', { style: 'currency', currency: 'RON', maximumFractionDigits: 2 }).format(v)
let pollTimer: number | null = null;

function formatIban(raw?: string) {
  const s = (raw ?? '').replace(/\s+/g, '')
  if (!s) return 'IBAN unavailable'

  const cc = s.slice(0, 2)
  const rest = s.slice(2)

  const groups = rest.match(/.{1,4}/g) ?? []
  return [cc, ...groups].join(' ')
}

const ibanPretty = computed(() => formatIban(iban.value))
async function loadData() {
  try {
    const user: UserResponseDTO = await UserControllerService.getCurrentUser()
    owner.value = user.fullName
    balance.value = user.account?.balance ?? 0
    iban.value = user.account?.iban ?? ''
  } catch (error) {
    console.error('Error loading user data:', error);
  }
}
onMounted(async () => {
  await loadData();
  pollTimer = window.setInterval(loadData, 5000);
})
onBeforeUnmount(() => {
  if (pollTimer) {
    clearInterval(pollTimer);
  }
})
</script>

<template>
  <div class="mini-card">
    <div class="row-top">
      <div class="who">
        <v-avatar size="28" class="avatar">
          <v-icon size="18">mdi-account</v-icon>
        </v-avatar>
        <div class="owner" :title="owner">{{ owner }}</div>
      </div>
    </div>
    <div class="label">Total Balance</div>
    <div class="amount">
      {{ money(balance) }}
    </div>

    <div class="cardline mono">
      {{ ibanPretty }}
    </div>
  </div>
</template>

<style scoped>
.mini-card{
  background: linear-gradient(140deg, #2b3edc 0%, #461997 45%, #5c263a 100%);
  border-radius: 16px;
  padding: 14px;
  color: #eef4ff;
  box-shadow:
    inset 0 0 0 1px rgba(255,255,255,.14),
    0 10px 20px rgba(0,0,0,.30);
  position: relative;
  overflow: hidden;
}

.row-top{
  display:flex; align-items:center; justify-content:space-between;
  margin-bottom: 6px;
}

.who{ display:flex; align-items:center; gap:8px; min-width: 0; }
.avatar{ background: rgba(255,255,255,.22); color:#fff; }
.owner{ font-weight: 700; letter-spacing:.2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

.label{
  font-size: 11px; letter-spacing: .6px; opacity: .9;
  padding-top: 12px;
}
.amount{
  font-size: 22px; font-weight: 800; margin: 2px 0 6px; letter-spacing: .2px;
}
.cardline{
  font-size: 12px; letter-spacing: 2px; opacity: .95;
}
.mono{ font-family: ui-monospace; }

</style>
