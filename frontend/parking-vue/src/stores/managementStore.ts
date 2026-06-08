import { defineStore } from 'pinia'
import { api } from './api'

export const useManagementStore = defineStore('management', {
  state: () => ({
    clients: [] as any[],
    cars: [] as any[],
    parkingSpaces: [] as any[]
  }),
  actions: {
    // функция делает запрос на получение списка клиентов
    async fetchClients(search = '') {
      const res = await api.get('/clients', { params: { search } })
      this.clients = res.data
    },
    // функция делает пост запрос на добавление клиента
    async addClient(client: any) {
      await api.post('/clients', client)
      this.fetchClients()
    },
    // функция делает запрос на изменение клиента
    async updateClient(id: number, client: any) {
      await api.put(`/clients/${id}`, client)
      this.fetchClients()
    },
    // функция делает запрос на удаление клиента
    async deleteClient(id: number) {
      await api.delete(`/clients/${id}`)
      this.fetchClients()
    },

    // функция делает запрос на получение списка машин
    async fetchCars(search = '') {
      const res = await api.get('/cars', { params: { search } })
      this.cars = res.data
    },
    // функция делает пост запрос на добавление машины
    async addCar(car: any) {
      await api.post('/cars', car)
      this.fetchCars()
    },
    // функция делает запрос на изменение машины
    async updateCar(id: number, car: any) {
      await api.put(`/cars/${id}`, car)
      this.fetchCars()
    },
    // функция делает запрос на удаление машины
    async deleteCar(id: number) {
      await api.delete(`/cars/${id}`)
      this.fetchCars()
    },

    // функция делает запрос на получение всех мест
    async fetchParkingSpaces() {
      const res = await api.get('/parking-spaces')
      this.parkingSpaces = res.data
    },
    // функция делает пост запрос на добавление парковочного места
    async addParkingSpace(space: any) {
      await api.post('/parking-spaces', space)
      this.fetchParkingSpaces()
    },
    // функция делает запрос на изменение парковочного места
    async updateParkingSpace(id: number, space: any) {
      await api.put(`/parking-spaces/${id}`, space)
      this.fetchParkingSpaces()
    },
    // функция делает запрос на удаление парковочного места
    async deleteParkingSpace(id: number) {
      await api.delete(`/parking-spaces/${id}`)
      this.fetchParkingSpaces()
    }
  }
})
