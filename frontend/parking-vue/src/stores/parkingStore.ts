import { defineStore } from 'pinia'

const API_BASE = 'http://localhost:8080/api'

export interface Client {
  id?: number
  fullName: string
}

export interface Car {
  id?: number
  numberCar: string
  clientId?: number
}

export interface ParkingSpace {
  id?: number
  numberSpace: string
}

export interface Reservation {
  id?: number
  parkingId: number
  carId: number
  isPaid: boolean
}


export interface CarDto {
  id: number
  numberCar: string
  clientId?: number
  clientName?: string 
}

export interface ReservationDto {
  id: number
  parkingId: number
  parkingSpaceNumber: string
  carId: number
  carNumber: string
  clientFullName?: string 
  isPaid: boolean
  startTime: string
  endTime?: string
}

export const useParkingStore = defineStore('parking', {
  state: () => ({
    clients: [] as Client[],
    cars: [] as CarDto[], 
    spaces: [] as ParkingSpace[],
    availableSpaces: [] as ParkingSpace[],
    reservations: [] as ReservationDto[], 
  }),
  actions: {
    // Клиенты (без изменений)
    async fetchClients(search = '') {
      const res = await fetch(`${API_BASE}/clients?search=${search}`)
      this.clients = await res.json()
    },
    async createClient(client: Client) {
      await fetch(`${API_BASE}/clients`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(client)
      })
      await this.fetchClients()
    },
    async updateClient(client: Client) {
      await fetch(`${API_BASE}/clients/${client.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(client)
      })
      await this.fetchClients()
    },
    async deleteClient(id: number) {
      await fetch(`${API_BASE}/clients/${id}`, { method: 'DELETE' })
      await this.fetchClients()
    },

    // Автомобили (принимают чистую сущность Car, а fetch получает CarDto)
    async fetchCars(search = '') {
      const res = await fetch(`${API_BASE}/cars?search=${search}`)
      this.cars = await res.json() // Сюда приходят CarDto
    },
    async createCar(car: Car) { // Отправляем чистую сущность Car
      await fetch(`${API_BASE}/cars`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(car)
      })
      await this.fetchCars()
    },
    async updateCar(car: Car) { // Отправляем чистую сущность Car
      await fetch(`${API_BASE}/cars/${car.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(car)
      })
      await this.fetchCars()
    },
    async deleteCar(id: number) {
      await fetch(`${API_BASE}/cars/${id}`, { method: 'DELETE' })
      await this.fetchCars()
    },

    // Парковочные места (без изменений)
    async fetchSpaces() {
      const res = await fetch(`${API_BASE}/spaces`)
      this.spaces = await res.json()
    },
    async fetchAvailableSpaces() {
      const res = await fetch(`${API_BASE}/spaces/available`)
      this.availableSpaces = await res.json()
    },
    async createSpace(space: ParkingSpace) {
      await fetch(`${API_BASE}/spaces`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(space)
      })
      await this.fetchSpaces()
    },
    async updateSpace(space: ParkingSpace) {
      await fetch(`${API_BASE}/spaces/${space.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(space)
      })
      await this.fetchSpaces()
    },
    async deleteSpace(id: number) {
      await fetch(`${API_BASE}/spaces/${id}`, { method: 'DELETE' })
      await this.fetchSpaces()
    },

    // Бронирование (принимает чистую сущность Reservation, fetch получает ReservationDto)
    async fetchReservations(search = '') {
      const res = await fetch(`${API_BASE}/reservations?search=${search}`)
      this.reservations = await res.json() // Получаем ReservationDto
    },
    async createReservation(res: Reservation) { // Отправляем чистую Reservation
      await fetch(`${API_BASE}/reservations`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(res)
      })
      await this.fetchReservations()
      await this.fetchAvailableSpaces()
    },
    async togglePayment(id: number, isPaid: boolean) {
      await fetch(`${API_BASE}/reservations/${id}/pay?isPaid=${isPaid}`, { method: 'PUT' })
      await this.fetchReservations()
    },
    async releaseReservation(id: number) {
      await fetch(`${API_BASE}/reservations/${id}/release`, { method: 'PUT' })
      await this.fetchReservations()
      await this.fetchAvailableSpaces()
    }
  }
})