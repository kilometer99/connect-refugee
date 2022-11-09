import { defineStore } from "pinia";
import { useAlert } from "../composables/useAlert";
const { showAlert } = useAlert();

export const usePostsStore = defineStore("posts", {
  state: () => {
    return {
      posts: [],
      currentPost: null,
      currentPostReplies: [],
    };
  },
  actions: {
    async setAllPosts(categoryId) {
      try {
        const { data } = await this.$axios.get(
          `/category/${categoryId}/all-posts`
        );
        this.posts = data;
      } catch (error) {
        showAlert(error.response?.data?.message || error.message, "error");
      }
    },
    async setPostById(postId) {
      try {
        const { data } = await this.$axios.get(`/post/${postId}`);
        this.currentPost = data;
      } catch (error) {
        showAlert(error.response?.data?.message || error.message, "error");
      }
    },
    async setReplyByPostId(postId) {
      try {
        const { data } = await this.$axios.get(`/post/${postId}/all-replies`);
        this.currentPostReplies = data;
      } catch (error) {
        showAlert(error.response?.data?.message || error.message, "error");
      }
    },
    async addPost(categoryId, post) {
      try {
        const { data } = await this.$axios.post(
          `/category/${categoryId}/add-post`,
          post
        );
        this.posts = data;
      } catch (error) {
        showAlert(error.response?.data?.message || error.message, "error");
      }
    },
    async deletePost(postId) {
      try {
        await this.$axios.delete(`/post/delete/${postId}`);
        const allPosts = [...this.posts];
        allPosts.splice(
          allPosts.findIndex((post) => post.id == postId),
          1
        );
        this.posts = allPosts;
      } catch (error) {
        showAlert(error.response?.data?.message || error.message, "error");
      }
    },

    async deleteReply(replyId) {
      try {
        await this.$axios.delete(`/reply/delete/${replyId}`);
        const currentPostReplies = [...this.currentPostReplies];
        currentPostReplies.splice(
          currentPostReplies.findIndex((reply) => reply.id == replyId),
          1
        );
        this.currentPostReplies = currentPostReplies;
      } catch (error) {
        console.log("ERR : ",error)
        showAlert(error.response?.data?.message || error.message, "error");
      }
    },

    async replyToPost(postId, response) {
      try {
        await this.$axios.post(`/post/${postId}/reply`, response);
      } catch (error) {
        showAlert(error.response?.data?.message || error.message, "error");
      }
    },

    async replyToReply(replyId, response) {
      try {
        const { data } = await this.$axios.post(
          `/reply/${replyId}/reply`,
          response
        );
      } catch (error) {
        showAlert(error.response?.data?.message || error.message, "error");
      }
    },
    addReply(reply) {
      this.currentPostReplies.push(reply);
    },
  },
  getters: {
    getAlleplies: (state) => {
      return state.currentPostReplies;
    },
    getSubReplies: (state) => {
      return (replyId) =>
        state.currentPostReplies.filter(
          (res) => res.parentReply != null && res.parentReply.id == replyId
        );
    },
  },
});
