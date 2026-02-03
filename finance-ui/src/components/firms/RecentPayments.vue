<script setup lang="ts">
type TxType = 'INCOME' | 'EXPENSE'
type Item = { id: string; title: string; subtitle: string; amount: number; type: TxType; };

const props = defineProps<{ items: Item[] }>();

const formatRON = (n: number) => n.toLocaleString('ro-RO', {minimumFractionDigits: 2, maximumFractionDigits: 2 })
</script>

<template>
  <div>
    <div class="row">
      <h2>Recent Payments</h2>
    </div>

    <ul class="list">
      <li v-for="it in props.items" :key="it.id" class="row item">
        <div class="stack">
          <div class="title">{{ it.title }}</div>
          <div class="sub">{{ it.subtitle }}</div>
        </div>
        <div class="amount" :class="it.type === 'INCOME' ? 'inc' : 'exp'">
          {{ it.type === 'INCOME' ? '+' : '-' }}{{ formatRON(it.amount) }} RON
        </div>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.row { display: flex; align-items: center; justify-content: space-between; gap: 12px; }
h2{ font-size: 1.05rem; font-weight: 800; margin: 0 0 12px; }
.list { list-style: none; margin: 0; padding: 0; display: grid; gap: 10px; }

.item { background: var(--card-2); border: 1px solid var(--line-2); border-radius: 12px; padding: 12px; transition: 120ms ease; }
.item:hover { border-color: var(--line-2); box-shadow: var(--shadow) inset; background: var(--btn-bg-hover); }

.stack { display: grid; gap: 4px; }
.title { font-weight: 600; }
.sub { color: #97a3b6; font-size: .85rem; }
.amount { font-weight: 800; }
.amount.inc { color: var(--inc); }
.amount.exp { color: var(--exp); }
</style>
