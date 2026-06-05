import axios from 'axios'

const api = axios.create({
baseURL: 'http://localhost:8000/api',
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