import api from './index'

// 1. 拉所有角色列表 (无分页)
// 后端: GET /api/resonators → Result<List<Resonator>>
export function listResonators() {
    return api.get('/resonators')
}

// 2. 分页拉(如果想用分页)
// 后端: GET /api/resonators/page?pageNum=1&pageSize=10 → Result<Page<Resonator>>
export function listResonatorsPaged(pageNum = 1, pageSize = 10) {
    return api.get('/resonators/page', { params: { pageNum, pageSize } })
}

// 3. 查单个
// 后端: GET /api/resonators/{id} → Result<Resonator>
export function getResonator(id) {
    return api.get(`/resonators/${id}`)
}

// 4. 新增
// 后端: POST /api/resonators (body: Resonator) → Result<Resonator>
export function createResonator(data) {
    return api.post('/resonators', data)
}

// 5. 更新
// 后端: PUT /api/resonators/{id} (body: Resonator) → Result<Resonator>
export function updateResonator(id, data) {
    return api.put(`/resonators/${id}`, data)
}

// 6. 删除
// 后端: DELETE /api/resonators/{id} → Result<Void>
export function deleteResonator(id) {
    return api.delete(`/resonators/${id}`)
}