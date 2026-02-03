<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'

import {
  SettingsControllerService,
  type SettingsRespDTO,
  type UpdateSettingsReqDTO,
} from '@/services'

const loading = ref(true)
const saving = ref(false)
const errorMsg = ref<string | null>(null)
const successMsg = ref<string | null>(null)
let errTimer: number | null = null
let okTimer: number | null = null

function clearTimers() {
  if (errTimer) { clearTimeout(errTimer); errTimer=null }
  if (okTimer) { clearTimeout(okTimer); okTimer=null } }

function setError(msg:string) {
  clearTimers(); errorMsg.value = msg; successMsg.value = null
  errTimer = window.setTimeout(() => { errorMsg.value = null }, 5000) as unknown as number
}
function setSuccess(msg:string) {
  clearTimers(); successMsg.value = msg; errorMsg.value = null
  okTimer = window.setTimeout(() => { successMsg.value = null }, 3000) as unknown as number
}

const data = ref<SettingsRespDTO | null>(null)
const isReady = computed(() => !loading.value && data.value !== null)

const editThreshold = ref(false)
const newThreshold = ref<string>('')

const editPin = ref(false)
const newPin = ref<string>('')

const activePct = 70

const enabled = computed(() => !!data.value?.lowBalanceEnabled)
const formatThreshold = computed(() => {
  const v = data.value?.lowBalanceThreshold
  const n = typeof v === 'string' ? Number(v) : Number(v ?? 0)
  return isFinite(n) ? n.toLocaleString('ro-RO', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : '—'
})
const pinMsgLabel = computed(() => (data.value?.hasPinSet ? 'Change PIN' : 'Set PIN'))

async function load() {
  loading.value = true
  errorMsg.value = null
  successMsg.value = null
  try {
    data.value = await SettingsControllerService.getSettings()
  } catch (e: any) {
    // errorMsg.value = e?.message ?? 'Failed to load settings.'
    setError('Failed to load settings.')
  } finally {
    loading.value = false
  }
}

async function update(payload: UpdateSettingsReqDTO) {
  saving.value = true
  errorMsg.value = null
  successMsg.value = null
  try {
    data.value = await SettingsControllerService.updateSettings(payload)
    setSuccess('Settings saved.')
  } catch (e: any) {
    setError('Update failed.')
    throw e
  } finally {
    saving.value = false
  }
}

async function toggleLowBalance() {
  if (!data.value || saving.value) return
  await update({ lowBalanceEnabled: !data.value.lowBalanceEnabled })
}

function startEditThreshold() {
  editThreshold.value = true
  const v = data.value?.lowBalanceThreshold
  newThreshold.value = (typeof v === 'string' ? v : (v ?? 0)).toString()
}
function cancelEditThreshold() {
  editThreshold.value = false
  newThreshold.value = ''
}
async function confirmThreshold() {
  const val = (newThreshold.value ?? '').trim()
  if (!val) {
    setError('Please enter a threshold.')
    return
  }
  const num = Number(val.replace(',', '.'))
  if (!isFinite(num) || num < 0) {
    setError('Threshold must be a valid non-negative number.')
    return
  }
  await update({ lowBalanceThreshold: num as any })
  editThreshold.value = false
}
function onEnterThreshold() {
  const val = (newThreshold.value ?? '').trim()
  if (!val) return
  void confirmThreshold()
}

function startEditPin() {
  editPin.value = true
  newPin.value = ''
  nextTick(() => {
    const element = document.querySelector<HTMLInputElement>('#pin-input-settings')
    element?.focus()
  })
}
function cancelEditPin() {
  editPin.value = false
  newPin.value = ''
}
async function confirmPin() {
  const pin = (newPin.value || '').trim()
  if (!/^\d{4}$/.test(pin)) {
    setError('PIN must be exactly 4 digits.')
    return
  }
  await update({ newPin: pin })
  editPin.value = false
}
function onEnterPin() {
  const pin = (newPin.value || '').trim()
  if (!/^\d{4}$/.test(pin)) return
  void confirmPin()
}

onMounted(load)
</script>

<template>
  <div class="settings-card">
    <div class="row head">
      <div class="title">Settings</div>
      <div v-if="saving || loading" class="small muted">Loading…</div>
    </div>

    <div v-if="errorMsg" class="err">{{ errorMsg }}</div>
    <div v-if="successMsg" class="ok">{{ successMsg }}</div>

    <div class="block">
      <div class="label">Low balance alert</div>

      <div v-if="!isReady" class="bar-wrap loading">
        <div class="bar inactive full" aria-hidden="true"></div>
      </div>

      <div v-else class="bar-wrap" :class="{ on: enabled, off: !enabled }">
        <div
          class="bar active"
          :class="enabled ? 'green' : 'red'"
          :style="{
            width: activePct + '%',
            order: enabled ? 1 : 2
          }"
        >
          <span class="state-label">{{ enabled ? 'ENABLED' : 'DISABLED' }}</span>
        </div>

        <button
          class="bar inactive"
          :style="{
            width: (100 - activePct) + '%',
            order: enabled ? 2 : 1
          }"
          :disabled="saving"
          :title="enabled ? 'Disable low balance alert' : 'Enable low balance alert'"
          @click="toggleLowBalance"
        />
      </div>
    </div>

    <div class="block">
      <div class="label">Threshold limit</div>

      <div v-if="!editThreshold" class="row between">
        <div class="value">
          {{ formatThreshold }} <span class="muted">RON</span>
        </div>
        <button class="btn ghost" @click="startEditThreshold">Change limit</button>
      </div>

      <div v-else class="edit-row">
        <input
          class="input"
          v-model.trim="newThreshold"
          placeholder="e.g. 150.00"
          inputmode="decimal"
          @keydown.enter.prevent="onEnterThreshold"
        />
        <div class="row gap">
          <button class="btn ghost" @click="cancelEditThreshold">Cancel</button>
          <button
            class="btn"
            :disabled="saving || !(newThreshold && newThreshold.trim().length > 0)"
            @click="confirmThreshold"
          >Change</button>
        </div>
      </div>
    </div>

    <div class="block">
      <div class="label">Card PIN</div>

      <div v-if="!editPin" class="row between">
        <div class="value">
          <span class="chip" :class="data?.hasPinSet ? 'ok' : 'no'">
            {{ data?.hasPinSet ? 'PIN is set' : 'PIN not set' }}
          </span>
        </div>
        <button class="btn ghost" @click="startEditPin">{{ pinMsgLabel }}</button>
      </div>

      <div v-else class="edit-row">
        <input
          id="pin-input-settings"
          class="input"
          v-model.trim="newPin"
          placeholder="****"
          inputmode="numeric"
          maxlength="4"
          @keydown.enter.prevent="onEnterPin"
        />
        <div class="row gap">
          <button class="btn ghost" @click="cancelEditPin">Cancel</button>
          <button
            class="btn"
            :disabled="saving || !/^\d{4}$/.test((newPin||'').trim())"
            @click="confirmPin"
          >{{ data?.hasPinSet ? 'Change' : 'Set' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.settings-card{
  background: var(--card);
  border: 1px solid var(--line);
  border-radius: 12px;
  padding: 12px;
  box-shadow: var(--shadow);
}

.row{ display:flex; align-items:center; }
.row.between{ justify-content:space-between; gap:10px; }
.row.gap{ gap:10px; }
.head{ justify-content:space-between; margin-bottom:8px; }
.title{ font-weight:800; letter-spacing:.4px; }

.block{ display:grid; gap:8px; margin-top:12px; }
.label{ color: var(--muted); font-size:.9rem; }

.value{ font-weight:800; }
.small{ font-size:.85rem; }
.muted{ color: var(--muted); }
.err{
  color:#ef4444; background:rgba(239,68,68,.08);
  border:1px solid rgba(239,68,68,.35);
  border-radius:8px; padding:8px; font-size:.9rem; margin-bottom:6px;
}
.ok{
  color:#22c55e; background:rgba(34,197,94,.08);
  border:1px solid rgba(34,197,94,.35);
  border-radius:8px; padding:8px; font-size:.9rem; margin-bottom:6px;
}


.bar-wrap{
  display:flex; width:100%;
  height:36px; border-radius:10px;
  overflow:hidden; border:1px solid var(--line);
  background: var(--card-2);
}
.bar{
  height:100%;
  transition: all 180ms ease;
  display:flex; align-items:center; justify-content:center;
}
.bar.active.green{ background: #22c55e; }
.bar.active.red{ background: #ef4444; }
.bar.inactive{
  background: linear-gradient( to bottom, var(--btn-bg), var(--btn-bg-hover) );
  border:none; outline:none; cursor:pointer;
}
.bar.inactive:hover{ filter: brightness(1.08); }
.bar.inactive:disabled{ cursor: default; }

.state-label{
  color:#fff; font-weight:800; letter-spacing:.6px; font-size:.85rem; user-select:none;
}

.bar-wrap.loading .bar.inactive.full{
  width: 100%;
  cursor: default;
}

.edit-row{
  display:grid; gap:10px;
}
.chip{
  font-size:.85rem; font-weight:700; padding:4px 8px; border-radius:9999px; border:1px solid;
}
.chip.ok{ color:#22c55e; border-color:#22c55e; background:transparent; }
.chip.no{ color:#ef4444; border-color:#ef4444; background:transparent; }
</style>
