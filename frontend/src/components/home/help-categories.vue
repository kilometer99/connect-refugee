<template>
  <div
    class="p-6 max-w-sm mx-auto bg-white rounded-xl shadow-lg flex items-center space-x-4"
  >
    <div class="w-full">
      <div class="text-lg border-b border-gray-200">{{props.title}}</div>
      <ul
        class="mt-8 w-full text-sm font-medium text-gray-900 bg-white rounded-lg border border-gray-200 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
      >
        <li
          class="py-2 px-4 w-full border-b border-gray-200 dark:border-gray-600"
          v-for="category in categories"
          @click="gotoCategoryPosts(category.id)"
        >
          {{ category.name }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useCategoriesStore } from "@/stores/categories";
import { useRouter } from 'vue-router'

const props = defineProps({
  title:{
    type:String,
    default:"Find Help"
  },
  categoryType:{
    type:String,
    default:"normal"
  },
})

const categoriesStore = useCategoriesStore();
const categories = computed(() => props.categoryType=='city'?categoriesStore.getCityCategories:categoriesStore.getNormalCategories);
const router=useRouter();

const gotoCategoryPosts = (categoryId) => {
  router.push(`/${categoryId}/posts`);
};


</script>
