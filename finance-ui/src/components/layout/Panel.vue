<template>
  <aside class="sidebar">
    <div class="brand">
      <v-avatar size="44"
                style="background: linear-gradient(135deg, #7352c3 0%, #1d2ba5 100%); color: #bbd1ed;"
                variant="tonal">
        <v-icon>mdi-credit-card</v-icon>
      </v-avatar>
      <div class="name">
        <div class="title">FinanceApp</div>
        <div class="subtitle">Financial Management</div>
      </div>
    </div>

    <v-list class="nav-list" nav density="comfortable" bg-color="transparent" active-color="primary">

      <v-list-item>
        <SidebarAccountCard
          :balance="balance"
          :owner="owner"
          :iban="iban"
        />
      </v-list-item>
      <v-list-item
        :to="{ name: 'main' }"
        prepend-icon="mdi-home">
        <v-list-item-title class="text-subtitle-1"> Dashboard </v-list-item-title>
      </v-list-item>

      <v-list-item
        :to="{ name: 'transaction history' }"
        prepend-icon="mdi-history">
        <v-list-item-title class="text-subtitle-1"> Transaction History </v-list-item-title>
      </v-list-item>

      <v-list-item
        :to="{ name: 'friends' }"
        prepend-icon="mdi-account-group">
        <v-list-item-title class="text-subtitle-1"> Friends </v-list-item-title>
      </v-list-item>

      <v-list-item
        :to="{ name: 'firms' }"
        prepend-icon="mdi-domain">
        <v-list-item-title class="text-subtitle-1"> Firms </v-list-item-title>
      </v-list-item>

      <v-list-item
        :to="{ name: 'statistics' }"
        prepend-icon="mdi-chart-donut">
        <v-list-item-title class="text-subtitle-1"> Statistics </v-list-item-title>
      </v-list-item>
    </v-list>

    <div class="spacer"></div>
    <div class="footer">
      <span class="small">
        <v-list nav density="comfortable" bg-color="transparent">
        <v-list-item @click="settingsOpen = !settingsOpen" prepend-icon="mdi-cog">
          <v-list-item-title class="text-subtitle-1">
            Settings
          </v-list-item-title>
          <template #append>
            <v-icon v-if="settingsOpen">mdi-chevron-down</v-icon>
          </template>
        </v-list-item>
      </v-list>

        <v-expand-transition>
          <div v-if="settingsOpen" class="settings-wrap">
            <SettingsPanel />
          </div>
        </v-expand-transition>

        <div class="signout">
          <v-list nav density="comfortable" bg-color="transparent">
            <v-list-item
              @click="signOut"
              prepend-icon="mdi-logout"
            >
              <v-list-item-title class="text-subtitle-1">
                {{ isLoading ? 'Signing Out...' : 'Sign Out' }}
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </div>
      </span>
    </div>
  </aside>
</template>

<script setup lang="ts">
import {ref, onMounted, computed} from 'vue';
import { useRouter } from 'vue-router';

import SidebarAccountCard from "@/components/layout/SidebarAccountCard.vue";
import {
  UserControllerService,
  type UserResponseDTO
} from "@/services";
import { useSignOut } from '@/components/SignOut/useSignOut';
import SettingsPanel from '@/components/settings/SettingsPanel.vue'

const router = useRouter();
const { isLoading, signOut } = useSignOut(router);

const settingsOpen = ref(false)

const user = ref<UserResponseDTO | null>(null);
const loading = ref(false);
const error = ref<string | null>(null);

const refreshData = async () => {
  try {
    loading.value = true;
    const [u] = await Promise.all([
      UserControllerService.getCurrentUser(),
    ]);
    user.value = u;
  } catch (e: any) {
    error.value = e?.message ?? 'Failed to load data.';
  } finally {
    loading.value = false;
  }
};
onMounted(refreshData);

const balance = computed(() => user.value?.account?.balance ?? 0);
const owner = computed(() => user.value?.fullName ?? '-');
const iban = computed(() => user.value?.account?.iban ?? '')
</script>

<style scoped>
.sidebar {
  position: sticky; top: 0;
  height: 100%;
  background: var(--panel);
  display: flex;
  flex-direction: column;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 30px 20px;
}

.name .title {
  font-weight: 700;
  letter-spacing: .8px;
  font-size: 1.5rem;
}

.name .title {
  font-weight: 700;
  letter-spacing: .8px;
  font-size: 1.5rem;
}
.name .subtitle {
  font-size: .85rem;
  letter-spacing: .6px;
  color: #9aa3b2;
}

.signout{
  color: #ac3c39;
}

.settings-wrap{
  margin: 8px 6px 4px;
  max-height: 340px;
  overflow: auto;
  border-radius: 12px;
}


.spacer { flex: 1; }
.footer { color: #8b94a6; font-size: .85rem; padding: 8px; }
.small { opacity: .8; }
</style>
