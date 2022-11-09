import { defineStore } from "pinia";
import { useAlert } from "../composables/useAlert";
const {showAlert} = useAlert()

export const useAuthStore = defineStore("auth", {
  state: () => {
    return {
      user: {
        accessToken: "",
        id: null,
        tokenType: "Bearer",
        username: "",
      },
    };
  },
  persist: true,
  actions: {
    async login(credentials) {
      try {
        const { data } = await this.$axios.post("/auth/login", credentials);
        this.user = data;
      } catch (error) {
        showAlert(error.response?.data?.message || error.message,"error")
      }
    },
    async signup(credentials) {
      try {
        const { data } = await this.$axios.post("/auth/signup", credentials);
        this.user = data;
        this.$router.push("/");
      } catch (error) {
        showAlert(error.response?.data?.message || error.message,"error")
      }
    },
    async logout() {
      try {
        await this.$axios.post("/auth/logout", null);
      } catch (error) {
        console.error(error);
      } finally {
        this.user = {
          accessToken: "",
          id: null,
          tokenType: "Bearer",
          username: "",
        };
      }
    },
  },
  getters: {
    userIsLoggedIn: (state) => !!state.user.accessToken,
  },
});
