import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    // 登录页: 不带 layout 壳
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/LoginView.vue')
    },
    // 主应用: 用 MainLayout 当壳, 里面切子路由
    {
        path: '/',
        component: () => import('../views/MainLayout.vue'),
        redirect: '/home',
        meta: { requiresAuth: true },        // 需要登录才能访问
        children: [
            { path: 'home', name: 'Home', component: () => import('../views/HomeView.vue') },
            { path: 'resonators', name: 'Resonators', component: () => import('../views/ResonatorListView.vue') },
            { path: 'ai', name: 'Ai', component: () => import('../views/AiChatView.vue') },
        ]
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})


// ⭐  路由守卫: 每次跳路由前检查登录
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.meta.requiresAuth && !token) {
        // 需要登录但没 token → 踢回登录页
        next('/login')
    } else if (to.path === '/login' && token) {
        // 已登录还想去登录页 → 直接进主页
        next('/home')
    } else {
        next()    // 放行
    }
})

export default router