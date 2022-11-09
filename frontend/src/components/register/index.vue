<template>
  <div
    class="flex min-h-full items-center justify-center py-12 px-4 sm:px-6 lg:px-8"
  >
    <div class="w-full max-w-md space-y-8">
      <span class="mt-8 space-y-6" action="#" method="POST">
        <input type="hidden" name="remember" value="true" />
        <div class="-space-y-px rounded-md shadow-sm">
          <div>
            <label for="username" class="sr-only">Username</label>
            <input
              id="username"
              name="username"
              type="input"
              autocomplete="off"
              v-model="signupForm.username"
              required
              class="relative block w-full appearance-none rounded-none rounded-t-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
              placeholder="Username"
              @blur.prevent="checkUserName($event.target.value)"
            />

            <span v-if="v$.username.$error" class="text-red-400">
              {{ v$.username.$errors[0].$message }}
            </span>
          </div>
          <div>
            <label for="password" class="sr-only">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              autocomplete="current-password"
              required
              v-model="signupForm.password"
              class="relative block w-full appearance-none rounded-none rounded-b-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm"
              placeholder="Password"
              @blur="v$.password.$touch" 
            />
          </div>
          <span v-if="v$.password.$error" class="text-red-400">
            {{ v$.password.$errors[0].$message }}
          </span>
        </div>

        <div>
          <button
            @click="signup"
            class="group relative flex w-full justify-center rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Register
          </button>
        </div>
      </span>
    </div>
  </div>
</template>

<script setup>
import { reactive, inject, computed, ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import useValidate from "@vuelidate/core";
import { required, minLength, maxLength, helpers } from "@vuelidate/validators";

import { useAlert } from "../../composables/useAlert";
const { showAlert } = useAlert();

const axios = inject("axios");

const mustNotIncludeSpace = (value) => !value.includes(" ");
const passwordValid = helpers.regex(/^[A-Za-z]\w{7,19}$/);
const checkUsername = () => usernameIsAvailable.value === true;

const signupForm = reactive({
  username: "",
  password: "",
});

const usernameIsAvailable = ref(true);

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
      checkUsername: helpers.withMessage(
        "Username not available",
        checkUsername
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

const v$ = useValidate(rules, signupForm).value;

const authStore = useAuthStore();
const signup = async () => {
  const isValid = await v$.$validate();
  if (isValid) {
    await authStore.signup(signupForm);
  }
};

const checkUserName = async (username) => {
  try {
    v$.username.$touch()
    if (username) {
      const { data } = await axios.post("/auth/check-username", username);
      usernameIsAvailable.value = !data;
    }
  } catch (error) {
    showAlert("Internal server error", "error");
  }
};
</script>
