import axios from 'axios'
import store from '../store'
import router from '../router'
import { ElMessage } from 'element-plus'

// 创建axios实例
const server = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  // baseURL: 'https://imagecloud.shiyq.top/api',
  baseURL: '/api',
  // 超时
  timeout: 30000
})
server.all = axios.all
server.spread = axios.spread

// request拦截器
server.interceptors.request.use(config => {
  // 设置头部携带accessToken
  if (store.state.accessToken) config.headers.Authorization = `Bearer ${store.state.accessToken}`
  // get请求映射params参数
  if (config.method === 'get' && config.params) {
    let url = config.url + '?';
    for (const propName of Object.keys(config.params)) {
      const value = config.params[propName];
      var part = encodeURIComponent(propName) + "=";
      if (value !== null && typeof(value) !== "undefined") {
        if (typeof value === 'object') {
          for (const key of Object.keys(value)) {
            if (value[key] !== null && typeof (value[key]) !== 'undefined') {
              let params = propName + '[' + key + ']';
              let subPart = encodeURIComponent(params) + '=';
              url += subPart + encodeURIComponent(value[key]) + '&';
            }
          }
        } else {
          url += part + encodeURIComponent(value) + "&";
        }
      }
    }
    url = url.slice(0, -1);
    config.params = {};
    config.url = url;
  }
  return config
}, error => {
    console.log(error)
    Promise.reject(error)
})

// 响应拦截器
server.interceptors.response.use(
  res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;
    // 获取错误信息
    const msg = res.data.msg || 'Unknown error, please contact administrator!'
    
    if (code >=200 && code <300) {
      return res.data
    } else if (code === 401) {
      store.commit('setAccessToken', '')
      localStorage.removeItem('accessToken')
      router.push({path: '/login'})
    }
    ElMessage.error(msg)
    return Promise.reject(msg)
  },
  error => {
    console.log('err' + error)
    let { message } = error;
    if (message == "Network Error") {
      message = "后端接口连接异常";
    }
    else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    }
    else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    ElMessage({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default server
