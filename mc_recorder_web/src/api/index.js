import axios from 'axios'

// baseURL 用相对路径 /api:
//   - 开发: vite.config.js 配 proxy → 转发到 localhost:8000
//   - 生产: Nginx 配 location /api/ → 转发到 backend:8000
// 这样代码一份, 不用判断环境
const api = axios.create({
baseURL: '/api',
timeout: 30000,
})

// 请求拦截器: 自动加 JWT 头
api.interceptors.request.use(config => {
const token = localStorage.getItem('token')
if (token) {
    config.headers.Authorization = `Bearer ${token}`
}
return config
})

// 响应拦截器: 401 自动跳登录
api.interceptors.response.use(
response => response.data,    // 直接返回 data, 省一层 .data
error => {
    if (error.response?.status === 401) {
    localStorage.removeItem('token')
    window.location.href = '/login'
    }
    return Promise.reject(error)
}
)

export default api