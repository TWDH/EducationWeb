<template>
  <div class="in-wrap">
    <!-- 公共头引入 -->
    <header id="header">
      <section class="container">
        <h1 id="logo">
          <a href="#" title="若枫若影">
            <img src="~/assets/img/logo-maple.jpg" width="60%" height="100%" alt="若枫若影">
          </a>
        </h1>
        <div class="h-r-nsl">
          <ul class="nav">
            <router-link to="/" tag="li" active-class="current" exact>
              <a>Main</a>
            </router-link>
            <router-link to="/course" tag="li" active-class="current">
              <a>Movie</a>
            </router-link>
            <router-link to="/teacher" tag="li" active-class="current">
              <a>Artist</a>
            </router-link>
            <!-- <router-link to="/article" tag="li" active-class="current">
              <a>文章</a>
            </router-link>
            <router-link to="/qa" tag="li" active-class="current">
              <a>问答</a>
            </router-link> -->
          </ul>
           <!-- nav:右上角登录 -->
            <ul class="h-r-login">
              <!-- 如果loginInfo里面没有值：未登录 -->
                <li v-if="!loginInfo.id" id="no-login">
                    <a href="/login" title="登录">
                        <em class="icon18 login-icon">&nbsp;</em>
                        <span class="vam ml5">Login</span>
                    </a>
                    |
                    <a href="/register" title="注册">
                        <span class="vam ml5">Register</span>
                    </a>
                </li>
                <!-- 如果loginInfo里面有值：已登录 -->
                <li v-if="loginInfo.id" id="is-login-one" class="mr10">
                    <a id="headerMsgCountId" href="#" title="消息">
                        <em class="icon18 news-icon">&nbsp;</em>
                    </a>
                    <q class="red-point" style="display: none">&nbsp;</q>
                </li>
                <!-- 已登录：显示头像，用户名等 -->
                <li v-if="loginInfo.id" id="is-login-two" class="h-r-user">
                    <a href="/ucenter" title>
                        <img
                            :src="loginInfo.avatar"
                            width="30"
                            height="30"
                            class="vam picImg"
                            alt
                            >
                        <span id="userName" class="vam disIb">{{ loginInfo.nickname }}</span>
                    </a>
                    <a href="javascript:void(0);" title="退出" @click="logout()" class="ml5">Logout</a>
                </li>
                <!-- /未登录显示第1 li；登录后显示第2，3 li -->
            </ul>
          <aside class="h-r-search">
            <form action="#" method="post">
              <label class="h-r-s-box">
                <input type="text" placeholder="Search Movie" name="queryCourse.courseName" value>
                <button type="submit" class="s-btn">
                  <em class="icon18">&nbsp;</em>
                </button>
              </label>
            </form>
          </aside>
        </div>
        <aside class="mw-nav-btn">
          <div class="mw-nav-icon"></div>
        </aside>
        <div class="clear"></div>
      </section>
    </header>
    <!-- /公共头引入 -->
      
    <nuxt/>

    <!-- 公共底引入 -->
    <footer id="footer">
      <section class="container">
        <div class>
          <!-- <h4 class="hLh30">
            <span class="fsize18 f-fM c-999">友情链接</span>
          </h4>
          <ul class="of flink-list">
            <li>
              <a href="http://www.atguigu.com/" title="尚硅谷" target="_blank">尚硅谷</a>
            </li>
          </ul> -->
          <!-- <div class="clear"></div> -->
        </div>
        <div class="b-foot">
          <section class="fl col-7">
            <section class="mr20">
              <section class="b-f-link">
                <a href="#" title="关于我们" target="_blank">About us</a>|
                <a href="#" title="联系我们" target="_blank">Contact us</a>|
                <a href="#" title="帮助中心" target="_blank">Help</a>|
                <a href="#" title="资源下载" target="_blank">Reources Download</a>|
                <span>Tel：613-869-9690 (Auther)</span>
                <span>Email：hezhukevin@gmail.com</span>
              </section>
              <section class="b-f-link mt10">
                <span>©All Rights Reserved by He Zhu</span>
              </section>
            </section>
          </section>
          <aside class="fl col-3 tac mt15">
            <section class="gf-tx">
              <span>
                <img src="~/assets/img/wx-icon.png" alt>
              </span>
            </section>
            <section class="gf-tx">
              <span>
                <img src="~/assets/img/wb-icon.png" alt>
              </span>
            </section>
          </aside>
          <div class="clear"></div>
        </div>
      </section>
    </footer>
    <!-- /公共底引入 -->
  </div>
</template>
<script>
import '~/assets/css/reset.css'
import '~/assets/css/theme.css'
import '~/assets/css/global.css'
import '~/assets/css/web.css'
import '~/assets/css/base.css'
import '~/assets/css/activity_tab.css'
import '~/assets/css/bottom_rec.css'
import '~/assets/css/nice_select.css'
import '~/assets/css/order.css'
import '~/assets/css/swiper-3.3.1.min.css'
import "~/assets/css/pages-weixinpay.css"

import cookie from 'js-cookie'
import loginApi from '@/api/login'

export default {
  data() {
    return {
      //token
      token:'',

      //用户信息
      loginInfo:{
        id: '',
        age: '',
        avatar: '',
        mobile: '',
        nickname: '',
        sex: ''
      },

    }
  },

  created() {
    //获取路径中的token
    this.token = this.$route.query.token
    //判断路径是否有token
    if(this.token){
        //调用方法
        this.wxLogin()
    }

    this.showInfo()
  },

  methods: {
    //1.创建方法，从cookie获取信息
    showInfo(){
      //1.1 从cookie获取用户信息
      var userStr = cookie.get('guli_ucenter')

      //1.2 cookie中存放的是json的字符串形式(多了引号"")，需要把字符串转为json对象(js对象)
      if(userStr){
        this.loginInfo = JSON.parse(userStr)
      }
    },

    //2.登出
    logout(){
      //清空cookie中的值
      cookie.set('guli_token', '', { domain: '47.94.174.79' })
      cookie.set('guli_ucenter', '', { domain: '47.94.174.79' })

      //跳转到首页面
      window.location.href='/'
    },

    //3.微信登录显示方法
    wxLogin(){
      //1.把路径上的token放到cookie中
      cookie.set('guli_token', this.token, { domain: '47.94.174.79' })
      cookie.set('guli_ucenter', '', { domain: '47.94.174.79' })

      //2.调用接口，根据token值获取用户信息(此时cookie已经放到了header中)
      loginApi.getLoginInfo()
        .then(response => {
          this.loginInfo = response.data.data.userInfo
          cookie.set('guli_ucenter', 'this.loginInfo', { domain: '47.94.174.79' })
        })
    },
    
  }
};
</script>