import api from './index'

// 登录: POST /api/auth/login
export function login(username, password) {
    return api.post('/auth/login', { username, password })
}

// 注册: POST /api/auth/register
export function register(username, password, email) {
    return api.post('/auth/register', { username, password, email })
}

// 查当前用户: GET /api/auth/me
export function getCurrentUser() {
    return api.get('/auth/me')
}