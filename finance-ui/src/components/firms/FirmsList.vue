<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue';

type Category = 'BILLS' | 'ENTERTAINMENT' | 'FOOD' | 'SHOPPING' | 'TRANSPORTATION';
type Colors = {
  bills: string;
  entertainment: string;
  food: string;
  shopping: string;
  transportation: string;
}
const colors = ref<Colors>(
  { bills: '#a855f7',
    entertainment: '#22c55e',
    food: '#f97316',
    shopping: '#0ea5e9',
    transportation: '#efde44'}
)
const colorByCategory = computed<Record<Category, string>>(() => ({
  BILLS:           colors.value.bills,
  ENTERTAINMENT:   colors.value.entertainment,
  FOOD:            colors.value.food,
  SHOPPING:        colors.value.shopping,
  TRANSPORTATION:  colors.value.transportation,
}));

function colorFor(cat: Category): string {
  return colorByCategory.value[cat];
}

type Firm = {
  fullName: string;
  email: string;
  iban: string;
  category: Category;
};

const props = withDefaults(defineProps<{ firms?: Firm[]; page?: number }>(), {
  firms: () => [],
  page: 1,
})
const emit = defineEmits<{
  (e:'update:page', v:number): void
  (e:'pay-to-firm', payload:{ iban: string; amount: number; message?: string; pin?: string }): void
  (e:'validation-error', msg: string): void
}>();

// 6 per page
const page = computed({
  get: () => props.page ?? 1,
  set: (v:number) => emit('update:page', v),
})
const pageSize = 6;
const totalPages = computed(() => Math.max(1, Math.ceil(props.firms.length / pageSize)));

watch([() => props.firms.length, totalPages], () => {
  if (page.value > totalPages.value) page.value = totalPages.value
  if (page.value < 1) page.value = 1
})

const pageItems = computed(() => {
  const start = (page.value - 1) * pageSize;
  return props.firms.slice(start, start + pageSize);
});

const sendFor = ref<string | null>(null)
const amount = ref<number | null>(null);
const message = ref<string>('');
const pin = ref<string>('');

const triedPin = ref(false);
const pinInputRef = ref<HTMLInputElement|null>(null);

const needsPin = computed(() => Number(amount.value) > 100);
const pinFilled = computed(() => (pin.value ?? '').trim().length > 0);
const amountValid = computed(() => !!amount.value && Number(amount.value) > 0);
const pinValid = computed(() => !needsPin.value || (/^\d{4}$/.test((pin.value ?? '').trim())))
const pinError = computed(() => needsPin.value && triedPin.value && !pinFilled.value);

const toggleSend = (iban: string) => {
  sendFor.value = sendFor.value === iban ? null : iban
  amount.value = null
  message.value = ''
  pin.value = ''
  triedPin.value = false
}
const closeSend = () => { sendFor.value = null; amount.value = null; message.value = ''; pin.value = ''; triedPin.value = false }

watch([amount, pin], ([a, p]) => {
  if (Number(a) <= 100 || (/^\d{4}$/.test((p ?? '').trim()))) triedPin.value = false;
});

function invalidPinKick() {
  triedPin.value = true
  emit('validation-error', 'PIN must be exactly 4 digits.')
  nextTick(() => pinInputRef.value?.focus())
}

function attemptSend(f: Firm) {
  const a = Number(amount.value);
  if (!a || a <= 0) return;

  if (needsPin.value && !(/^\d{4}$/.test((pin.value || '').trim()))) {
    invalidPinKick()
    return
  }

  emit('pay-to-firm', {
    iban: f.iban,
    amount: a,
    message: (message.value || undefined),
    pin: (a > 100 ? (pin.value || undefined) : undefined),
  });
  closeSend();
}

function onEnterAmount(f: Firm) { attemptSend(f); }
function onEnterMessage() {}
function onEnterPin(f: Firm) {
  if (!amountValid.value) return;
  if (!pinValid.value){ invalidPinKick(); return }
  attemptSend(f);
}

const initials = (name: string) =>
  name.split(' ')
    .filter(Boolean)
    .map(w => w[0]?.toUpperCase())
    .join('')
