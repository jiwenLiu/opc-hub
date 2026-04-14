import request from './request'

export function submitOpcCertification(data) {
  return request.post('/opc/certification', data)
}

export function getOpcCertificationStatus() {
  return request.get('/opc/certification/status')
}

export function submitOpcService(data) {
  return request.post('/opc/service', data)
}

export function getOpcServiceList(params) {
  return request.get('/opc/services', { params })
}

export function getOpcServiceDetail(id) {
  return request.get(`/opc/service/${id}`)
}

export function updateOpcService(id, data) {
  return request.put(`/opc/service/${id}`, data)
}

export function submitEnterpriseCertification(data) {
  return request.post('/enterprise/certification', data)
}

export function getServiceCategories() {
  return request.get('/service/categories')
}
