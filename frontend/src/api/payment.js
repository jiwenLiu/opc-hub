import request from './request'

export function createPayment(data) {
  return request.post('/payment/create', data)
}

export function getPaymentStatus(paymentNo) {
  return request.get(`/payment/status/${paymentNo}`)
}

export function payDeposit(data) {
  return request.post('/payment/deposit', data)
}

export function getDepositInfo() {
  return request.get('/payment/deposit-info')
}

export function payFinal(orderId) {
  return request.post(`/payment/pay-final/${orderId}`)
}
