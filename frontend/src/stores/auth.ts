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
    try {
      const response = await http.post<AuthResponse>(
        "/api/auth/login",
        payload,
      );
      persistSession(response.data);
    } catch (error) {
      console.warn("登录接口暂未连通，使用 Mock 数据", error);
      persistSession({
        accessToken: "mock-access-token",
        refreshToken: "mock-refresh-token",
        user: {
          id: crypto.randomUUID(),
          email: payload.email,
          displayName: payload.email.split("@")[0] ?? "旅行者",
        },
      });
    }
  }

  async function register(payload: RegisterPayload) {
    try {
      const response = await http.post<AuthResponse>(
        "/api/auth/register",
        payload,
      );
      persistSession(response.data);
    } catch (error) {
      console.warn("注册接口暂未连通，使用 Mock 数据", error);
      persistSession({
        accessToken: "mock-access-token",
        refreshToken: "mock-refresh-token",
        user: {
          id: crypto.randomUUID(),
          email: payload.email,
          displayName: payload.displayName,
        },
      });
    }
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
