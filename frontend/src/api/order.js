import request from './request'

export function createOrder(data) {
  return request.post('/order/create', data)
}

export function getOrderDetail(id) {
  return request.get(`/order/detail/${id}`)
}

export function getOrderList(params) {
  return request.get('/order/list', { params })
}

export function deliverOrder(data) {
  return request.post('/order/delivery', data)
}

export function acceptOrder(orderId) {
  return request.post(`/order/confirm/${orderId}`)
}

export function payFinal(orderId) {
  return request.post(`/order/pay/${orderId}`)
}

export function cancelOrder(orderId) {
  return request.post(`/order/cancel/${orderId}`)
}

export function requestInvoice(data) {
  return request.post('/order/invoice', data)
}
