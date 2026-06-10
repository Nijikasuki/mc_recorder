import api from './index'

export function sendChat(message){
    return api.post('/ai/chat',null,{params:{ message }})
}

// ⭐  新增: 流式版本
// 不能用 axios(它不支持流式响应), 用浏览器原生 fetch
export async function streamChat(message, onChunk, signal) {
    const token = localStorage.getItem('token')
    // 用相对路径, 让 vite proxy / Nginx 转发, 不写死 localhost
    const url = `/api/ai/chat-stream?message=${encodeURIComponent(message)}`

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
        },
        signal,    // ⭐ AbortController.signal, 用于取消请求
    })

    if (!response.ok) {
        throw new Error('请求失败: ' + response.status)
    }

    // 读取流式响应
    const reader = response.body.getReader()
    const decoder = new TextDecoder()

    while (true) {
        const { done, value } = await reader.read()
        if (done) break

        const chunk = decoder.decode(value, { stream: true })

        // SSE 格式: 每行是 "data:内容\n", 解析出来
        const lines = chunk.split('\n')
        for (const line of lines) {
            if (line.startsWith('data:')) {
                const data = line.slice(5)    // 去掉 "data:" 前缀
                if (data) {
                    onChunk(data)                // 回调通知页面"来了一小段"
                }
            }
        }
    }
}