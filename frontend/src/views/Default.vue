<template>
  <div class="flex flex-no-wrap">
    <SideBar />
    <div class="container mx-auto h-64 md:w-4/5 w-11/12 px-2">
      <div class="w-full h-full rounded ">
        <router-view />
      </div>
    </div>
  </div>
</template>
<script setup>
import SideBar from "../components/base-components/SideBar.vue";
</script>
