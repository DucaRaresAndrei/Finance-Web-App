import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import AdminLayout from '@/layouts/AdminLayout.vue'
import FriendsPage from '@/pages/FriendsPage.vue'
import FirmsPage from '@/pages/FirmsPage.vue';
import StatisticsPage from "@/pages/StatisticsPage.vue";
import MainPage from "@/views/MainPage.vue";
import SignUpPage from '@/pages/auth/SignUpPage.vue'
import LoginPage from '@/pages/auth/LoginPage.vue';
import TransactionHistoryPage from "@/pages/TransactionHistoryPage.vue";
import AdminMonitoringPage from "@/pages/AdminMonitoringPage.vue";

const routes: RouteRecordRaw[] = [
  { path: '/sign-up', name: 'signup', component: SignUpPage },
  { path: '/login', name: 'login', component: LoginPage },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: '', name: 'admin', component: AdminMonitoringPage, meta: { requiresAuth: true } },
    ],
  },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', redirect: '/main', meta: { requiresAuth: true } },
      { path: 'transaction-history', name: 'transaction history', component: TransactionHistoryPage, meta: { requiresAuth: true } },
      { path: 'friends', name: 'friends', component: FriendsPage, meta: { requiresAuth: true } },
      { path: 'firms', name: 'firms', component: FirmsPage, meta: { requiresAuth: true } },
      { path: 'statistics', name: 'statistics', component: StatisticsPage, meta: { requiresAuth: true } },
      { path: 'main', name: 'main', component: MainPage, meta: { requiresAuth: true } },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('authToken');
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

  if (requiresAuth && !token) {
    next({ name: 'login' });
  } else if ((to.name === 'login' || to.name === 'signup') && token) {
    next({ name: 'main' });
  } else {
    next();
  }
});

export default router
