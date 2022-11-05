import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/Login.vue";
import JeeoklogHomeView from "../views/jeeoklog/JeeoklogHomeView.vue";
import JeeoklogWriteView from "../views/jeeoklog/JeeoklogWriteView.vue";
import JeeoklogReadView from "../views/jeeoklog/JeeoklogReadView.vue";
import JeeoklogEditView from "../views/jeeoklog/JeeoklogEditView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/jeeoklog",
      name: "jeeoklogHome",
      component: JeeoklogHomeView,
    },
    {
      path: "/jeeoklog/write",
      name: "jeeoklogWrite",
      component: JeeoklogWriteView,
    },
    {
      path: "/jeeoklog/read/:postId",
      name: "jeeoklogRead",
      component: JeeoklogReadView,
      props: true,
    },
    {
      path: "/jeeoklog/edit/:postId",
      name: "jeeoklogEdit",
      component: JeeoklogEditView,
      props: true,
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
    },
  ],
});

export default router;
