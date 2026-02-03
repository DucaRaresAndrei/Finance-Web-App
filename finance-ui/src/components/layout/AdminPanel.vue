<template>
  <aside class="sidebar">
    <div class="brand">
      <v-avatar
        size="44"
        style="background: linear-gradient(135deg, #7352c3 0%, #1d2ba5 100%); color: #bbd1ed;"
        variant="tonal"
      >
        <v-icon>mdi-shield-crown</v-icon>
      </v-avatar>

      <div class="name">
        <div class="title">FinanceApp</div>
        <div class="subtitle">Admin Monitoring</div>
      </div>
    </div>

    <div class="spacer"></div>

    <div class="footer">
      <v-list nav density="comfortable" bg-color="transparent">
        <v-list-item @click="signOut" prepend-icon="mdi-logout" class="signout">
          <v-list-item-title class="text-subtitle-1">
            {{ isLoading ? 'Signing Out...' : 'Sign Out' }}
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useSignOut } from '@/components/SignOut/useSignOut';

const router = useRouter()
const { isLoading, signOut } = useSignOut(router)
</script>

<style scoped>
.sidebar {
  position: sticky;
  top: 0;
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
  letter-spacing: 0.8px;
  font-size: 1.5rem;
}
.name .subtitle {
  font-size: 0.85rem;
  letter-spacing: 0.6px;
  color: #9aa3b2;
}

.spacer { flex: 1; }
.footer { padding: 12px; color: #8b94a6; }

.signout { color: #ac3c39; }
</style>
