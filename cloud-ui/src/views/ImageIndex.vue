<template>
  <div class="app-wrapper">
    <div :class="'sidebar-container has-logo '+($store.state.settings.theme==='light'?'theme-light':'')">
      <div class="sidebar-logo-container">
        <router-link to="/" class="sidebar-logo-link router-link-active">
          <img src="../assets/logo.png" class="sidebar-logo">
          <h1 class="sidebar-title theme-light-title">Image Cloud</h1>
        </router-link>
      </div>
      <ul class="sidebar-menu">
        <router-link v-for="link in sideBarMenu" :key="link.id" :to="link.to" active-class="is-active">
          <li class="sidebar-menu-item">
            <img class="icon theme-light-icon" :src="link.iconUrl">
            <span class="theme-light-span">{{ link.title }}</span>
          </li>
        </router-link>
      </ul>
      <div class="sidebar-stored theme-light-span">Stored: <span style="color: #409eff;">{{ storedSize }}</span></div>
    </div>
    <div class="main-container">
      <div :class="$store.state.settings.fixedHeader ? 'fixed-header' : ''">
        <div class="menubar">
          <div class="left-menu is-active">
            <img src="../assets/icon/left_menu/menu_open.svg" alt="">
          </div>
          <div class="right-menu">
            <el-dropdown trigger="click">
              <div style="cursor: pointer;">
                <img class="head-img" :src="$store.state.userInfo.avatarUrl">
                <img style="margin-bottom: 6px;" src="../assets/icon/user_menu/down.svg">
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <router-link to="/img/user-info">
                      <img class="dropdown-item-img" src="../assets/icon/user_menu/user.svg" alt="">User info
                    </router-link>
                  </el-dropdown-item>
                  <el-dropdown-item @click="settingsDrawer = true" divided>
                    <img class="dropdown-item-img" src="../assets/icon/user_menu/setting.svg" alt="">Setting
                  </el-dropdown-item>
                  <!-- 该部分暂时与布局设置合并
                  <el-dropdown-item @click="openSettingsDrawer('operation')">
                    <img class="dropdown-item-img" src="../assets/icon/user_menu/setting.svg" alt="">Operation
                  </el-dropdown-item> -->
                  <el-dropdown-item @click="signOut" divided>
                    <img class="dropdown-item-img" src="../assets/icon/user_menu/logout.svg" alt="">Sign out
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div class="right-menu">
            <span class="greet" style="color: #000000d0;">Hi, </span>
            <span class="greet" style="color: #409eff;">{{ $store.state.userInfo.nickname }}~</span>
          </div>
        </div>
      </div>
      <div class="app-main" :style="$store.state.settings.fixedHeader ? 'margin-top: calc(50px + 1rem)' : ''">
        <router-view/>
      </div>
      <el-drawer v-model="settingsDrawer" :with-header="false" :size="260" append-to-body>
        <div class="drawer-container">
          <h3 class="drawer-title">Theme style settings</h3>
          <div class="drawer-item-theme">
            <div class="theme-item">
              <div class="selected" @click="$store.state.settings.theme = 'dark'">
                <img src="../assets/icon/theme/selected.svg" v-if="$store.state.settings.theme === 'dark'">
              </div>
              <img class="theme-img" src="../assets/icon/theme/theme_dark.svg" alt="dark">
            </div>
            <div class="theme-item">
              <div class="selected" @click="$store.state.settings.theme = 'light'">
                <img src="../assets/icon/theme/selected.svg" v-if="$store.state.settings.theme === 'light'">
              </div>
              <img class="theme-img" src="../assets/icon/theme/theme_light.svg" alt="light">
            </div>
          </div>
          <div class="drawer-item">
            <span data-v-1269b55a="">Theme color</span>
            <el-color-picker v-model="$store.state.settings.themeColor" />
          </div>
          <el-divider></el-divider>
          <h3 class="drawer-title">System layout settings</h3>
          <div class="drawer-item">
            <span data-v-1269b55a="">Show Date In List</span>
            <el-switch v-model="$store.state.settings.showDateInList" />
          </div>
          <div class="drawer-item">
            <span data-v-1269b55a="">Show Date In Recycle</span>
            <el-switch v-model="$store.state.settings.showDateInRecycle" />
          </div>
          <div class="drawer-item">
            <span data-v-1269b55a="">Fixed Header</span>
            <el-switch v-model="$store.state.settings.fixedHeader" />
          </div>
          <div class="drawer-item">
            <span data-v-1269b55a="">Dynamic Title</span>
            <el-switch v-model="$store.state.settings.dynamicTitle" />
          </div>
          <el-divider></el-divider>
          <h3 class="drawer-title">User operation settings</h3>
          <div class="drawer-item">
            <span data-v-1269b55a="">Upload Directly</span>
            <el-switch v-model="$store.state.settings.uploadDirectly" />
          </div>
        </div>
      </el-drawer>
    </div>
  </div>
  <div id="shade">
    <transition name="fade">
      <div v-show="$store.state.shade">
        <div @click="closeShade" class="shade"></div>
        <img class="big-image" :src="$store.state.imageList[$store.state.imageListIndex].url">
        <div @click="closeShade" class="close-shade-btn hvr-grow"><img src="../assets/icon/image/close.svg"></div>
        <div @click="lastImage" class="last-image-btn hvr-grow"><img src="../assets/icon/image/left.svg"></div>
        <div @click="nextImage" class="next-image-btn hvr-grow"><img src="../assets/icon/image/right.svg"></div>
        <div class="copy-btn-group">
          <img @click="copy('url')" class="item hvr-grow" style="border-color: #1296db;" src="../assets/icon/image/copy_url.svg">
          <img @click="copy('html')" class="item hvr-grow" style="border-color: #e44d26;" src="../assets/icon/image/copy_html.svg">
          <img @click="copy('md')" class="item hvr-grow" style="border-color: #13227a;" src="../assets/icon/image/copy_md.svg">
        </div>
      </div>
    </transition>
    <el-message v-if="false"></el-message>
  </div>
