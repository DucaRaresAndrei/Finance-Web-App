<template>
  <v-card
    class="stat-card"
    :class="[`stat-${type}`]"
    variant="elevated"
    elevation="8"
    rounded="xl"
  >
    <div class="stat-header">
      <v-avatar size="36" :color="chipColor" variant="tonal">
        <v-icon :icon="iconName" />
      </v-avatar>
      <div class="stat-title">{{ title }}</div>
    </div>

    <div class="stat-value">
      <span :class="type">{{ pretty }}</span>
    </div>

    <v-divider class="my-2" />

    <div class="stat-footer">
      <v-chip :color="chipColor" size="small" label variant="tonal" class="font-weight-medium">
        {{ type === 'pos' ? 'Positive' : 'Negative' }}
      </v-chip>
    </div>
  </v-card>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { VCard, VAvatar, VIcon, VDivider, VChip } from 'vuetify/components';

interface Props {
  title: string;
  value: number;
  type?: 'pos' | 'neg';
  locale?: string;          // ex: 'ro-RO'
  currencySymbol?: string;  // ex: 'RON' sau '€' sau '$'
}
const props = withDefaults(defineProps<Props>(), {
  type: 'pos',
  locale: 'en-US',
  currencySymbol: '$',
});

const pretty = computed(() => {
  const sign = props.value >= 0 ? '+' : '-';
  const abs = Math.abs(props.value).toLocaleString(props.locale, {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
  return `${sign}${props.currencySymbol}${abs}`;
});

const chipColor = computed(() => (props.type === 'pos' ? 'success' : 'error'));
const iconName = computed(() => (props.type === 'pos' ? 'mdi-trending-up' : 'mdi-trending-down'));
</script>

<style scoped>
:root {
  --v-shadow-10: 0 6px 16px rgba(0, 0, 0, 0.12),
  0 3px 6px rgba(0, 0, 0, 0.08);
}
.stat-card {
  padding: 16px 18px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  border: 1px solid rgba(148, 163, 184, 0.15);
  transition: transform 0.15s ease, box-shadow 0.15s ease, border-color 0.2s ease;
  background-image: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.02),
    rgba(255, 255, 255, 0)
  );
}
.stat-card:hover {
  transform: translateY(-1px);
  box-shadow: var(--v-shadow-10);
  border-color: rgba(148, 163, 184, 0.3);
}

.stat-header {
  display: flex;
  align-items: center;
  gap: 10px;
}
.stat-title {
  font-size: 0.95rem;
  color: #94a3b8; /* slate-400 */
  letter-spacing: 0.2px;
}

.stat-value {
  display: flex;
  align-items: baseline;
  gap: 6px;
}
.stat-value > span {
  font-size: 1.6rem;
  font-weight: 800;
  line-height: 1.1;
}
.stat-value > span.pos { color: #16a34a; } /* success-600 */
.stat-value > span.neg { color: #dc2626; } /* error-600 */

.stat-footer {
  display: flex;
  justify-content: flex-start;
}
</style>
