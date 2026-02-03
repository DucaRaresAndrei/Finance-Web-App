<template>
  <div class="app-shell">
    <AdminPanel class="panel" />
    <main class="content">
      <div class="inner">
        <RouterView />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import AdminPanel from '@/components/layout/AdminPanel.vue'
</script>

<style scoped>
.app-shell {
  display: grid;
  grid-template-columns: 280px 1fr;
  height: 100vh;
}
.panel { border-right: 1px solid var(--line); }
.content { padding: 24px 32px; overflow: auto; }
.inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
