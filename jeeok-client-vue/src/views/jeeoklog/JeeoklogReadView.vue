<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.postTitle }}</h2>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2022-09-11</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ post.postContent }}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { defineProps, onMounted, ref } from "vue";
import axios from "axios";
import router from "@/router";

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

const post = ref({
  postId: 0,
  postTitle: "",
  postContent: "",
});

const moveToEdit = () => {
  router.push({ name: "edit", params: { postId: props.postId } });
};

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data.data;
  });
});
</script>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}
.sub {
  margin-top: 4px;
  font-size: 0.78rem;
  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}
.content {
  font-size: 0.95rem;
  margin-top: 8px;
  color: #5d5d5d;
  white-space: break-spaces;
  line-height: 1.5;
}
</style>
