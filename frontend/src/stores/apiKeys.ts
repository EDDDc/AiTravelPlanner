import { defineStore } from "pinia";
import http from "../services/http";

interface ApiKeyPayload {
  provider: string;
  alias?: string;
  value: string;
}

export const useApiKeyStore = defineStore("apiKeys", {
  state: () => ({
    lastTestMessage: "",
    loading: false,
  }),
  actions: {
    async saveKeys(payloads: ApiKeyPayload[]) {
      this.loading = true;
      try {
        await Promise.all(
          payloads.map((payload) =>
            http.post("/api/settings/api-keys", payload),
          ),
        );
      } finally {
        this.loading = false;
      }
    },
    async testKey() {
      this.loading = true;
      try {
        const response = await http.post<{ success: boolean; message: string }>(
          "/api/settings/api-keys/test",
          {
            provider: "BAILIAN",
            value: "demo",
          },
        );
        this.lastTestMessage = response.data.message;
      } finally {
        this.loading = false;
      }
    },
  },
});
