import axios from 'axios'

export const api = axios.create({
  baseURL: 'http://localhost:8080/api'
})

api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    let errorMsg = 'Произошла неизвестная ошибка'

    if (error.response) {
      // Сервер ответил кодом ошибки (4xx, 5xx)
      const data = error.response.data

      if (typeof data === 'string' && data.includes('<html')) {

        errorMsg = `Ошибка сервера (Код: ${error.response.status})`
      } else if (data && data.message) {
        // Если бэкенд прислал структурированную JSON-ошибку с полем message
        errorMsg = data.message
      } else if (typeof data === 'string' && data.trim() !== '') {
        // Если бэкенд прислал обычную строку текста ошибки
        errorMsg = data
      } else {
        // Резервный вывод в зависимости от HTTP-статуса
        errorMsg = `Ошибка запроса. Статус: ${error.response.status}`
      }
    } else if (error.request) {
      // Запрос отправлен, но ответ не получен (сервер выключен, проблемы с сетью или CORS)
      errorMsg = 'Сервер недоступен. Проверьте, запущен ли бэкенд.'
    } else {
      // Ошибка на этапе отправки запроса во фронтенде
      errorMsg = error.message
    }

    // Вывод ошибки один раз глобально
    alert(`Ошибка:\n${errorMsg}`)

    return Promise.reject(error) 
  }
)