</script>

<template>
  <div>
    <div class="row header">
      <h2>Firms ({{ props.firms.length }})</h2>
    </div>

    <div class="list">
      <div
        v-for="f in pageItems"
        :key="f.iban"
        class="item"
      >
        <div class="left">
          <div class="avatar firm" :style="{ background: colorFor(f.category) }">
            {{ initials(f.fullName) }}
          </div>

          <div class="stack">
            <div class="name">{{ f.fullName }}</div>
            <div class="sub">{{ f.email }}</div>
          </div>
        </div>

        <div class="right">
          <v-chip size="small" class="cat-chip" variant="outlined"
                  :style="{ borderColor: colorFor(f.category), color: colorFor(f.category) }">{{ f.category }}</v-chip>

          <v-btn class="icon-btn" variant="outlined" size="small" title="Send" @click="toggleSend(f.iban)">
            <v-icon>mdi-send</v-icon>
          </v-btn>
        </div>

        <div v-if="sendFor === f.iban" class="inline-send" @click.stop>
          <input
            class="input amount"
            type="number"
            min="0"
            step="1"
            placeholder="Amount"
            v-model="amount"
            @keydown.enter.prevent="onEnterAmount(f)"
          />

          <input class="input" type="text" placeholder="Message (optional)" v-model.trim="message" @keydown.enter.prevent="onEnterMessage()" />

          <input
            v-if="Number(amount) > 100"
            ref="pinInputRef"
            v-model="pin"
            type="text"
            placeholder="Card PIN"
            class="input"
            maxlength="4"
            inputmode="numeric"
            :class="{ error: pinError }"
            @keydown.enter.prevent="onEnterPin(f)"
          />

          <button class="btn ghost" @click.stop="closeSend">Cancel</button>
          <button class="btn" :disabled="!amount || Number(amount) <= 0" @click.stop="attemptSend(f)">Send</button>
        </div>
      </div>
    </div>

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

<style scoped>
.row{ display:flex; align-items:center; justify-content:space-between; }
.header{ margin-bottom:10px; }
h2{ font-size:1.05rem; font-weight:800; margin:0; }

.list{ display:grid; gap:12px; }
.item{
  background: var(--card-2);
  border: 1px solid var(--line-2);
  border-radius: 14px;
  padding: 12px;
  display:grid; grid-template-columns: 1fr auto; gap: 10px;
  position:relative;
  transition:120ms ease;
}
.item:hover{ border-color: var(--line-2); box-shadow: var(--shadow) inset; background: var(--btn-bg-hover); }

.left{ display:flex; align-items:center; gap:12px; }
.avatar{
  width:40px; height:40px; border-radius:9999px;
  display:grid; place-items:center; font-weight:800; color:#fff;
}

.stack{ display:grid; gap:4px; }
.name{ font-weight:700; }
.sub{ color:var(--muted); font-size:.85rem; }

.right{ display:flex; align-items:center; gap:8px; }
.icon-btn {
  background: transparent; border: 1px solid var(--card); color: #b7c1d5;
  width: 36px; height: 36px; border-radius: 10px; display: grid; place-items: center; cursor: pointer;
  transition: 120ms ease;
}
.icon-btn:hover { background: var(--btn-bg-hover); color: #fff; border: 2px solid var(--card-2); cursor:pointer; }

.cat-chip{
  background: transparent;
  font-weight: 600;
}

.inline-send{
  grid-column: 1 / -1;
  display:flex; gap:10px; align-items:center;
  margin-top:8px; padding:10px;
  border:1px dashed var(--line);
  border-radius:10px;
  background: var(--card);
}
.amount{ max-width: 160px; }
.row.end{ justify-content:flex-end; }
.small{ font-size:.85rem; }
.muted{ color: var(--muted); }
.pager{ display:flex; align-items:center; justify-content:flex-end; gap:4px; margin-top:10px; }

.input.error {
  border-color: #ef4444;
  box-shadow: 0 0 0 2px rgba(239, 68, 68, .25) inset;
}
</style>
