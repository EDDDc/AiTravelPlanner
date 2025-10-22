import axios, { AxiosHeaders, type AxiosRequestHeaders } from "axios";

const http = axios.create({
  baseURL: import.meta.env.VITE_APP_API_BASE_URL ?? "http://localhost:8080",
  timeout: 15000,
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem("atp_access_token");
  if (token) {
    if (!config.headers) {
      config.headers = new AxiosHeaders();
    }

    if (config.headers instanceof AxiosHeaders) {
      config.headers.set("Authorization", `Bearer ${token}`);
    } else {
      config.headers = Object.assign({}, config.headers, {
        Authorization: `Bearer ${token}`,
      }) as AxiosRequestHeaders;
    }
  }
  return config;
});

export default http;
