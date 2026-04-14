import request from './request'

export function aiMatch(data) {
  return request.post('/match/ai', data)
}

export function publishRequirement(data) {
  return request.post('/requirement/publish', data)
}

export function getRequirementList(params) {
  return request.get('/requirements', { params })
}

export function getRequirementDetail(id) {
  return request.get(`/requirement/${id}`)
}

export function confirmMatch(matchRecordId, accepted) {
  return request.post(`/match/confirm/${matchRecordId}`, { accepted })
}