</template>

<script>
import { onMounted, ref, computed, watch, toRaw } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import server from '@/util/request';
import { ElMessage } from 'element-plus'

export default {
  name: 'ImageIndex',
  components: {
    ElMessage,
  },
  setup() {
    // 侧边栏菜单
    const sideBarMenu = [
      {id: 'sideBarMenu_1', title: 'Image Upload', to: '/img/upload', iconUrl: require('../assets/icon/left_menu/upload.svg')},
      {id: 'sideBarMenu_2', title: 'Image List', to: '/img/list', iconUrl: require('../assets/icon/left_menu/image_list.svg')},
      {id: 'sideBarMenu_3', title: 'Image Recycle', to: '/img/recycle', iconUrl: require('../assets/icon/left_menu/recycle.svg')},
      {id: 'sideBarMenu_4', title: 'Open Api', to: '/img/api', iconUrl: require('../assets/icon/left_menu/open_api.svg')},
      {id: 'sideBarMenu_5', title: 'Operating Guide', to: '/img/guide', iconUrl: require('../assets/icon/left_menu/guide.svg')},
    ]
    const store = useStore()
    const router = useRouter()

    // 已存储大小
    const storedSize = computed(()=>{
      if (store.state.userInfo.storedSize < 1024)
        return `${store.state.userInfo.storedSize} B`
      else if (store.state.userInfo.storedSize < 1024*1024)
        return `${(store.state.userInfo.storedSize / 1024).toFixed(2)} KB`
      else if (store.state.userInfo.storedSize < 1024*1024*1024)
        return `${(store.state.userInfo.storedSize / 1024/1024).toFixed(2)} MB`
      else
        return `${(store.state.userInfo.storedSize / 1024/1024/1024).toFixed(2)} GB`
    })

    // 查看大图模式
    onMounted(() => {
      document.body.appendChild(document.getElementById('shade'))
    })
    function closeShade() {
      store.commit('changeShade', false)
      document.body.removeAttribute("class","ban-scroll");
    }
    function lastImage () {
      store.commit('changeImageListIndexByStep', -1)
    }
    function nextImage () {
      store.commit('changeImageListIndexByStep', 1)
    }
    function copy (category) {
      switch(category) {
        case 'url':
          navigator.clipboard.writeText(store.state.imageList[store.state.imageListIndex].url)
          .then(()=>{ ElMessage({message: 'The <b>URL</b> has been copied to the clipboard.', type: 'success', dangerouslyUseHTMLString: true,}) })
          .catch(err=>{ ElMessage.error(err) })
          break;
        case 'html':
          navigator.clipboard.writeText(`<img src="${store.state.imageList[store.state.imageListIndex].url}" alt="">`)
          .then(()=>{ ElMessage({message: 'The <b>H5 Tag</b> has been copied to the clipboard.', type: 'success', dangerouslyUseHTMLString: true,}) })
          .catch(err=>{ ElMessage.error(err) })
          break;
        case 'md':
          navigator.clipboard.writeText(`![](${store.state.imageList[store.state.imageListIndex].url})`)
          .then(()=>{ ElMessage({message: 'The <b>MD Tag</b> has been copied to the clipboard.', type: 'success', dangerouslyUseHTMLString: true,}) })
          .catch(err=>{ ElMessage.error(err) })
          break;
        default:
          ElMessage.error('error')
          break;
      }
    }

    // 设置面板（抽屉）
    const settingsDrawer = ref(false)

    // 监听设置项的变化
    watch(()=>store.state.settings, (newValue, oldValue)=>{
      // 第一次赋值为初始化，不进行处理
      if (!toRaw(oldValue).init) {
        const settingVO = toRaw(newValue)
        settingVO.userId = "1"
        server.post('/setting/updateSetting', settingVO).then(res=>{
          if (res.code === 200)
            ElMessage.success({message: 'Settings updated successfully.'})
        })
      }
    }, {deep: true,})

    // 退出登录
    function signOut() {
      store.commit('setAccessToken', '')
      localStorage.removeItem('accessToken')
      router.push({path: '/login'})
    }

    return {
      sideBarMenu,
      storedSize,
      closeShade, lastImage, nextImage, copy,
      settingsDrawer,
      signOut,
    }
  }
};
</script>

