import { createRouter, createWebHistory } from 'vue-router'
import Rooms from '../views/room/Rooms.vue'
import RoomHistory from '../views/room/RoomHistory.vue'
import Departments from '../views/department/Departments.vue'
import DepHistory from '../views/department/DepHistory.vue'
import Approved from '../views/approve/Approved.vue'
import NotApproved from '../views/approve/NotApproved.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    redirect: '/welcome',
    children: [
      {
        path: '/welcome',
        name: 'Welcome',
        component: () => import('../views/Welcome.vue')
      },
      { path: '/meetingRooms', component: Rooms },
      { path: '/meetingRoomHistory', component: RoomHistory },
      { path: '/department', component: Departments },
      { path: '/depHistory', component: DepHistory },
      { path: '/approved', component: Approved },
      { path: '/notApproved', component: NotApproved }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.path === '/login') return next()
  const tokenStr = window.sessionStorage.getItem('token')
  if (!tokenStr) return next('/login')
  next()
})

export default router
//
//
// import { createRouter, createWebHistory } from 'vue-router'
// import Rooms from '../components/room/Rooms.vue'
// import RoomHistory from '../components/room/RoomHistory.vue'
// import Departments from '../components/department/Departments.vue'
// import DepHistory from '../components/department/DepHistory.vue'
// import Approved from '../components/approve/Approved.vue'
// import NotApproved from '../components/approve/NotApproved.vue'
//
// const routes = [
//   {
//     path: '/login',
//     name: 'Login',
//     component: () => import('../components/Login.vue')
//   },
//   {
//     path: '/',
//     name: 'Home',
//     component: () => import('../components/Home.vue'),
//     redirect: '/welcome',
//     children: [
//       {
//         path: '/welcome',
//         name: 'Welcome',
//         component: () => import('../components/Welcome.vue')
//       },
//       { path: '/meetingRooms', component: Rooms },
//       { path: '/meetingRoomHistory', component: RoomHistory },
//       { path: '/department', component: Departments },
//       { path: '/depHistory', component: DepHistory },
//       { path: '/approved', component: Approved },
//       { path: '/notApproved', component: NotApproved }
//     ]
//   }
// ]
//
// const router = createRouter({
//   history: createWebHistory(process.env.BASE_URL),
//   routes
// })
//
// // 路由守卫
// router.beforeEach((to, from, next) => {
//   if (to.path === '/login') return next()
//   const tokenStr = window.sessionStorage.getItem('token')
//   if (!tokenStr) return next('/login')
//   next()
// })
//
// export default router
