<script setup lang="ts">
import { computed, ref, watch } from 'vue'

type Category = 'BILLS' | 'ENTERTAINMENT' | 'FOOD' | 'SHOPPING' | 'TRANSPORTATION' | 'USER' | string

type UserRow = {
  username: string
  email: string
  category: Category
}

const props = withDefaults(defineProps<{ users?: UserRow[]; page?: number }>(), {
  users: () => [],
  page: 1,
})

const emit = defineEmits<{ (e: 'update:page', v: number): void }>()

const colors = {
  BILLS: '#a855f7',
  ENTERTAINMENT: '#22c55e',
  FOOD: '#f97316',
  SHOPPING: '#0ea5e9',
  TRANSPORTATION: '#efde44',
  USER: '#94a3b8',
} as const

function colorFor(cat: Category) {
  return (colors as any)[cat] ?? '#94a3b8'
}

const page = computed({
  get: () => props.page ?? 1,
  set: (v: number) => emit('update:page', v),
})

const pageSize = 8
const totalPages = computed(() => Math.max(1, Math.ceil(props.users.length / pageSize)))

watch([() => props.users.length, totalPages], () => {
  if (page.value > totalPages.value) page.value = totalPages.value
  if (page.value < 1) page.value = 1
})

const pageItems = computed(() => {
  const start = (page.value - 1) * pageSize
  return props.users.slice(start, start + pageSize)
})

const initials = (s: string) =>
  (s || '')
    .split(/[.\s_@-]+/)
    .filter(Boolean)
    .slice(0, 2)
    .map(w => w[0]?.toUpperCase())
    .join('')
</script>

<template>
  <div>
    <div class="row header">
      <h2>Users ({{ props.users.length }})</h2>
    </div>

    <div class="list">
      <div v-for="u in pageItems" :key="u.username + u.email" class="item">
        <div class="left">
          <div class="avatar" :style="{ background: colorFor(u.category) }">
            {{ initials(u.username) }}
          </div>

          <div class="stack">
            <div class="name">{{ u.username }}</div>
            <div class="sub">{{ u.email }}</div>
          </div>
        </div>

        <div class="right">
          <v-chip
            size="small"
            class="cat-chip"
            variant="outlined"
            :style="{ borderColor: colorFor(u.category), color: colorFor(u.category) }"
          >
            {{ u.category }}
          </v-chip>
        </div>
      </div>
    </div>

    <div class="pager">
      <v-btn icon variant="text" :disabled="page === 1" @click="page--">
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
      <div class="px-2 small muted">{{ page }} / {{ totalPages }}</div>
      <v-btn icon variant="text" :disabled="page === totalPages" @click="page++">
        <v-icon>mdi-chevron-right</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<style scoped>
.row { display: flex; align-items: center; justify-content: space-between; }
.header { margin-bottom: 10px; }
h2 { font-size: 1.05rem; font-weight: 800; margin: 0; }

.list { display: grid; gap: 12px; }

.item{
  background: var(--card-2);
  border: 1px solid var(--line-2);
  border-radius: 14px;
  padding: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: 120ms ease;
}
.item:hover { border-color: var(--line-2); box-shadow: var(--shadow) inset; background: var(--btn-bg-hover); }

.left { display: flex; align-items: center; gap: 12px; }
.avatar{
  width: 40px; height: 40px; border-radius: 9999px;
  display: grid; place-items: center;
  font-weight: 800;
  color: #fff;
}
.stack { display: grid; gap: 4px; }
.name { font-weight: 700; }
.sub { color: var(--muted); font-size: .85rem; }

.right { display: flex; align-items: center; gap: 8px; }
.cat-chip{ background: transparent; font-weight: 600; }

.small{ font-size:.85rem; }
.muted{ color: var(--muted); }
.pager{ display:flex; align-items:center; justify-content:flex-end; gap:4px; margin-top:10px; }
</style>
