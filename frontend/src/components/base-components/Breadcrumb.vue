<template>
  <nav class="bg-grey-light rounded-md w-full py-4">
    <ol class="list-reset flex">
      <template v-for="(item, index) in items">
        <li>
          <router-link
            :to="item.to"
            :class="
              items.length - 1 == index
                ? 'text-gray-500'
                : 'text-blue-600 hover:text-blue-700'
            "
            >{{ item.title }}</router-link
          >
        </li>
        <li>
          <span
            class="text-gray-500 mx-2"
            v-if="items.length - 1 > index && separator"
            >{{ separator }}</span
          >
          <svg
            v-else-if="items.length - 1 > index"
            class="w-6 h-6 text-gray-400"
            fill="currentColor"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fill-rule="evenodd"
              d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
              clip-rule="evenodd"
            ></path>
          </svg>
        </li>
      </template>
    </ol>
  </nav>
</template>

<script setup>
const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
  separator: {
    type: String || HTMLElement,
    required: false,
  },
});
</script>
