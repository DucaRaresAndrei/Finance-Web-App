<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { AdminMonitoringApi, type MonitoringSummaryDTO, type LabelValueDTO, type EndpointRateDTO, type ServiceUpDTO, type AdminUserRowDTO } from '@/services/admin/adminMonitoringApi'
import AdminUsersList from '@/components/admin/AdminUsersList.vue'

const loading = ref(false)
const error = ref<string | null>(null)

const summary = ref<MonitoringSummaryDTO | null>(null)
const byStatus = ref<LabelValueDTO[]>([])
const topEndpoints = ref<EndpointRateDTO[]>([])
const services = ref<ServiceUpDTO[]>([])
const users = ref<AdminUserRowDTO[]>([])

const usersPage = ref(1)

let pollTimer: number | null = null

function fmt(n: number, digits = 2) {
  if (Number.isNaN(n) || n == null) return '—'
  return new Intl.NumberFormat('en-US', { maximumFractionDigits: digits }).format(n)
}

const upChip = computed(() => summary.value?.financeApiUp ?? false)

async function loadAll() {
  loading.value = true
  error.value = null
  try {
    const [s, st, top, up, us] = await Promise.all([
      AdminMonitoringApi.summary(),
      AdminMonitoringApi.requestsByStatus(),
      AdminMonitoringApi.topEndpoints(10),
      AdminMonitoringApi.servicesUp(),
      AdminMonitoringApi.users(),
    ])
    summary.value = s
    byStatus.value = st
    topEndpoints.value = top
    services.value = up
    users.value = us
  } catch (e: any) {
    error.value = e?.message ?? 'Failed to load admin monitoring data.'
  } finally {
    loading.value = false
  }
}

function openGrafana() {
  window.open('http://localhost:3000', '_blank', 'noopener,noreferrer')
}

onMounted(async () => {
  await loadAll()
  pollTimer = window.setInterval(loadAll, 10000) // refresh la 10s
})

onBeforeUnmount(() => {
  if (pollTimer) window.clearInterval(pollTimer)
})
</script>

