import axios from 'axios'

// создание hhtp-клиента
export const api = axios.create({
  baseURL: 'http://localhost:8080/api'
})

api.interceptors.response.use(
  //если нет ошибки
  (response) => response,
  //еслиесть
  (error) => {
    let errorMsg = 'Произошла неизвестная ошибка'

    if (error.response) {
      const data = error.response.data

      if (typeof data === 'string' && data.includes('<html')) {
        errorMsg = `Ошибка сервера (Код: ${error.response.status})`
      } else if (data && data.message) {
        errorMsg = data.message
      } else if (typeof data === 'string' && data.trim() !== '') {
        errorMsg = data
      } else {
        errorMsg = `Ошибка запроса. Статус: ${error.response.status}`
      }
    } else if (error.request) {
      errorMsg = 'Сервер недоступен.'
    } else {
      errorMsg = error.message
    }

    alert(`Ошибка:\n${errorMsg}`)
    return Promise.reject(error) 
  }
)