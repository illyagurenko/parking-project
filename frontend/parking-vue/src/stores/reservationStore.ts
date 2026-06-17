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
    async togglePayment(id: number, reservation: any) {
      reservation.isPaid = !reservation.isPaid
      await api.put(`/reservations/${id}`, reservation)
      this.fetchReservations()
    }
  }
})