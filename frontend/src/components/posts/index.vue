<template>
  <div class="border-b border-gray-200 shadow">
    <div class="flex justify-around">
      <breadcrumb :items="breadCrumbItems" />
      <button
        class="mt-3 mr-3 relative rounded-md border border-transparent text-white py-2 px-4 text-sm font-medium bg-red-400 hover:bg-red-500 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
        @click="gotoAddNewPost()"
        v-if="authenticated"
      >
        Post
      </button>
    </div>

    <table class="mt-8 w-full">
      <thead class="bg-gray-50 text-center">
        <tr>
          <th class="px-6 py-2 text-xs text-gray-500">Number</th>
          <th class="px-6 py-2 text-xs text-gray-500">Title</th>
          <th class="px-6 py-2 text-xs text-gray-500">Author</th>
          <th class="px-6 py-2 text-xs text-gray-500">Date</th>
          <th></th>
        </tr>
      </thead>
      <tbody class="bg-white">
        <tr
          class="text-center"
          v-for="(post, index) in posts"
          @click="gotoPostDetails(post.id)"
        >
          <td class="px-6 py-4 text-sm text-gray-500">{{ index + 1 }}</td>
          <td class="px-6 py-4">
            <div class="text-sm text-gray-900">{{ post.title }}</div>
          </td>
          <td class="px-6 py-4">
            <div class="text-sm text-gray-500">{{ post.createdBy }}</div>
          </td>
          <td class="px-6 py-4 text-sm text-gray-500">
            {{ post.createdDate }}
          </td>
          <td v-if="user.username == post.createdBy">
            <img
              @click.stop="openDeleteDialog(post.id)"
              height="25"
              width="25"
              class="cursor-pointer"
              src="@/assets/trash.svg"
              alt="Trash"
            />
          </td>
        </tr>
      </tbody>
    </table>
    <modal
      :input="showModal"
      title="Delete Post?"
      body="Do you want to delete post?"
      @onClick:Ok="deletePost()"
      @onClick:Cancel="closeDialog()"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { usePostsStore } from "@/stores/posts";
import { useRouter, useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import Modal from "../base-components/Modal.vue";
import Breadcrumb from "../base-components/Breadcrumb.vue";

const postsStore = usePostsStore();
const authStore = useAuthStore();
const posts = computed(() => postsStore.posts);
const authenticated = computed(() => authStore.userIsLoggedIn);
const showModal = ref(false);
const selectedPostToDelete = ref(0);
const user = computed(() => authStore.user);

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
];

const gotoAddNewPost = () => {
  router.push(`/${route.params.id}/posts/add`);
};

const openDeleteDialog = (id) => {
  selectedPostToDelete.value = id;
  showModal.value = true;
};

const closeDialog = () => {
  showModal.value = false;
};

const deletePost = async() => {
 await postsStore.deletePost(selectedPostToDelete.value);
  await setPostsByCategory();
  closeDialog();
};

const gotoPostDetails = (postId) => {
  router.push(`/${route.params.id}/posts/${postId}`);
};

const setPostsByCategory =async () => {
  await postsStore.setAllPosts(route.params.id);
};

onMounted(() => {
  setPostsByCategory();
});
</script>
