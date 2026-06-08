import { defineStore } from 'pinia'
import { api } from './api'

export const useReservationStore = defineStore('reservation', {
  state: () => ({
    reservations: [] as any[]
  }),
  actions: {
    //  функция делает запрос на получение всех бронирований
    async fetchReservations(carNumber = '', clientFullName = '') {
      const res = await api.get('/reservations', { params: { carNumber, clientFullName } })
      this.reservations = res.data
    },
    //  функция делает запрос на создание брони
    async addReservation(reservation: any) {
      await api.post('/reservations', reservation)
      this.fetchReservations()
    },
    //  функция делает запрос на обновление брони
    async updateReservation(id: number, reservation: any) {
      await api.put(`/reservations/${id}`, reservation)
      this.fetchReservations()
    },
    //  функция делает запрос на удаление брони
    async deleteReservation(id: number) {
      await api.delete(`/reservations/${id}`)
      this.fetchReservations()
    },
    //  функция делает запрос на переключение статуса оплаты
    async togglePayment(id: number, reservation: any) {
      reservation.isPaid = !reservation.isPaid
      await api.put(`/reservations/${id}`, reservation)
      this.fetchReservations()
    }
  }
})
