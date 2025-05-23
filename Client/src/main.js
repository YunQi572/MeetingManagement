import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/fonts/iconfont.css'
import './assets/css/global.css'
import moment from 'moment'
import axios from 'axios'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

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
    console.error('Request error:', error)
    return Promise.reject(error)
})

// 响应拦截器
axios.interceptors.response.use(response => {
    // 对响应数据做点什么
    return response
}, error => {
    // 对响应错误做点什么
    console.error('Response error:', error)
    return Promise.reject(error)
})

console.log('Creating Vue app...')

try {
    const app = createApp(App)

    // Register plugins
    app.use(store)
    app.use(router)
    app.use(ElementPlus)

    // 注册所有Element Plus图标组件
    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }

    // 全局配置axios
    app.config.globalProperties.$http = axios
    // 全局配置moment
    app.config.globalProperties.$moment = moment

    // Error handler
    app.config.errorHandler = (err, vm, info) => {
        console.error('Vue error:', err, info)
    }

    console.log('Mounting Vue app...')
    app.mount('#app')
    console.log('Vue app mounted successfully')
} catch (error) {
    console.error('Failed to initialize Vue app:', error)
}
