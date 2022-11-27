<template>
  <h1>Jeeok Login</h1>

  <div class="login">
    <el-card>
      <h2>Login</h2>
      <el-form class="login-form" :model="model" :rules="rules" ref="form" @submit.native.prevent="login">
        <el-form-item prop="username">
          <el-input v-model="model.username" placeholder="Username" prefix-icon="fas fa-user"></el-input>
        </el-form-item>
      </el-form>
    </el-card>
  </div>

</template>

<script setup lang="ts">
import { Component, Vue} from "vue-property-decorator";
</script>

<style lang="scss">

</style>
