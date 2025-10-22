import { defineStore } from "pinia";
import { computed, ref } from "vue";
import http from "../services/http";

interface LoginPayload {
  email: string;
  password: string;
}

interface RegisterPayload extends LoginPayload {
  displayName: string;
}

interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: {
    id: string;
    email: string;
    displayName: string;
    avatarUrl?: string | null;
  };
}

export const useAuthStore = defineStore("auth", () => {
  const user = ref<AuthResponse["user"] | null>(null);
  const accessToken = ref<string | null>(
    localStorage.getItem("atp_access_token"),
  );
  const refreshToken = ref<string | null>(
    localStorage.getItem("atp_refresh_token"),
  );

  function persistSession(payload: AuthResponse) {
    user.value = payload.user;
    accessToken.value = payload.accessToken;
    refreshToken.value = payload.refreshToken;
    localStorage.setItem("atp_access_token", payload.accessToken);
    localStorage.setItem("atp_refresh_token", payload.refreshToken);
  }

  async function login(payload: LoginPayload) {
    // TODO: 替换为真实后端接口
    const response = await http.post<AuthResponse>("/api/auth/login", payload);
    persistSession(response.data);
  }

  async function register(payload: RegisterPayload) {
    const response = await http.post<AuthResponse>(
      "/api/auth/register",
      payload,
    );
    persistSession(response.data);
  }

  function logout() {
    user.value = null;
    accessToken.value = null;
    refreshToken.value = null;
    localStorage.removeItem("atp_access_token");
    localStorage.removeItem("atp_refresh_token");
  }

  return {
    user,
    accessToken,
    refreshToken,
    isAuthenticated: computed(() => !!accessToken.value),
    login,
    register,
    logout,
  };
});
