import { defineStore } from 'pinia'
import { api } from './api'

export const useReservationStore = defineStore('reservation', {
  state: () => ({
    reservations: [] as any[],
    totalReservations: 0,
    page: 0,
    size: 10,
    carSearch: '',
    clientSearch: ''
  }),
  actions: {
    async fetchReservations(carNumber?: string, clientFullName?: string, page?: number, size?: number) {
      if (carNumber !== undefined) this.carSearch = carNumber
      if (clientFullName !== undefined) this.clientSearch = clientFullName
      if (page !== undefined) this.page = page
      if (size !== undefined) this.size = size

      try {
        const res = await api.get('/reservations', {
          params: {
            carNumber: this.carSearch,
            clientFullName: this.clientSearch,
            page: this.page,
            size: this.size
          }
        })
        this.reservations = res.data.content
        this.totalReservations = res.data.totalElements
      } catch (error: any) {
        console.error('Не удалось загрузить бронирования', error)
      }
    },
    async addReservation(reservation: any) {
      try {
        await api.post('/reservations', reservation)
        this.fetchReservations()
      } catch (error: any) {
        console.error('Не удалось создать бронирование', error)
      }
    },
    async updateReservation(id: number, reservation: any) {
      try {
        await api.put(`/reservations/${id}`, reservation)
        this.fetchReservations()
      } catch (error: any) {
        console.error('Не удалось обновить бронирование', error)
      }
    },
    async deleteReservation(id: number) {
      try {
        await api.delete(`/reservations/${id}`)
        this.fetchReservations()
      } catch (error: any) {
        console.error('Не удалось удалить бронирование', error)
      }
    },
    async togglePayment(id: number, reservation: any) {
      // Сохраняем исходное состояние на случай ошибки бэкенда
      const originalPaidStatus = reservation.isPaid
      
      try {
        reservation.isPaid = !reservation.isPaid
        await api.put(`/reservations/${id}`, reservation)
        this.fetchReservations()
      } catch (error: any) {
        // Возвращаем исходный статус, если бэкенд отклонил запрос
        reservation.isPaid = originalPaidStatus
        console.error('Не удалось обновить статус оплаты', error)
      }
    }
  }
})