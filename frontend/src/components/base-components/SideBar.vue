<template>
  <div
    style="min-height: 716px"
    class="w-64 absolute sm:relative bg-gray-800 shadow md:h-full flex-col justify-between hidden sm:flex"
  >
    <div class="px-4">
      <div class="h-16 w-full flex items-center">
        <router-link
          to="/"
          class="flex justify-between w-full"
          v-slot="{ href }"
        >
          <img
            height="25"
            width="25"
            :href="href"
            src="@/assets/logo_1.jpeg"
            alt="Logo"
          />
          <span class="text-white" style="white-space: nowrap"
            >Swiss - Ukrainian Connect</span
          >
        </router-link>
      </div>
      <ul class="mt-4">
        <template v-if="authenticated">
          <li class="group flex w-full justify-between mb-6 text-white">
            User : {{ user.username }}
          </li>
          <li class="group flex w-full justify-between mb-6">
            <button
              type="button"
              class="relative rounded-md border border-transparent bg-red-400 py-2 px-4 text-sm font-medium text-white hover:bg-red-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
              @click="logout()"
            >
              Logout
            </button>
          </li>
        </template>
        <template v-else>
          <li
            class="flex w-full justify-between text-gray-300 cursor-pointer items-center mb-6"
          >
            <div>
              <label for="username" class="sr-only">Username</label>
              <input
                id="username"
                name="username"
                type="input"
                autocomplete="off"
                required
                class="relative block w-full appearance-none border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
                placeholder="Username"
                v-model="loginForm.username"
                @blur="v$.username.$touch" 
              />
              <span v-if="v$.username.$error" class="text-red-400">
                {{ v$.username.$errors[0].$message }}
              </span>
            </div>
          </li>
          <li
            class="flex w-full justify-between text-gray-400 hover:text-gray-300 cursor-pointer items-center mb-6"
          >
            <div>
              <label for="password" class="sr-only">Password</label>
              <input
                id="password"
                name="password"
                type="password"
                autocomplete="current-password"
                required
                class="relative block w-full appearance-none border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
                placeholder="Password"
                v-model="loginForm.password"
                @blur="v$.password.$touch" 
              />
              <span v-if="v$.password.$error" class="text-red-400">
                {{ v$.password.$errors[0].$message }}
              </span>
            </div>
          </li>
          <li class="group flex w-full justify-between mb-6">
            <button
              type="button"
              class="relative rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
              @click="login()"
            >
              Sign in
            </button>

            <router-link
              to="/register"
              tag="button"
              class="relative rounded-md border border-transparent text-white py-2 px-4 text-sm font-medium bg-red-400 hover:bg-red-500 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
              >Register</router-link
            >
          </li>
        </template>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed } from "vue";
import { useAuthStore } from "@/stores/auth";

import useValidate from "@vuelidate/core";
import { required, minLength, maxLength, helpers } from "@vuelidate/validators";

const loginForm = reactive({
  username: "",
  password: "",
});

const authStore = useAuthStore();
const user = computed(() => authStore.user);
const authenticated = computed(() => authStore.userIsLoggedIn);

const mustNotIncludeSpace = (value) => !value.includes(" ");
const passwordValid = helpers.regex(
  /^([A-Za-z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{7,19}$/
);

const rules = computed(() => {
  return {
    username: {
      required: helpers.withMessage("Username is required", required),
      minLength: minLength(4),
      maxLength: maxLength(10),
      mustNotIncludeSpace: helpers.withMessage(
        "Username cannot have space",
        mustNotIncludeSpace
      ),
    },
    password: {
      required: helpers.withMessage("Password is required", required),
      mustNotIncludeSpace: helpers.withMessage(
        "Password cannot have space",
        mustNotIncludeSpace
      ),
      passwordValid: helpers.withMessage(
        "Must be a valid password containing(8 to 20 characters start with letter)",
        passwordValid
      ),
    },
  };
});

const v$ = useValidate(rules, loginForm).value;

const login = async () => {
  const isValid = await v$.$validate();
  if (isValid) {
    await authStore.login(loginForm);
  }
};

const logout = () => {
  authStore.logout();
};
</script>
