import { createStore } from 'vuex'

export default createStore({
  state: {
    // 除登录、注册、发送验证码外，所有请求需携带
    accessToken: '',
    // 设置
    settings: {
      theme: 'dark',
      themeColor: '#409EFF',
      showDateInList: true,
      showDateInRecycle: true,
      fixedHeader: false,
      dynamicTitle: true,
      uploadDirectly: true,
      init: true, // 标志第一次更新为初始化，watch将不进行处理
    },
    // 一些全局用户信息
    userInfo: {
      username: '',
      nickname: '',
      avatarUrl: '',
      storedSize: 0,
      secondaryPathList: [],
      createTime: '',
    },
    // 查看大图模式
    shade: false,
    imageList: [{id: "", url: ""}],
    imageListIndex: 0,
  },
  mutations: {
    // 添加访问令牌
    setAccessToken(state, newValue) {
      state.accessToken = newValue
    },
    // 初始化设置
    initSettings(state, newValue) {
      for (const key in newValue) {
        if (Object.hasOwnProperty.call(state.settings, key))
          state.settings[key] = newValue[key]
      }
      state.settings.init = false
    },
    // 更新全局用户信息
    initUserInfo(state, newValue) {
      for (const key in newValue) {
        if (Object.hasOwnProperty.call(state.userInfo, key))
          state.userInfo[key] = newValue[key]
      }
    },
    pushSecondaryPathList(state, newValue) {
      state.userInfo.secondaryPathList.push(newValue)
    },
    increaseStoredSize(state, newValue) {
      state.userInfo.storedSize += newValue
    },
    // 查看大图模式
    changeShade(state, newValue) {
      state.shade = newValue;
    },
    changeImageList(state, newValue) {
      state.imageList = newValue;
    },
    changeImageListIndex(state, newValue) {
      state.imageListIndex = newValue;
    },
    changeImageListIndexByStep(state, newValue) {
      state.imageListIndex = (state.imageListIndex + newValue + state.imageList.length) % state.imageList.length;
    },
  },
  actions: {
  },
  modules: {
  }
})
