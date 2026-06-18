import { defineStore } from 'pinia'
import { api } from './api'

export const useReservationStore = defineStore('reservation', {
  //state - переменные, массивы для сущностей в таблицы, данные для пагинации и фильтрации
  state: () => ({
    reservations: [] as any[],
    totalReservations: 0,
    page: 0,
    size: 10,
    carSearch: '',
    clientSearch: ''
  }),
  actions: {
    //основная таблица брони
    async fetchReservations(numberCar?: string, fullName?: string, page?: number, size?: number) {
      if (numberCar !== undefined) this.carSearch = numberCar
      if (fullName !== undefined) this.clientSearch = fullName
      if (page !== undefined) this.page = page
      if (size !== undefined) this.size = size

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
    },
    //crud операции с бронью
    async addReservation(reservation: any) {
      await api.post('/reservations', reservation)
      this.fetchReservations()
    },
    async updateReservation(id: number, reservation: any) {
      await api.put(`/reservations/${id}`, reservation)
      this.fetchReservations()
    },
    async deleteReservation(id: number) {
      await api.delete(`/reservations/${id}`)
      this.fetchReservations()
    },
    //оплачено/неоплачено
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
      }
    }
  }
})