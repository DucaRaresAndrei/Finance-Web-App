import { ref } from 'vue';
import axios from 'axios';
import type { Router } from 'vue-router';
import { api, publicApi  } from '@/services/http'

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export function useSignOut(router: Router) {
  const isLoading = ref(false);

  const signOut = async () => {
    try {
      isLoading.value = true;
      await sleep(300);

      const refreshToken = localStorage.getItem('refreshToken')

      if (refreshToken) {
        await publicApi.post('/auth/logout', { refresh_token: refreshToken })
      }
    } catch (error) {
      console.error("Error calling backend logout:", error);
    } finally {
      isLoading.value = false;
      localStorage.removeItem('authToken');
      localStorage.removeItem('refreshToken')

      // Redirect to the login page of the application
      router.push({ name: 'login' });
    }
  };

  return {
    isLoading,
    signOut
  };
}
