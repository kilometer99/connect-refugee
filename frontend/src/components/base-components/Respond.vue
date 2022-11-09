<template>
  <div
    class="flex items-center mt-4"
    :class="props.right ? 'justify-end' : 'justify-start'"
  >
    <div
      v-if="props.response.message"
      class="flex w-9/12 h-48 rounded-lg border border-gray-200 shadow p-4"
    >
      <span class="flex items-center p-6 border">
        <div>
          <div style="white-space: nowrap">
            Author : {{ props.response.repliedBy }}
          </div>
          <div style="white-space: nowrap">
            Date : {{ props.response.repliedDate }}
          </div>
          <button
            v-if="props.allowResponse"
            type="button"
            class="mt-3 relative rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
            @click="openResponseForResponse"
          >
            Respond
          </button>
          <button
            type="button"
            class="mt-3 relative rounded-md border border-transparent bg-red-600 py-2 px-4 text-sm font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 flex justify-between items-center"
            v-if="user.username == props.response.repliedBy"
            @click="openDeleteDialog"
          >
            <img
              height="25"
              width="25"
              class="cursor-pointer"
              src="@/assets/trash.svg"
              alt="Trash"
            />
            <div>Delete</div>
          </button>
        </div>
      </span>
      <span class="flex items-center p-2 border w-full">
        <span>
          <div
            style="overflow-y: auto"
            class="relative block w-full appearance-none rounded-none rounded-b-md px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm resize-none"
            :class="props.response.message.length > 100 ? 'h-24' : ''"
          >
            {{ props.response.message }}
          </div>
        </span>
      </span>
    </div>
    <div
      v-else
      class="w-9/12 h-48 rounded-lg border border-gray-200 shadow p-4"
    >
      <textarea
        rows="4"
        v-model="respondForm.message"
        class="relative block w-full appearance-none rounded-none rounded-b-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm resize-none"
        @blur="v$.message.$touch"
      ></textarea>
      <button
        type="button"
        class="mt-3 relative rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
        @click="addResponse"
      >
        Respond
      </button>
    </div>
  </div>
</template>
<script setup>
import { reactive, computed } from "vue";
import useValidate from "@vuelidate/core";
import { maxLength } from "@vuelidate/validators";
import { useAuthStore } from "@/stores/auth";

const respondForm = reactive({
  message: "",
});

const authStore = useAuthStore();
const user = computed(() => authStore.user);

const emits = defineEmits([
  "onAddResponse",
  "onOpenResponse",
  "onOpenDeleteDialog",
]);

const props = defineProps({
  right: {
    type: Boolean,
    default: false,
  },
  response: {
    type: Object,
    default: {},
  },
  allowResponse: {
    type: Boolean,
    default: false,
  },
});

const rules = computed(() => {
  return {
    message: {
      maxLength: maxLength(1024),
    },
  };
});

const v$ = useValidate(rules, respondForm).value;

const addResponse = async () => {
  const isValid = await v$.$validate();
  if (isValid) {
    let responseToAdd = { ...respondForm };
    if (props.response.parentReply) {
      responseToAdd["parentReply"] = props.response.parentReply;
    }
    emits("onAddResponse", responseToAdd);
  }
};

const openDeleteDialog = () => {
  emits("onOpenDeleteDialog", props.response.id);
};

const openResponseForResponse = () => {
  emits("onOpenResponse", props.response.id);
};
</script>
