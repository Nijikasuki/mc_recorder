import api from './index'

// 绑定库街区账号
// 后端: POST /api/kurobbs/bind  body: { batToken, gameRoleId }
export function bindKurobbs(batToken, gameRoleId) {
  return api.post('/kurobbs/bind', { batToken, gameRoleId })
}

// 同步库街区角色到 resonator 表
// 后端: POST /api/kurobbs/sync → Result<SyncResult>
export function syncFromKurobbs() {
  return api.post('/kurobbs/sync')
}

// (测试用) 直接查库街区返回的角色, 不入库
export function getKurobbsCharacters() {
  return api.get('/kurobbs/characters')
}
