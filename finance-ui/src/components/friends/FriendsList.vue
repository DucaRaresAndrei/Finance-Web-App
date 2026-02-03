<template>
  <div>
    <div class="row header">
      <h2>Friends ({{ friends.length }})</h2>
    </div>

    <ul class="list">
      <li v-for="f in pageItems" :key="f.iban" class="item" @mouseenter="hover=f.iban" @mouseleave="hover=null">
        <div class="left">
          <div class="avatar">{{ initials(f.fullName) }}</div>
          <div class="stack">
            <div class="name">
              {{ f.fullName }}
            </div>
            <div class="sub">{{ f.email }}</div>
          </div>
        </div>

        <div class="right">
          <v-btn class="icon-btn" variant="outlined" size="small" title="Send" @click="toggleSend(f.iban)">
            <v-icon>mdi-send</v-icon>
          </v-btn>

          <!--          <v-btn class="icon-btn" variant="outlined" size="small" title="Settings">-->
          <!--            <v-icon>mdi-dots-horizontal</v-icon>-->
          <!--          </v-btn>-->
        </div>

        <div v-if="sendFor === f.iban" class="inline-send" @click.stop>
          <input
            class="input amount"
            type="number"
            min="0"
            step="1"
            placeholder="Amount"
            v-model.number="amountLocal"
            @keydown.enter.prevent="sendAndClose(f)"
          />
          <input class="input" type="text" placeholder="Message (optional)" v-model.trim="messageLocal" @keydown.enter.prevent />
          <button class="btn ghost" @click="closeSend">Cancel</button>
          <button class="btn" :disabled="!amountLocal || Number(amountLocal) <= 0" @click.stop="sendAndClose(f)">Send</button>
        </div>
      </li>
    </ul>

    <!--  Pager-->
    <div class="pager">
      <v-btn icon variant="text" :disabled="page===1" @click="page--">
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
      <div class="px-2 small muted">{{ page }} / {{ totalPages }}</div>
      <v-btn icon variant="text" :disabled="page===totalPages" @click="page++">
        <v-icon>mdi-chevron-right</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'

type Friend = { fullName: string; email: string; iban: string }
const props = withDefaults(defineProps<{ friends?: Friend[] }>(), {
  friends: () => []
})
const emit = defineEmits<{
  (e:'send-to-friend', payload:{ iban:string; amount:number; message?:string }): void
  (e:'validation-error', msg: string): void
}>()
const sendFor = ref<string | null>(null)
const hover = ref<string | null>(null)

const amountLocal = ref<number | null>(null)
const messageLocal = ref<string>('')

// 6 per page
const page = ref(1);
const pageSize = 6;
const totalPages = computed(() => Math.max(1, Math.ceil(props.friends.length / pageSize)));

watch(() => props.friends, () => { page.value = 1; });

const pageItems = computed(() => {
  const start = (page.value - 1) * pageSize;
  return props.friends.slice(start, start + pageSize);
});

const initials = (name: string) =>
  name.split(' ')
    .filter(Boolean)
    .map(w => w[0]?.toUpperCase())
    .join('')

const toggleSend = (iban: string) => {
  sendFor.value = sendFor.value === iban ? null : iban
  amountLocal.value = null
  messageLocal.value = ''
}
const closeSend = () => { sendFor.value = null; amountLocal.value = null; messageLocal.value = '' }

function sendAndClose(f: Friend) {
  const a = Number(amountLocal.value)
  if (!a || a <= 0) return
  emit('send-to-friend', {
    iban: f.iban,
    amount: a,
    message: (messageLocal.value || undefined),
  })
  closeSend()
}
</script>

<style scoped>
.row {
  display: flex; align-items: center; justify-content: space-between;
}
.header { margin-bottom: 10px; }
h2 { font-size: 1.05rem; font-weight: 800; margin: 0; }

.list { list-style: none; margin: 0; padding: 0; display: grid; gap: 12px; }
.item {
  background: var(--card-2); border: 1px solid var(--line-2); border-radius: 14px;
  padding: 12px; display: grid; grid-template-columns: 1fr auto; gap: 10px; position: relative;
  transition: 120ms ease;
}
.item:hover { border-color: var(--line-2); box-shadow: var(--shadow) inset; background: var(--btn-bg-hover); }

.left { display: flex; align-items: center; gap: 12px; }
.avatar {
  width: 40px; height: 40px; border-radius: 9999px;
  background: #2563eb; color: #fff; display: grid; place-items: center; font-weight: 800;
}
.stack { display: grid; gap: 4px }
.name { font-weight: 700; display: flex; align-items: center; gap: 8px; }
.sub { color: #8e9bb0; font-size: .85rem; }

.right { display: flex; align-items: center; gap: 8px; }
.icon-btn {
  background: transparent; border: 1px solid var(--card); color: #b7c1d5;
  width: 36px; height: 36px; border-radius: 10px; display: grid; place-items: center; cursor: pointer;
  transition: 120ms ease;
}
.icon-btn:hover { background: var(--btn-bg-hover); color: #fff; border: 2px solid var(--card-2); }

.inline-send {
  grid-column: 1 / -1;
  display: flex; gap: 10px; align-items: center;
  background: var(--card); border: 1px dashed var(--line); padding: 10px; border-radius: 10px;
}
.amount { max-width: 160px; }
.small{ font-size:.85rem; }
.muted{ color: var(--muted); }
.pager{ display:flex; align-items:center; justify-content:flex-end; gap:4px; margin-top:10px; }
</style>
