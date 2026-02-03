import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { api, publicApi  } from '@/services/http'

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

function parseJwt(token: string): any {
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
    return JSON.parse(decodeURIComponent(escape(atob(base64))))
  } catch {
    return null
  }
}

export function useLoginForm() {
  const router = useRouter();

  const email = ref('');
  const password = ref('');
  const isLoading = ref(false);
  const errorMessage = ref('');

  const handleLogin = async () => {
    errorMessage.value = '';

    if (!email.value || !password.value) {
      errorMessage.value = 'Please enter both email and password';
      return;
    }

    try {
      isLoading.value = true;
      await sleep(500);

      const response = await publicApi.post('/auth/login', {
        username: email.value,
        password: password.value,
      })

      const accessToken = response.data.access_token
      const refreshToken = response.data.refresh_token

      if (!accessToken) throw new Error('Login response did not include a token')

      localStorage.setItem('authToken', accessToken)
      if (refreshToken) localStorage.setItem('refreshToken', refreshToken)

      // Admin redirect
      const payload = parseJwt(accessToken)
      const roles: string[] =
        payload?.realm_access?.roles ??
        payload?.roles ??
        []

      if (roles.includes('ADMIN')) router.push('/admin')
      else router.push('/main')

    } catch (error: any) {
      if (axios.isAxiosError(error) && error.response) {
        errorMessage.value = error.response.data.message || 'Invalid email or password';
      } else {
        errorMessage.value = 'Something went wrong. Please check your connection.';
      }
      console.error('Login failed:', error);
    } finally {
      isLoading.value = false;
    }
  };

  return {
    email,
    password,
    isLoading,
    errorMessage,
    handleLogin,
  };
}
