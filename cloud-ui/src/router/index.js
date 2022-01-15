import { createRouter, createWebHashHistory } from 'vue-router';
import store from '../store'
import server from '@/util/request'
import ImageIndex from '../views/ImageIndex.vue';
import ImageUpload from '../views/ImageUpload.vue';
import ImageRecycle from '../views/ImageRecycle.vue';
import ImageList from '../views/ImageList.vue';
import OpenApiGuide from '../views/OpenApiGuide.vue';
import OperatingGuide from '../views/OperatingGuide.vue';
import Login from '../views/Login.vue';
import UserInfo from '../views/UserInfo.vue';

const routes = [
  {
    path: '/',
    name: 'index',
    // redirect: '/img/upload',
    redirect: '/login',
    // component: Login,
    meta: { title: 'Index', authRequired: false, },
  },
  {
    path: '/img',
    name: 'img',
    component: ImageIndex,
    meta: { authRequired: true, },
    children: [
      {
        path: 'upload',
        name: 'upload',
        component: ImageUpload,
        meta: { title: 'Upload', },
      },
      {
        path: 'list',
        name: 'list',
        component: ImageList,
        meta: { title: 'List', },
      },
      {
        path: 'recycle',
        name: 'recycle',
        component: ImageRecycle,
        meta: { title: 'Recycle', },
      },
      {
        path: 'api',
        name: 'api',
        component: OpenApiGuide,
        meta: { title: 'Open Api', },
      },
      {
        path: 'guide',
        name: 'guide',
        component: OperatingGuide,
        meta: { title: 'Operating Guide', },
      },
      {
        path: 'user-info',
        name: 'user-info',
        component: UserInfo,
        meta: { title: 'User Info', },
      },
    ],
    redirect: '/img/upload',
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: { title: 'Sign In', authRequired: false, },
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

// 前置路由守卫
router.beforeEach((to, from, next)=>{
  // 页面需要权限，而没有访问令牌时
  if (to.meta.authRequired && !store.state.accessToken) {
    // 查询本地使用有accessToken存储
    const accessToken = localStorage.getItem('accessToken')
    if (accessToken) {
      store.commit('setAccessToken', accessToken)
      // 获取访问令牌后，请求用户信息和设置，初始化全局用户信息、全局用户设置
      server.all([server.get('/user-info/getUserInfo'), server.get('/setting/getSetting')])
        .then(server.spread((info, setting)=>{
          // 初始化全局用户信息、全局用户设置
          store.commit('updateUserInfo', info.data)
          store.commit('initSettings', setting.data)
          // 初始化成功后，跳转到指定页面
          document.title = to.meta.title + ' - imagecloud'
          next()
        })).catch(()=>{
          // 初始化失败，跳转到登录页
          next({name: 'login'})
        })
    } else {
      next({name: 'login'})
    }
  } else {
    document.title = to.meta.title + ' - imagecloud'
    next()
  }
});

export default router;
