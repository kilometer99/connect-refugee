import { createRouter, createWebHistory } from "vue-router";
import Default from "@/views/Default.vue";
import Home from "@/components/home/index.vue";
import Register from "@/components/register/index.vue"
import Posts from "@/components/posts/index.vue"
import AddPost from "@/components/posts/add.vue"
import PostDetails from "@/components/posts/details.vue"


export default createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: Default,
      children: [
        {
          path: '',
          component: Home,
        },
        {
          path: ':id/posts',
          // component: Posts,
          children:[
            {
              path: '',
              component: Posts,
            },
            {
              path: 'add',
              component: AddPost,
            },
            {
              path: ':postId',
              component: PostDetails,
            }
          ]
        },
        {
          path: 'register',
          component: Register,
        }
      ],
    },
  ],
})
