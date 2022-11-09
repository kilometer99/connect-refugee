import { defineStore } from "pinia";
export const useCategoriesStore = defineStore("categories", {
  state: () => {
    return {
      categories: [],
    };
  },
  persist: true,
  actions: {
    async setAllCategories() {
      try {
        const { data } = await this.$axios.get("/category/all");
        this.categories = data;
      } catch (error) {
        console.error(error);
      }
    }
  },
  getters:{
    getNormalCategories(state){
      return state.categories.filter(res=>res.type=="category")
    },
    getCityCategories(state){
      return state.categories.filter(res=>res.type=="city")
    }
  }
});
