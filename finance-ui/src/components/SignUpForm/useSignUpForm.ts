import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { api, publicApi  } from '@/services/http'

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export function useSignUpForm() {
  const router = useRouter();

  const name = ref('');
  const email = ref('');
  const password = ref('');
  const confirmPassword = ref('');
  const isLoading = ref(false);
  const errorMessage = ref('');

  const handleSignUp = async () => {
    errorMessage.value = '';

    if (!name.value || !email.value || !password.value || !confirmPassword.value) {
      errorMessage.value = 'Please fill out all fields';
      return;
    }
    if (password.value.length < 8) {
      errorMessage.value = 'Password must be at least 8 characters long';
      return;
    }
    if (password.value !== confirmPassword.value) {
      errorMessage.value = 'Passwords do not match';
      return;
    }

    try {
      isLoading.value = true;

      await sleep(500);

      await publicApi.post('/auth/register', {
        email: email.value,
        fullName: name.value,
        password: password.value,
      })

      // Redirect to the login page on success
      router.push('/login');

    } catch (error: any) {
      if (axios.isAxiosError(error) && error.response) {
        errorMessage.value = error.response.data.message || 'An unknown error occurred';
      } else {
        errorMessage.value = 'Something went wrong';
      }
      console.error('Sign up failed:', error);
    } finally {
      isLoading.value = false;
    }
  };

  return {
    name,
    email,
    password,
    confirmPassword,
    isLoading,
    errorMessage,
    handleSignUp,
  };
}
