<template>
  <el-header>
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/jeeoklog">Jeeoklog</el-menu-item>
      <el-menu-item index="/login">Login</el-menu-item>
    </el-menu>
  </el-header>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.header {
  padding: 0;
  height: 60px;
}
</style>
