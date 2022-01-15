<template>
  <loading-heart-2 v-if="login.loading"></loading-heart-2>
  <transition name="fade">
    <div v-show="!login.loading" class="bg">
      <img id="bg" :src="login.bg" alt="bg"/>
    </div>
  </transition>
  <template v-if="!login.loading">
    <div class="header">
      <div class="left">
        <img class="logo" src="../assets/logo.png" alt="Image Cloud" />
        <h1 class="title">Image Cloud</h1>
      </div>
      <div class="right">
        <el-button v-if="!login.signup" type="primary" size="large" round @click="login.signup=true">Sign up</el-button>
        <el-button v-if="login.signup" class="btn-login" plain size="large" round @click="login.signup=false">Sign in</el-button>
      </div>
    </div>
    <div class="signup-form">

      <div class="logo-box">
        <div class="logo">
          <img src="../assets/logo.png" alt="Image Cloud" />
          <h1>Image Cloud</h1>
        </div>
        <div class="catchphrase">Welcome to Image Cloud!</div>
      </div>

      <form v-if="!login.signup">
        <div class="input-field-group">
          <div class="input-field">
            <input type="text" placeholder="E-mail address / user id" autocapitalize="off" v-model="login.username" @blur="checkUsername" />
          </div>
          <div class="input-field">
            <input type="password" placeholder="password" autocapitalize="off" v-model="login.password" @blur="checkPassword" />
          </div>
        </div>
        <div class="error" v-if="login.error" v-text="login.error"></div>
        <div class="submit">
          <el-button type="primary" size="large" round @click="signin">Sign in</el-button>
        </div>
        <div class="forget">
          <a href="/">Forgot password?</a>
        </div>
      </form>

      <form v-if="login.signup">
        <div class="input-field-group">
          <div class="input-field">
            <input type="text" placeholder="E-mail address" autocapitalize="off" v-model="login.username" @blur="checkUsername"/>
          </div>
          <div class="input-field">
            <input type="password" placeholder="Password" autocapitalize="off" v-model="login.password" @blur="checkPassword"/>
          </div>
        </div>
        <div class="input-field-code">
          <div class="input-field">
            <input type="text" placeholder="Verification code" autocapitalize="off" v-model="login.code"/>
          </div>
          <el-button size="large" :disabled="login.disabled" @click="sendEmailCode">Verify E-mail</el-button>
        </div>
        <div class="error" v-if="login.error" v-text="login.error"></div>
        <div class="submit">
          <el-button type="primary" size="large" round @click="signup">Sign up</el-button>
        </div>
      </form>

      <div class="copy-right">
        Copyright © 2022 <a href="imagecloud.shiyq.top">imagecloud.shiyq.top</a>
        <br />
        <a href="http://www.beian.miit.gov.cn/" target="_blank" style="color: #409effd0">豫ICP备2021006771号</a>
      </div>

    </div>
  </template>
  <div class="loading-login" v-show="login.loadingLogin">
    <img src="../assets/icon/loading_login.svg" alt="loading">
  </div>
</template>

<script>
import { onMounted, reactive, watch } from "vue"
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import server from '@/util/request'
import md5 from 'js-md5'
import { ElMessage } from 'element-plus'
import LoadingHeart2 from "@/components/LoadingHeart2"

