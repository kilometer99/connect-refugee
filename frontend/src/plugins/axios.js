import axios from "axios";
export function axiosPlugin({ app, store }) {
  const axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
  });

  axiosInstance.interceptors.request.use((config) => {
    const auth = store.user
      ? store
      : JSON.parse(localStorage.getItem("auth"));
    if (auth?.user && auth.user.accessToken) {
      config.headers["Authorization"] = `Bearer ${auth.user.accessToken}`;
    }
    return config;
  });

  axiosInstance.defaults.headers.get["X-HTTP-Method-Override"] = "GET";
  axiosInstance.defaults.headers.post["Content-Type"] = "application/json";

  app.provide("axios", axiosInstance);
  store.$axios = axiosInstance;
}
