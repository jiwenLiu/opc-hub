import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home/index.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login/index.vue'),
    meta: { guest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register/index.vue'),
    meta: { guest: true }
  },
  {
    path: '/opc/dashboard',
    name: 'OpcDashboard',
    component: () => import('@/views/OpcDashboard/index.vue'),
    meta: { requiresAuth: true, role: 'opc' }
  },
  {
    path: '/opc/service/publish',
    name: 'OpcServicePublish',
    component: () => import('@/views/OpcServicePublish/index.vue'),
    meta: { requiresAuth: true, role: 'opc' }
  },
  {
    path: '/opc/services',
    name: 'OpcServiceList',
    component: () => import('@/views/OpcServiceList/index.vue'),
    meta: { requiresAuth: true, role: 'opc' }
  },
  {
    path: '/opc/certification',
    name: 'OpcCertification',
    component: () => import('@/views/OpcCertification/index.vue'),
    meta: { requiresAuth: true, role: 'opc' }
  },
  {
    path: '/enterprise/dashboard',
    name: 'EnterpriseDashboard',
    component: () => import('@/views/EnterpriseDashboard/index.vue'),
    meta: { requiresAuth: true, role: 'employer' }
  },
  {
    path: '/enterprise/requirement/publish',
    name: 'EnterpriseRequirementPublish',
    component: () => import('@/views/EnterpriseRequirementPublish/index.vue'),
    meta: { requiresAuth: true, role: 'employer' }
  },
  {
    path: '/enterprise/requirements',
    name: 'EnterpriseRequirementList',
    component: () => import('@/views/EnterpriseRequirementList/index.vue'),
    meta: { requiresAuth: true, role: 'employer' }
  },
  {
    path: '/match',
    name: 'Match',
    component: () => import('@/views/Match/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/orders',
    name: 'OrderList',
    component: () => import('@/views/OrderList/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order/:id',
    name: 'OrderDetail',
    component: () => import('@/views/OrderDetail/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/service/:id',
    name: 'ServiceDetail',
    component: () => import('@/views/ServiceDetail/index.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/tools',
    name: 'Tools',
    component: () => import('@/views/Tools/index.vue')
  },
  {
    path: '/message',
    name: 'Message',
    component: () => import('@/views/Message/index.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  if (to.meta.role && userStore.role !== to.meta.role) {
    if (userStore.role === 'opc') {
      next({ name: 'OpcDashboard' })
    } else {
      next({ name: 'EnterpriseDashboard' })
    }
    return
  }

  if (to.meta.guest && token) {
    next({ name: userStore.role === 'opc' ? 'OpcDashboard' : 'EnterpriseDashboard' })
    return
  }

  next()
})

export default router