<template>
  <div class="admin-page">
    <header class="header">
      <div>
        <h1>Admin Monitoring</h1>
        <p class="muted">Live overview (summarized) of finance-api</p>
      </div>

      <div class="header-actions">
        <v-chip
          size="small"
          class="status-chip"
          variant="outlined"
          :style="{ borderColor: upChip ? 'var(--inc)' : 'var(--exp)', color: upChip ? 'var(--inc)' : 'var(--exp)' }"
        >
          {{ upChip ? 'finance-api UP' : 'finance-api DOWN' }}
        </v-chip>

        <v-btn class="btn" @click="openGrafana">
          Open Grafana Dashboards
          <v-icon end>mdi-open-in-new</v-icon>
        </v-btn>
      </div>
    </header>

    <v-alert
      v-if="error"
      type="error"
      density="comfortable"
      variant="tonal"
      class="mb-3"
    >
      {{ error }}
      <v-btn size="small" class="ml-2" @click="loadAll">Retry</v-btn>
    </v-alert>

    <section class="grid">
      <!-- Left column: Top endpoints -->
      <div class="left-col">
        <div class="card endpoints-card">
          <div class="card-head">
            <h2>Top endpoints (per min)</h2>
          </div>

          <div class="list">
            <div v-for="e in topEndpoints" :key="e.method + e.uri" class="row-item">
              <div class="mono method">{{ e.method }}</div>
              <div class="uri">{{ e.uri }}</div>
              <div class="mono muted">{{ fmt(e.rps, 2) }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Right column: Summary, Requests by status, Services UP -->
      <div class="right-col">
        <!-- Summary -->
        <div class="card">
          <div class="card-head">
            <h2>Summary (last 3 minutes)</h2>
            <div class="muted small">Auto-refresh: 10s</div>
          </div>

          <div class="summary-grid">
            <div class="kpi">
              <div class="k">Requests / min</div>
              <div class="v">{{ fmt(summary?.requestsPerSecond ?? 0, 1) }}</div>
            </div>
            <div class="kpi">
              <div class="k">4xx / min</div>
              <div class="v" style="color: var(--exp);">{{ fmt(summary?.errors4xxPerSecond ?? 0, 1) }}</div>
            </div>
            <div class="kpi">
              <div class="k">5xx / min</div>
              <div class="v" style="color: var(--exp);">{{ fmt(summary?.errors5xxPerSecond ?? 0, 1) }}</div>
            </div>
            <div class="kpi">
              <div class="k">p95 latency (ms)</div>
              <div class="v">{{ fmt(summary?.p95LatencyMs ?? 0, 0) }}</div>
            </div>
          </div>
        </div>

        <!-- Requests by status -->
        <div class="card">
          <div class="card-head">
            <h2>Requests by status (per min)</h2>
          </div>

          <div class="list">
            <div v-for="it in byStatus" :key="it.label" class="row-item status-row">
              <div class="mono">{{ it.label }}</div>
              <div class="mono muted">{{ fmt(it.value, 2) }}</div>
            </div>
          </div>
        </div>

        <!-- Services up -->
        <div class="card">
          <div class="card-head">
            <h2>Services UP</h2>
          </div>

          <div class="svc">
            <div v-for="s in services" :key="s.job + s.instance" class="svc-item">
              <div class="mono muted">{{ s.job }}</div>
              <div class="mono small">{{ s.instance }}</div>
              <v-chip
                size="x-small"
                variant="outlined"
                :style="{ borderColor: s.up ? 'var(--inc)' : 'var(--exp)', color: s.up ? 'var(--inc)' : 'var(--exp)' }"
              >
                {{ s.up ? 'UP' : 'DOWN' }}
              </v-chip>
            </div>
          </div>
        </div>
      </div>

      <!-- Users list -->
      <div class="card users-card">
        <AdminUsersList :users="users" v-model:page="usersPage" />
      </div>
    </section>

    <div v-if="loading" class="muted" style="margin-top: 10px;">Loading…</div>

    <div class="note muted">
      Note: These are summarized metrics. For full dashboards and detailed panels, open Grafana.
    </div>
  </div>
</template>

<style scoped>
.admin-page { padding-top: 18px; }

.header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 14px;
  margin-bottom: 18px;
}
h1 { font-size: 1.6rem; font-weight: 800; margin: 0 0 6px; }
.muted { color: var(--muted); margin: 0; }
.small { font-size: .85rem; }
.mono { font-family: ui-monospace; }
.btn { background: var(--btn-bg); border: 1px solid var(--line); color: var(--txt); }
.btn:hover { background: var(--btn-bg-hover); border-color: var(--line-2); }

.header-actions { display: flex; align-items: center; gap: 10px; }
.status-chip { background: transparent; font-weight: 700; }

.grid {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 18px;
  align-items: start;
}

.left-col {
  display: flex;
  flex-direction: column;
}

.right-col {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.endpoints-card {
  height: 100%;
}

.card {
  background: var(--card);
  border: 1px solid var(--line);
  border-radius: 14px;
  padding: 16px;
  overflow: hidden;
  box-shadow: var(--shadow);
}
.card:hover { background: var(--card-2); border-color: var(--line-2); }

.card-head {
  display:flex;
  align-items:flex-start;
  justify-content: space-between;
  gap: 10px;
  margin-bottom: 12px;
}
h2 { font-size: 1.05rem; font-weight: 800; margin: 0; }

.summary-grid{
  display:grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}
.kpi{
  background: var(--card-2);
  border: 1px solid var(--line-2);
  border-radius: 12px;
  padding: 12px;
}
.k { color: var(--muted); font-size: .85rem; }
.v { font-weight: 900; font-size: 1.2rem; margin-top: 4px; }

.list { display: grid; gap: 10px; }
.row-item{
  display:grid;
  grid-template-columns: 90px 1fr 70px;
  gap: 10px;
  align-items: center;
  background: var(--card-2);
  border: 1px solid var(--line-2);
  border-radius: 12px;
  padding: 10px 12px;
}
.status-row {
  grid-template-columns: 1fr 90px;
}
.method{ font-weight: 800; }
.uri{ overflow:hidden; text-overflow: ellipsis; white-space: nowrap; }

.svc { display:grid; gap: 10px; }
.svc-item{
  display:grid;
  grid-template-columns: 1fr 1.2fr auto;
  gap: 10px;
  align-items: center;
  background: var(--card-2);
  border: 1px solid var(--line-2);
  border-radius: 12px;
  padding: 10px 12px;
}

.users-card { grid-column: 1 / -1; }

.note { margin-top: 14px; }
</style>
