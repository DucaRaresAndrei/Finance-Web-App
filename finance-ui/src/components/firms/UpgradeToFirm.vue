<script setup lang="ts">
import { ref, computed } from 'vue';

type Cat = 'BILLS' | 'ENTERTAINMENT' | 'FOOD' | 'SHOPPING' | 'TRANSPORTATION' | 'USER';
type Colors = {
  bills: string;
  entertainment: string;
  food: string;
  shopping: string;
  transportation: string;
  user: string;
}
const colors = ref<Colors>(
  { bills: '#a855f7',
    entertainment: '#22c55e',
    food: '#f97316',
    shopping: '#0ea5e9',
    transportation: '#efde44',
    user: '#a9b0bb'}
)
const colorByCategory = computed<Record<Cat, string>>(() => ({
  BILLS:           colors.value.bills,
  ENTERTAINMENT:   colors.value.entertainment,
  FOOD:            colors.value.food,
  SHOPPING:        colors.value.shopping,
  TRANSPORTATION:  colors.value.transportation,
  USER:            colors.value.user,
}));

function colorFor(cat: Cat): string {
  return colorByCategory.value[cat];
}

const props = defineProps<{
  accountCategory?: Cat;
  categories: Exclude<Cat, 'USER'>[];
}>();
const emit = defineEmits<{ (e: 'upgraded', cat: Exclude<Cat, 'USER'>): void }>();

const choosing = ref(false);
const chosen = ref<Exclude<Cat, 'USER'> | null>(null);

function startUpgrade() { choosing.value = true; }
function cancel() { choosing.value = false; chosen.value = null; }
function confirm() {
  if (!chosen.value) return;
  emit('upgraded', chosen.value);
  choosing.value = false;
}
</script>

<template>
  <div class="card">
    <h2>Firm Status</h2>

    <template v-if="props.accountCategory === undefined">
      <div v-if="!choosing" class="row-between">
        <div class="text-sub">Checking...</div>
      </div>
    </template>

    <!--    If user-->
    <template v-else-if="props.accountCategory === 'USER'">
      <div v-if="!choosing" class="row-between">
        <div class="text-sub">You are not a Firm</div>
        <button class="btn invert" @click="startUpgrade">Upgrade your Account</button>
      </div>

      <div v-else class="upgrade-bar">
        <v-select
          class="flex-1"
          :items="props.categories"
          v-model="chosen"
          label="Select category"
          variant="outlined"
          density="comfortable"
          hide-details="auto"
          :style="{ '--v-theme-surface': 'var(--card)', '--v-theme-on-surface': 'var(--txt)' }"
        />
        <button class="btn ghost same-h" @click="cancel">Cancel</button>
        <button class="btn same-h" @click="confirm">Confirm</button>
      </div>
    </template>

    <!--    If Firm-->
    <template v-else>
      <div class="text-sub">Your Firm category is:
        <v-chip size="small" class="cat-chip" variant="outlined"
                :style="{ borderColor: colorFor(props.accountCategory), color: colorFor(props.accountCategory) }">
          {{ props.accountCategory }}</v-chip>
      </div>
    </template>
  </div>
</template>

<style scoped>
h2{ font-size: 1.05rem; font-weight: 800; margin: 0 0 12px; }

.row-between{
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.btn.invert { background: var(--white); color: var(--black); border-color: var(--white); }
.btn.invert:hover { background: var(--muted); color: var(--btn-bg-hover); }

.upgrade-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}
.flex-1{ flex: 1 1 auto; }
.flex-1:deep(.v-field){
  border-radius: 10px;
  height: 48px;
}
.flex-1:deep(.v-field__input){
  min-height: 48px;
  padding: 0 12px;
}

.text-sub {
  color: var(--muted);
  font-weight: 600;
}

.cat-chip{
  background: transparent;
  font-weight: 600;
}
</style>
