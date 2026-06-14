import api from './index'

export function sendChat(message) {
    return api.post('/ai/chat', null, { params: { message } })
}

/**
 * 流式聊天 (SSE 自定义事件类型: token / node / done)
 *
 * 不能用 axios (不支持流式), 用浏览器原生 fetch + ReadableStream.
 *
 * @param req         { message, conversation_id, enable_search, enable_knowledge }
 * @param callbacks   { onToken, onNode, onDone }
 * @param signal      AbortController.signal
 */
export async function streamChat(req, callbacks, signal) {
    const token = localStorage.getItem('token')

    const response = await fetch('/api/ai/chat-stream', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(req),
        signal,
    })

    if (!response.ok) {
        throw new Error('请求失败: ' + response.status)
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')

    let buffer = ''
    let currentEvent = 'message'

    while (true) {
        const { done, value } = await reader.read()
        if (done) break

        buffer += decoder.decode(value, { stream: true })

        // SSE 协议按 \n 分行解析
        const lines = buffer.split('\n')
        buffer = lines.pop()    // 最后一行可能不完整, 留到下次

        for (const line of lines) {
            if (line.startsWith('event:')) {
                currentEvent = line.slice(6).trim()
            } else if (line.startsWith('data:')) {
                // 去掉 "data:" 前缀 + 可选前导空格
                const data = line.slice(5).replace(/^ /, '')

                if (currentEvent === 'token') {
                    callbacks.onToken?.(data)
                } else if (currentEvent === 'node') {
                    callbacks.onNode?.(data)
                } else if (currentEvent === 'done') {
                    callbacks.onDone?.()
                }
            }
            // 空行 = 一个 SSE 事件结束, currentEvent 保留到下个 event: 行
        }
    }
}
