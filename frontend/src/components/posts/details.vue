<template>
  <span>
    <breadcrumb :items="breadCrumbItems" />
    <div>
      <div class="p-4 border w-full">
        <div v-if="currentPost && currentPost?.id" class="text-lg">
          {{ currentPost.title }}
        </div>
      </div>

      <span class="flex text-lg h-72" v-if="currentPost && currentPost.id">
        <div class="p-6 border items-center flex">
          <div>
            <div style="white-space: nowrap">
              Author : {{ currentPost.createdBy }}
            </div>
            <div style="white-space: nowrap">
              Date : {{ currentPost.createdDate }}
            </div>
            <div v-if="authenticated">
              <button
                @click="manipulateEmptyResponse"
                type="button"
                class="mt-4 relative rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
              >
                Respond
              </button>
            </div>
          </div>
        </div>
        <div class="p-6 border overflow-y-auto">
          {{ currentPost.description }}
        </div>
      </span>
      <div>
        <template v-for="response in parentRepliesOnly">
          <respond
            right
            :response="response"
            @on-add-response="addNewResponceToPost"
            @onOpenResponse="manipulateEmptyResponseForResponse"
            @onOpenDeleteDialog="openResponseDeleteDialog"
            :allow-response="authenticated"
          >
          </respond>
          <respond
            v-for="(subResponse, subIndex) in childRepliesOnly(response.id)"
            :key="subIndex"
            :response="subResponse"
            @on-add-response="addNewResponceToReply"
            @on-open-delete-dialog="openResponseDeleteDialog"
          />
        </template>
      </div>
    </div>
    <modal
      :input="showModal"
      title="Delete Response?"
      body="Do you want to delete response?"
      @onClick:Ok="deleteResponse()"
      @onClick:Cancel="closeDialog()"
    />
  </span>
</template>

<script setup>
import { computed, onBeforeMount, ref } from "vue";
import { usePostsStore } from "@/stores/posts";
import { useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import Respond from "../base-components/Respond.vue";
import Breadcrumb from "../base-components/Breadcrumb.vue";
import Modal from "../base-components/Modal.vue";

const postsStore = usePostsStore();
const authStore = useAuthStore();
const authenticated = computed(() => authStore.userIsLoggedIn);
const currentPost = computed(() => postsStore.currentPost);
const currentPostReplies = computed(() => postsStore.currentPostReplies);
const route = useRoute();
const responseToDelete = ref("");
const showModal = ref(false);
const parentRepliesOnly = computed(() =>
  currentPostReplies.value.filter((res) => res.parentReply == null)
);

onBeforeMount(() => {
  postsStore.setPostById(route.params.postId);
  postsStore.setReplyByPostId(route.params.postId);
});

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
    title: "Details",
    to: `/${route.params.id}/posts/${route.params.postId}`,
  },
];

const childRepliesOnly = (replyId) => {
  let previousReplies = replyId ? postsStore.getSubReplies(replyId) : [];
  let currentReplies = replyId
    ? postsStore.getAlleplies.filter((res) => res.parentReply == replyId)
    : [];
  return [...previousReplies, ...currentReplies];
};

const manipulateEmptyResponse = () => {
  postsStore.addReply({
    id: null,
    message: "",
    post: currentPost.value,
    parentReply: null,
    repliedBy: "",
    repliedDate: "",
  });
};

const manipulateEmptyResponseForResponse = (responseId) => {
  postsStore.addReply({
    id: null,
    message: "",
    post: currentPost.value,
    parentReply: responseId,
    repliedBy: "",
    repliedDate: "",
  });
};

const addNewResponceToPost = async (response) => {
  const postId = route.params.postId;
  await postsStore.replyToPost(postId, response);
  postsStore.setReplyByPostId(route.params.postId);
};

const addNewResponceToReply = async (response) => {
  const { parentReply, message } = response;
  await postsStore.replyToReply(parentReply, { message });
  postsStore.setReplyByPostId(route.params.postId);
};

const openResponseDeleteDialog = (id) => {
  responseToDelete.value = id;
  showModal.value = true;
};

const deleteResponse = async () => {
  await postsStore.deleteReply(responseToDelete.value);
  showModal.value = false;
};

const closeDialog = () => {
  showModal.value = false;
};
</script>
