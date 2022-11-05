<template>
  <h1>Jeeoklog Home</h1>
  <ul>
    <li v-for="post in posts" :key="post.postId">
      <div class="title">
        <router-link :to="{ name: 'read', params: { postId: post.postId } }">{{
          post.postTitle
        }}</router-link>
      </div>
      <div class="content">
        {{ post.postContent }}
      </div>
      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2022-09-11</div>
      </div>
    </li>
  </ul>
</template>

<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const posts = ref([]);

axios.get("/api/posts").then((response) => {
  response.data.data.forEach(function (row: any) {
    posts.value.push(row);
  });
});
</script>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;
  li {
    margin-bottom: 1.58rem;
    .title {
      a {
        font-size: 1.2rem;
        color: #383838;
        text-decoration: none;
      }
      &:hover {
        text-decoration: underline;
      }
    }
    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      color: #5d5d5d;
    }
    &:last-child {
      margin-bottom: 0;
    }
    .sub {
      margin-top: 4px;
      font-size: 0.78rem;
      .regDate {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}
</style>
