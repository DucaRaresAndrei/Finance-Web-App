<template>
  <div>
    <div class="row">
      <h2>Recent Activity</h2>
    </div>

    <ul class="list">
      <li v-for="item in items" :key="item.id" class="row item" @click="toggle(item.id)"
          :title="isRevealed(item.id) ? 'Click to hide IBAN' : 'Click to reveal IBAN'">
        <template v-if="!isRevealed(item.id)">
          <div class="stack">
            <div class="title">{{ item.title }}</div>
            <div class="sub">{{ item.subtitle }}</div>
          </div>
          <div class="amount" :class="item.type === 'INCOME' ? 'inc' : 'exp'">
            {{ item.type === 'INCOME' ? '+' : '-' }}{{ formatRON(item.amount) }} RON
          </div>
        </template>

        <template v-else>
          <div class="iban-view">
            <div class="label">Friend IBAN</div>
            <code class="iban">{{ item.iban || '—' }}</code>
          </div>
        </template>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

type TxType = 'INCOME' | 'EXPENSE'
type Item = { id: string; title: string; subtitle: string; amount: number; type: TxType; iban: string }
const props = defineProps<{ items: Item[] }>()

const formatRON = (n: number) => n.toLocaleString('ro-RO', {minimumFractionDigits: 2, maximumFractionDigits: 2 })

const revealed = ref<Set<string>>(new Set())
const isRevealed = (id: string) => revealed.value.has(id)

const toggle = (id: string) => {
  const set = new Set(revealed.value)
  if (set.has(id))
    set.delete(id);
  else
    set.add(id)
  revealed.value = set
}
</script>

<style scoped>
.row { display: flex; align-items: center; justify-content: space-between; gap: 12px; }
h2 { font-size: 1.05rem; font-weight: 800; margin: 0 0 12px; }
.list { list-style: none; margin: 0; padding: 0; display: grid; gap: 10px; }

.item { background: var(--card-2); border: 1px solid var(--line-2); border-radius: 12px; padding: 12px; transition: 120ms ease; }
.item:hover { border-color: var(--line-2); box-shadow: var(--shadow) inset; background: var(--btn-bg-hover); }

.stack { display: grid; gap: 4px; }
.title { font-weight: 600; }
.sub { color: #97a3b6; font-size: .85rem; }
.amount { font-weight: 800; }
.amount.inc { color: var(--inc); }
.amount.exp { color: var(--exp); }

.iban-view { display: grid; gap: 6px; width: 100%; }
.label { color: var(--muted); font-size: .85rem; }
.iban {
  background: var(--card);
  border: 1px dashed var(--line);
  padding: 8px 10px;
  border-radius: 8px;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", monospace;
  letter-spacing: 0.3px;
}
</style>
