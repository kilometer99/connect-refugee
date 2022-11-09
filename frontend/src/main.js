import { createApp, markRaw } from "vue";
import "./style.css";
import App from "./App.vue";
import routes from "./routes";
import { createPinia } from "pinia";
import { axiosPlugin } from "./plugins/axios";
import piniaPersistState from 'pinia-plugin-persistedstate'

const pinia = createPinia();

pinia.use(({ store }) => {
  store.$router = markRaw(routes);
});
pinia.use(axiosPlugin);
pinia.use(piniaPersistState)

createApp(App).use(routes).use(pinia).mount("#app");