export default {
  name: "Login",
  components: {
    "loading-heart-2": LoadingHeart2,
  },
  setup() {
    onMounted(() => {
      // 检查是否已持有访问令牌
      let accessToken = store.state.accessToken
      if (!accessToken) {
        accessToken = localStorage.getItem("accessToken")
        if (!accessToken) {
          // 加载bing的每日壁纸
          server.get('/bing/HPImageArchive.aspx?format=js&idx=0&n=1').then(res=>{
            login.bg = 'https://cn.bing.com' + res.images[0].url
            // login.bg = 'https://cn.bing.com/th?id=OHR.WinterBison_ZH-CN0120689382_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp'
            document.getElementById("bg").onload = function () {
              login.loading = false
            }
          })
          return
        }
        store.commit('setAccessToken', accessToken)
      }
      // 存在accessToken，默认拥有访问权限
      // 请求用户信息和设置，初始化全局用户信息、全局用户设置
      initGlobalInfo()
    });

    const login = reactive({
      loading: true,  // 加载页面前的loading
      bg: "", // 背景图片
      signup: false,  // 是注册还是登录
      username: '',
      password: '',
      confirmPassword: '',  // 注册时的确认密码
      code: '', // 注册表单的邮箱验证码
      error: '',  // 提示的错误信息
      disabled : true,  // 用户名（邮箱）检查通过前，发送邮件按钮不可点击
      loadingLogin: false,  // 登录和注册结果到来前的loading
    });

    // 监听登录/注册的切换
    watch(()=>login.signup, ()=>{
      // 每次切换登录和注册表单时，置空
      login.username = ''
      login.password = ''
      login.error = ''
    })

    // 检查用户名格式
    function checkUsername() {
      if (login.username.length === 0) {
        login.error = 'Please enter your user ID or email address.'
      } else if (login.username.indexOf('@') >= 0  && login.username.search(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/) < 0) {
        login.error = 'Please confirm your email address format.'
      } else if (!login.signup && login.username.indexOf('@') < 0 && login.username.search(/^[0-9]{6}$/) < 0 && login.username.length <= 6) {
        login.error = 'Please confirm your user id or email address format.'
      } else {
        login.error = ''
        login.disabled = false
        return true
      }
      login.disabled = true
      return false
    }
    // 检查密码格式
    function checkPassword() {
      if (login.password.length === 0) {
        login.error = 'Please enter your password.'
      } else if (login.password.length < 6 || login.password.length > 30) {
        login.error = 'Passwords must be between 6 to 30 characters.'
      } else {
        login.error = ''
        return true
      }
      return false
    }
    // 检查验证码格式
    function checkCode() {
      if (login.signup && login.code.length !== 6 && login.code.search(/^[0-9]{6}$/) < 0) {
        login.error = 'Please confirm your E-mail verification code format!'
      } else {
        login.error = ''
        return true
      }
      return false
    }

    const store = useStore()
    const router = useRouter()

    // 登录
    function signin() {
      // 点击登录后，显示loading图标
      login.loadingLogin = true
      // 验证表单，不通过直接结束
      if (!checkUsername() || !checkPassword) {
        login.loadingLogin = false
        return false
      }
      // 对密码进行md5加密
      const data = {
        username: login.username,
        password: md5(login.password+'#'+login.password),
      }
      // 请求登录，获取访问令牌
      server.post('/user/sign-in', data).then(res=>{
        // 保存访问令牌到全局、本地
        store.commit('setAccessToken', res.data.accessToken)
        localStorage.setItem("accessToken", res.data.accessToken);
        // 获取访问令牌后，请求用户信息和设置，初始化全局用户信息、全局用户设置
        initGlobalInfo()
      }).catch(error=>{
        login.loadingLogin = false
        login.error = error
      })
    }

    // 注册
    function signup() {
      // 点击登录后，显示loading图标
      login.loadingLogin = true
      // 验证表单，不通过直接结束
      if (!checkUsername() || !checkPassword || !checkCode) {
        login.loadingLogin = false
        return false
      }
      // 对密码进行md5加密
      const data = {
        username: login.username,
        password: md5(login.password+'#'+login.password),
        code: login.code
      }
      server.post('/user/signup', data).then(res=>{
        // 注册验证通过后，直接进行登录，保存访问令牌到全局、本地
        store.commit('setAccessToken', res.data.accessToken)
        localStorage.setItem("accessToken", res.data.accessToken);
        // 请求用户信息和设置，初始化全局用户信息、全局用户设置
        initGlobalInfo()
      }).catch(error=>{
        login.loadingLogin = false
        login.error = error
      })
    }

    // 获取访问令牌后，请求用户信息和设置，初始化全局用户信息、全局用户设置
    function initGlobalInfo() {
      // 同时请求用户信息和用户设置
      server.all([server.get('/user-info/getUserInfo'), server.get('/setting/getSetting')])
        .then(server.spread((info, setting)=>{
          // 初始化全局用户信息、全局用户设置
          store.commit('updateUserInfo', info.data)
          store.commit('initSettings', setting.data)
          // 初始化完成，移除loading图标
          login.loadingLogin = false
          // 初始化完成后，再跳转到首页
          router.push({path: '/img/upload'})
        }))
        .catch(error=>{
          login.loadingLogin = false
          login.error = error
        })
    }

    // 发送邮箱验证码
    function sendEmailCode() {
      // 点击发送后显示loading图标
      login.loadingLogin = true
      // 检查数据库，用户名（邮箱）是否已存在
      server.post('/user/checkSameUsername', {username: login.username})
      .then(()=>{
        login.error = ''
        server.post('/user/sendEmailVerificationCode/', {username: login.username}).then(()=>{
          // 发送成功后
          login.loadingLogin = false
          ElMessage.success('E-mail verification code sent successfully.')
        }).catch(error=>{
          login.loadingLogin = false
          login.error = error
        })
      })
      .catch(error=>{
        login.loadingLogin = false
        login.error = error
      })
      
    }

    return { login, checkUsername, checkPassword, signin, signup, sendEmailCode };
  },
};
</script>

