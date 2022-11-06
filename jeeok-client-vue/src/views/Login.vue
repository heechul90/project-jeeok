<template>
  <h2>Jeeok Login</h2>
<!--  <v-form ref="form" lazy-validation>
    <v-text-field
      :rules="[v => /.+@.+\..+/.test(v) || 'E-mail must be valid', v => !!v || '이메일은 필수 값입니다']"
      label="이메일"
      prepend-icon="mdi-account-circle"></v-text-field>
  </v-form>-->
<!--  <div id="app">
    <el-form @submit.prevent="loginWithPassword">
      <label>
        Email
        <el-input type="text" v-model="email"/>
      </label>
      <label>
        Password
        <el-input type="password" v-model="password"/>
      </label>
      <el-button type="submit">로그인</el-button>
    </el-form>

    <p>or</p>

    <el-button @click.prevent="loginWithSSO">
      Log in with Google
    </el-button>
  </div>-->
  <div class="col-md-12">
    <div class="card card-container">
      <img
          id="profile-img"
          src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
          class="profile-img-card"
      />
      <form name="form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">Username</label>
          <input
              v-model="user.username"
              v-validate="'required'"
              type="text"
              class="form-control"
              name="username"
          />
          <div
              v-if="errors.has('username')"
              class="alert alert-danger"
              role="alert"
          >
            Username is required!
          </div>
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
              v-model="user.password"
              v-validate="'required'"
              type="password"
              class="form-control"
              name="password"
          />
          <div
              v-if="errors.has('password')"
              class="alert alert-danger"
              role="alert"
          >
            Password is required!
          </div>
        </div>
        <div class="form-group">
          <button class="btn btn-primary btn-block" :disabled="loading">
            <span
                v-show="loading"
                class="spinner-border spinner-border-sm"
            ></span>
            <span>Login</span>
          </button>
        </div>
        <div class="form-group">
          <div v-if="message" class="alert alert-danger" role="alert">
            {{ message }}
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.login {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-button {
  width: 100%;
  margin-top: 40px;
}
.login-form {
  width: 290px;
}
.forgot-password {
  margin-top: 10px;
}
</style>
<style lang="scss">
/*$teal: rgb(0, 124, 137);
.el-button--primary {
  background: $teal;
  border-color: $teal;

  &:hover,
  &.active,
  &:focus {
    background: lighten($teal, 7);
    border-color: lighten($teal, 7);
  }
}
.login .el-input__inner:hover {
  border-color: $teal;
}
.login .el-input__prefix {
  background: rgb(238, 237, 234);
  left: 0;
  height: calc(100% - 2px);
  left: 1px;
  top: 1px;
  border-radius: 3px;
  .el-input__icon {
    width: 30px;
  }
}
.login .el-input input {
  padding-left: 35px;
}
.login .el-card {
  padding-top: 0;
  padding-bottom: 30px;
}
h2 {
  font-family: "Open Sans";
  letter-spacing: 1px;
  font-family: Roboto, sans-serif;
  padding-bottom: 20px;
}
a {
  color: $teal;
  text-decoration: none;
  &:hover,
  &:active,
  &:focus {
    color: lighten($teal, 7);
  }
}
.login .el-card {
  width: 340px;
  display: flex;
  justify-content: center;
}*/
</style>
