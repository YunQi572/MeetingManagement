import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Home from '../views/Home.vue'
import Welcome from '../views/Welcome.vue'
import Approved from '../views/history/Approved.vue'
import History from '../views/history/History.vue'
import NotApproved from '../views/history/NotApproved.vue'
import Approving from '../views/history/Approving'
import Book from '../views/book/Book.vue'
import CancelBook from '../views/book/CancelBook.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  {
    path: '/home',
    component: Home,
    redirect: '/book',
    children: [
      { path: '/Welcome', component: Welcome },
      { path: '/approved', component: Approved },
      { path: '/history', component: History },
      { path: '/notApproved', component: NotApproved },
      { path: '/approving', component: Approving },
      { path: '/book', component: Book },
      { path: '/cancelBook', component: CancelBook }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to 表示将要访问的路径
  // from 表示从哪个路径跳转而来
  // next 是一个函数，表示放行
  // next() 直接放行 next('/login') 强制跳转
  if (to.path === '/login') return next()
  // 获取token
  const tokenStr = window.sessionStorage.getItem('token')
  if (!tokenStr) return next('/login')
  next()
})

export default router
