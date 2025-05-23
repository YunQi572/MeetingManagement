import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '../src/assets/fonts/iconfont.css'
import axios from 'axios'

// 配置axios
axios.defaults.baseURL = '/api' // 使用代理URL
axios.defaults.timeout = 5000 // 设置超时时间
axios.defaults.headers.post['Content-Type'] = 'application/json'

// 请求拦截器
axios.interceptors.request.use(config => {
    // 在发送请求之前做些什么
    const token = window.sessionStorage.getItem('token')
    if (token) {
        config.headers.Authorization = token
    }
    return config
}, error => {
    // 对请求错误做些什么
    return Promise.reject(error)
})

// 响应拦截器
axios.interceptors.response.use(response => {
    // 对响应数据做点什么
    return response
}, error => {
    // 对响应错误做点什么
    return Promise.reject(error)
})


const app = createApp(App)
app.use(store)
app.use(router)
app.use(ElementPlus)

// 全局配置axios
app.config.globalProperties.$http = axios

app.mount('#app')
