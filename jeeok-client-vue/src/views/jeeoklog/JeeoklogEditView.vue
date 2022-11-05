<template>
  <div>
    <el-input v-model="post.postTitle" placeholder="제목을 입력하세요." />
  </div>

  <div class="mt-2">
    <el-input
      v-model="post.postContent"
      type="textarea"
      rows="15"
      placeholder="내용을 입력하세요."
    />
  </div>

  <div class="mt-2">
    <el-button type="warning" @click="edit()">수정완료</el-button>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { ref } from "vue";
import axios from "axios";

const router = useRouter();
const post = ref({
  postId: 0,
  postTitle: "",
  postContent: "",
});

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

axios.get(`/api/posts/${props.postId}`).then((response) => {
  post.value = response.data.data;
});

const edit = () => {
  axios.put(`/api/posts/${props.postId}`, post.value).then(() => {
    router.replace({ name: "home" });
  });
};
</script>

<style scoped lang="scss"></style>
