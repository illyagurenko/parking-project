import { defineStore } from 'pinia'
import { api } from './api'

// объявление хранилища и реактивных переменных пока просто any
export const useManagementStore = defineStore('management', {
  state: () => ({
    clients: [] as any[],
    cars: [] as any[],
    parkingSpaces: [] as any[]
  }),
  actions: {
    // запрос на получение списка клиентов
    async fetchClients(search = '') {
      const res = await api.get('/clients', { params: { search } })
      this.clients = res.data
    },
    // пост запрос на добавление клиента
    async addClient(client: any) {
      await api.post('/clients', client)
      this.fetchClients()
    },
    // запрос на изменение клиента
    async updateClient(id: number, client: any) {
      await api.put(`/clients/${id}`, client)
      this.fetchClients()
    },
    // запрос на удаление клиента
    async deleteClient(id: number) {
      await api.delete(`/clients/${id}`)
      this.fetchClients()
    },
    // запрос на получение списка машин
    async fetchCars(search = '') {
      const res = await api.get('/cars', { params: { search } })
      this.cars = res.data
    },
    // пост запрос на добавление машины
    async addCar(car: any) {
      await api.post('/cars', car)
      this.fetchCars()
    },
    // запрос на изменение машины
    async updateCar(id: number, car: any) {
      await api.put(`/cars/${id}`, car)
      this.fetchCars()
    },
    // запрос на удаление машины
    async deleteCar(id: number) {
      await api.delete(`/cars/${id}`)
      this.fetchCars()
    },

    // запрос на получение всех мест
    async fetchParkingSpaces() {
      const res = await api.get('/parking-spaces')
      this.parkingSpaces = res.data
    },
    // пост запрос на добавление парковочного места
    async addParkingSpace(space: any) {
      await api.post('/parking-spaces', space)
      this.fetchParkingSpaces()
    },
    // запрос на изменение парковочного места
    async updateParkingSpace(id: number, space: any) {
      await api.put(`/parking-spaces/${id}`, space)
      this.fetchParkingSpaces()
    },
    // запрос на удаление парковочного места
    async deleteParkingSpace(id: number) {
      await api.delete(`/parking-spaces/${id}`)
      this.fetchParkingSpaces()
    }
  }
})