<style scoped>
/* theme light */
.theme-light {
  background-color: #ffffff !important;
}
.theme-light .theme-light-title {
  color: #001529 !important;
}
.theme-light .theme-light-icon {
  filter: invert(33%) sepia(3%) saturate(22%) hue-rotate(319deg) brightness(102%) contrast(85%);
}
.theme-light .theme-light-span {
  color: #000000b3;
}
/* theme light */

.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
}
.sidebar-container {
  -webkit-transition: width .28s;
  transition: width .28s;
  width: 200px!important;
  background-color: #304156;
  height: 100%;
  position: fixed;
  font-size: 0;
  top: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  -webkit-box-shadow: 2px 0 6px rgb(0 21 41 / 35%);
  box-shadow: 2px 0 6px rgb(0 21 41 / 35%);
}
.sidebar-container a {
  display: inline-block;
  width: 100%;
  overflow: hidden;
}
.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  text-align: center;
  overflow: hidden;
}
.sidebar-logo-container .sidebar-logo-link {
  height: 100%;
  width: 100%;
}
.sidebar-logo-container .sidebar-logo-link .sidebar-logo {
  width: 32px;
  height: 32px;
  vertical-align: middle;
  margin-right: 12px;
}
.sidebar-logo-container .sidebar-logo-link .sidebar-title {
  display: inline-block;
  margin: 0;
  color: #fff;
  font-weight: 600;
  line-height: 50px;
  font-size: 14px;
  vertical-align: middle;
}
.sidebar-menu {
  list-style: none;
  position: relative;
  margin: 0;
  padding-left: 0;
}
.sidebar-menu .is-active img {
  filter: invert(50%) sepia(79%) saturate(1433%) hue-rotate(189deg) brightness(100%) contrast(103%);
}
.sidebar-menu .is-active span {
  color: #409eff;
}
.sidebar-menu-item {
  height: 56px;
  line-height: 56px;
  font-size: 14px;
  color: #bfcbd9;
  padding: 0 20px;
  list-style: none;
  cursor: pointer;
  position: relative;
  -webkit-transition: border-color .3s,background-color .3s,color .3s;
  transition: border-color .3s,background-color .3s,color .3s;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  white-space: nowrap;
}
.sidebar-menu-item:hover {
  background-color: #0000000f;
}
.sidebar-menu-item img {
  width: 18px;
  height: 18px;
  margin-right: 14px;
}
.sidebar-menu-item * {
  vertical-align: middle;
}
.sidebar-stored {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 50px;
  line-height: 50px;
  text-align: center;
  font-size: 0.9rem;
  color: #bfcbd9;
}
.main-container {
  min-height: 100%;
  -webkit-transition: margin-left .28s;
  transition: margin-left .28s;
  margin-left: 200px;
  position: relative;
}
.menubar {
  height: 50px;
  position: relative;
  /* background: #fff; */
  -webkit-box-shadow: 0 1px 4px rgb(0 21 41 / 8%);
  box-shadow: 0 1px 4px rgb(0 21 41 / 8%);
}
.menubar .left-menu {
  box-sizing: border-box;
  height: 100%;
  float: left;
  padding: 15px;
  cursor: pointer;
  -webkit-transition: background .3s;
  transition: background .3s;
  -webkit-tap-highlight-color: transparent;
}
.menubar .left-menu:hover{
  background-color: #f9f9f9;
}
.menubar .left-menu img {
  width: 20px;
  height: 20px;
}
.left-menu.is-active {
  -webkit-transform: rotate(180deg);
  transform: rotate(180deg);
}
.menubar .right-menu {
  float: right;
  height: 100%;
  margin-right: 1rem;
}
.menubar .right-menu .greet {
  line-height: 50px;
}
.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 11;
  width: calc(100% - 200px);
  box-shadow: 0 4px 4px rgb(0 21 41 / 8%);
  background: #ffffff;
}
.head-img {
  box-sizing: border-box;
  width: 50px;
  padding: 4px;
  border-radius: 10px;
}
.app-main {
  min-height: calc(100vh - 50px - 1rem);
  width: 100%;
  position: relative;
}
.shade {
  width: 100%;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 11;
  background-color: #00000080;
}
.big-image {
  position: fixed;
  top: 50vh;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 12;
}
.fade-enter-active, .fade-leave-active {
  transition: all .2s linear;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
.close-shade-btn {
  position: fixed;
  top: 5vh;
  right: 5vh;
  width: 40px;
  height: 40px;
  z-index: 12;
}
.last-image-btn {
  position: fixed;
  top: 50vh;
  left: 5vh;
  width: 40px;
  height: 40px;
  z-index: 12;
}
.next-image-btn {
  position: fixed;
  top: 50vh;
  right: 5vh;
  width: 48px;
  height: 48px;
  z-index: 12;
}
.copy-btn-group {
  position: fixed;
  bottom: 8vh;
  left: calc(50vw - 80px);
  width: 160px;
  height: 40px;
  display: -webkit-flex;
  display: flex;
  justify-content: space-around;
  z-index: 12;
}
.copy-btn-group .item {
  box-sizing: border-box;
  width: 40px;
  height: 40px;
  padding: 3px;
  border: 1px dotted;
  border-radius: 4px;
}
.el-dropdown-menu__item {
  padding: 7px 14px;
}
.dropdown-item-img {
  margin-right: 5px;
}
.el-dropdown-menu__item:focus .dropdown-item-img,
.el-dropdown-menu__item:hover .dropdown-item-img {
  filter: invert(57%) sepia(78%) saturate(513%) hue-rotate(184deg) brightness(101%) contrast(104%);
}
.el-dropdown-menu__item--divided {
  margin-top: 0;
}
.drawer-container {
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;
}
.drawer-title {
  margin-bottom: 12px;
  color: rgba(0,0,0,.85);
  font-size: 14px;
  line-height: 22px;
}
.drawer-item {
  color: rgba(0,0,0,.75);
  font-size: 14px;
  padding: 12px 0;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.drawer-item-theme {
  display: -webkit-flex;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.drawer-item-theme .theme-item {
  width: 48px;
  height: 48px;
  margin-right: 16px;
}
.drawer-item-theme .theme-img {
  width: 48px;
  height: 48px;
  position: relative;
  top: -48px;
  z-index: 1;
}
.drawer-item-theme .selected {
  width: 48px;
  height: 48px;
  position: relative;
  z-index: 2;
  cursor: pointer;
}
.drawer-item-theme .selected img {
  position: relative;
  top: 50%;
  left: 60%;
  transform: translate(-50%, -50%);
}
</style>
