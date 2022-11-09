<template>
  <span>
    <breadcrumb :items="breadCrumbItems" />
    <div class="grid grid-cols-2">
      <div>
        <help-categories />
      </div>

      <div>
        <help-categories category-type="city" title="Meetup" />
      </div>
    </div>
  </span>
</template>

<script setup>
import HelpCategories from "./help-categories.vue";
import Breadcrumb from "../base-components/Breadcrumb.vue";
import { onMounted } from "vue";
import { useCategoriesStore } from "@/stores/categories";

const breadCrumbItems = [
  {
    title: "Homepage",
    to: "/",
  }
];

const categoriesStore = useCategoriesStore();

onMounted(() => {
  categoriesStore.setAllCategories();
});
</script>
