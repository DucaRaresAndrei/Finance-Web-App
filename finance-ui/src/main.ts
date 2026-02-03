import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './styles/friendBase.css'
import { vuetify } from "@/plugins/vuetify.ts";
import { OpenAPI } from '@/services/core/OpenAPI.ts';

OpenAPI.BASE = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';
OpenAPI.TOKEN = async () => {
  const t = localStorage.getItem('authToken');
  return t ?? '';
};

const app = createApp(App)


app.use(router)
app.use(vuetify)
app.mount('#app')