<style scoped>
.bg {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1;
}
.bg img {
  width: 100vw;
  height: 100vh;
  object-fit: cover;
}
.fade-enter-active, .fade-leave-active {
  transition: all .3s linear;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
.header {
  box-sizing: border-box;
  width: 100vw;
  height: 80px;
  padding: 20px;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 2;
}
.header .left {
  display: -webkit-flex;
  display: flex;
  align-items: center;
}
.header .left .logo {
  width: 40px;
  height: 40px;
  margin-right: 15px;
}
.header .left .title {
  color: #fff;
  font-weight: 600;
  font-size: 1.1rem;
  margin: 0;
}
.header .el-button {
  padding: 9px 24px;
  font-size: 1rem;
}
.header .btn-login {
  border-color: #ffffff;
  color: #ffffff;
  background-color: #ffffff00;
}
.header .btn-login:hover {
  border-color: #3a8ee6;
  color: #ffffff;
  background-color: #409eff;
}
.signup-form {
  width: 363px;
  padding: 40px 0;
  background-color: #ffffffeb;
  border-radius: 4px;
  position: fixed;
  top: 50vh;
  left: 50vw;
  transform: translate(-50%, -50%);
  z-index: 2;
}
.signup-form .logo-box {
  margin-bottom: 30px;
}
.signup-form .logo-box .logo {
  display: -webkit-flex;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 5px;
}
.signup-form .logo-box .logo img {
  width: 56px;
  height: 56px;
  margin-right: 15px;
}
.signup-form .logo-box .logo h1 {
  color: #409eff;
  font-weight: 600;
  font-size: 1.6rem;
  margin: 0;
}
.signup-form .logo-box .catchphrase {
  display: -webkit-flex;
  display: flex;
  justify-content: center;
  color: #757c80;
  font-size: 0.75rem;
  font-weight: 700;
}
.signup-form form {
  margin-bottom: 20px;
}
.signup-form .input-field-group>.input-field:first-child {
  border-radius: 4px 4px 0 0;
  border-bottom: none;
}
.signup-form .input-field-group>.input-field:last-child {
  border-radius: 0 0 4px 4px;
}
.signup-form .input-field-group>.input-field {
  margin: 0 auto;
  border-radius: 0;
}
.signup-form .input-field {
  position: relative;
  margin: 0 auto;
  width: 300px;
  padding: 0;
  border: 1px solid #dfebf2;
  background-color: #fff;
  border-radius: 4px;
}
.signup-form .input-field input {
  width: 290px;
  height: 40px;
  margin: 0;
  padding: 0 0 0 10px;
  display: block;
  font-size: 14px;
  color: #000;
  line-height: normal;
  outline: 0;
  border: none;
  border-radius: 4px;
  overflow: visible;
}
.signup-form .input-field input:placeholder-shown {
  color: #bbb;
}
::-webkit-input-placeholder { /* WebKit browsers */ 
color: #bbb; 
}
.signup-form .input-field-code {
  position: relative;
  margin: 8px auto 0;
  width: 300px;
  padding: 0;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
}
.signup-form .input-field-code .input-field {
  width: 170px;
  margin: 0;
}
.signup-form .input-field-code input {
  width: 160px;
  height: 38px;
}
.error {
  box-sizing: border-box;
  width: 300px;
  margin: 8px auto 0;
  padding: 13px 10px;
  background-color: #0000004d;
  border-radius: 4px;
  color: #ffffff;
  line-height: 1.5;
  text-align: left;
}
.signup-form .submit {
  width: 300px;
  margin: 8px auto 0;
}
.signup-form .submit .el-button {
  box-sizing: border-box;
  width: 100%;
  padding: 9px 24px;
  font-size: 1rem;
}
.signup-form .forget {
  width: 300px;
  margin: 10px auto 0;
  overflow: hidden;
  text-align: right;
}
.signup-form .forget a {
  color: #5c5c5c;
  font-size: 12px;
  text-decoration: none;
}
.signup-form .forget a:hover {
  text-decoration: underline;
}
.copy-right {
  width: 300px;
  padding-top: 14px;
  margin: 0 auto;
  border-top: 1px solid #00000014;
  font-style: normal;
  font-weight: 400;
  font-size: 12px;
  line-height: 16px;
  text-align: center;
  color: #00000070;
}
.loading-login {
  box-sizing: border-box;
  width: 80px;
  height: 80px;
  padding: 14px;
  border-radius: 8px;
  background-color: #ffffffad;
  position: fixed;
  top: 50vh;
  left: 50vw;
  transform: translate(-50%, -50%);
  z-index: 2;
}
</style>
