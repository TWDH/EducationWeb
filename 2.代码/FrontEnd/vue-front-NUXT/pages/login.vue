<template>
  <div class="main">
    <div class="title">
      <a class="active" href="/login">登录</a>
      <span>·</span>
      <a href="/register">注册</a>
    </div>

    <div class="sign-up-container">
      <el-form ref="userForm" :model="user">

        <el-form-item class="input-prepend restyle" prop="mobile" :rules="[{ required: true, message: '请输入手机号码', trigger: 'blur' },{validator: checkPhone, trigger: 'blur'}]">
          <div >
            <el-input type="text" placeholder="手机号" v-model="user.mobile"/>
            <i class="iconfont icon-phone" />
          </div>
        </el-form-item>

        <el-form-item class="input-prepend" prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <div>
            <el-input type="password" placeholder="密码" v-model="user.password"/>
            <i class="iconfont icon-password"/>
          </div>
        </el-form-item>

        <div class="btn">
          <input type="button" class="sign-in-button" value="登录" @click="submitLogin()">
        </div>
      </el-form>
      <!-- 更多登录方式 -->
      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li><a id="weixin" class="weixin" target="_blank" href="http://47.94.174.79:8150/api/ucenter/wx/login"><i class="iconfont icon-weixin"/></a></li>
          <li><a id="qq" class="qq" target="_blank" href="#"><i class="iconfont icon-qq"/></a></li>
        </ul>
      </div>
    </div>

  </div>
</template>

<script>
  import '~/assets/css/sign.css'
  import '~/assets/css/iconfont.css'
  import cookie from 'js-cookie'
  import loginApi from '@/api/login'

  export default {
    layout: 'sign',

    data () {
      return {
        //封装登录手机号和密码
        user:{
          mobile:'',
          password:''
        },
        //用户信息
        loginInfo:{}
      }
    },

    methods: {
      //1.用户登录方法  
      submitLogin(){
          //第一步：调用接口进行登录，返回token字符串
            loginApi.submitLoginUser(this.user)
            .then(response => {
              if(response.data.success){
                //第二步：获取token字符串，并放到cookie中
                //参数1：cookie名称；参数2：参数值；参数3：作用范围
                cookie.set('guli_token', response.data.data.token)

                //第四步：调用api，根据token获取用户信息，为了首页面显示
                loginApi.getLoginInfo().then(response => {
                  //得到用户完整信息
                  this.loginInfo = response.data.data.userInfo
                  //将用户信息记录到cookie
                  cookie.set('guli_ucenter', this.loginInfo)
                  //跳转页面
                  window.location.href = "/";
                })
              }
            })
        },

      checkPhone (rule, value, callback) {
        //debugger
        if (!(/^1[34578]\d{9}$/.test(value))) {
          return callback(new Error('手机号码格式不正确'))
        }
        return callback()
      }
    }
  }
</script>
<style>
   .el-form-item__error{
    z-index: 9999999;
  }
</style>