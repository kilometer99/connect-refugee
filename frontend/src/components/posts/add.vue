<template>
  <span>
    <breadcrumb :items="breadCrumbItems" />
    <div
      class="flex min-h-full items-center justify-center py-12 px-4 sm:px-6 lg:px-8"
    >
      <div class="w-full space-y-8">
        <span class="mt-8 space-y-6">
          <div class="rounded-md shadow-sm">
            <div>
              <span class="flex">
                <label for="title-input">Title</label>
                <input
                  id="title-input"
                  name="title-input"
                  v-model="postForm.title"
                  autocomplete="off"
                  class="ml-2 relative block w-full appearance-none rounded-none rounded-t-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
                  placeholder="Title"
                  @blur="v$.title.$touch"
                />
              </span>
              <span v-if="v$.title.$error" class="ml-8 text-red-400">
                {{ v$.title.$errors[0].$message }}
              </span>
            </div>
            <div class="mt-8">
              <span class="flex">
                <label for="context">Context</label>
                <textarea
                  v-model="postForm.description"
                  rows="10"
                  id="context"
                  name="context"
                  class="ml-2 relative block w-full appearance-none rounded-none rounded-b-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
                  placeholder="Context"
                  @blur="v$.description.$touch"
                ></textarea>
              </span>
              <span v-if="v$.description.$error" class="ml-8 text-red-400">
                {{ v$.description.$errors[0].$message }}
              </span>
            </div>
          </div>

          <div class="flex justify-end">
            <button
              type="button"
              class="group relative rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
              @click="addPost()"
            >
              Post
            </button>
          </div>
        </span>
      </div>
    </div>
  </span>
</template>

<script setup>
import { computed, reactive } from "vue";
import { usePostsStore } from "@/stores/posts";
import { useRouter, useRoute } from "vue-router";
import useValidate from "@vuelidate/core";
import { required, maxLength, helpers } from "@vuelidate/validators";
import Breadcrumb from "../base-components/Breadcrumb.vue";

const postForm = reactive({
  title: "",
  description: "",
});

const rules = computed(() => {
  return {
    title: {
      required: helpers.withMessage("Title is required", required),
    },
    description: {
      maxLength: maxLength(1024),
    },
  };
});

const v$ = useValidate(rules, postForm).value;

const postsStore = usePostsStore();
const router = useRouter();
const route = useRoute();

const breadCrumbItems = [
  {
    title: "Homepage",
    to: "/",
  },
  {
    title: "Posts",
    to: `/${route.params.id}/posts`,
  },

  {
    title: "Add",
    to: `/${route.params.id}/posts/add`,
  },
];

const addPost = async () => {
  const isValid = await v$.$validate();
  if (isValid) {
   await postsStore.addPost(route.params.id, postForm);
    router.push(`/${route.params.id}/posts`);
  }
};
</script>
