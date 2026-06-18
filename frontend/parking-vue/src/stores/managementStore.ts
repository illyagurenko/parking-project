import { defineStore } from 'pinia'
import { api } from './api'

export const useManagementStore = defineStore('management', {
  //state - переменные, массивы для сущностей в таблицы, данные для пагинации и фильтрации
  state: () => ({
    clients: [] as any[],
    totalClients: 0,
    clientsPage: 0,
    clientsSize: 10,
    clientsSearch: '',

    cars: [] as any[],
    totalCars: 0,
    carsPage: 0,
    carsSize: 10,
    carsSearch: '',

    parkingSpaces: [] as any[],
    totalParkingSpaces: 0,
    parkingSpacesPage: 0,
    parkingSpacesSize: 10,
    parkingSpacesSearch: '',

    clientsOptions: [] as any[],
    carsOptions: [] as any[],
    parkingSpacesOptions: [] as any[]
  }),
  actions: {
    // для выпадающих списков при создании сущностей
    async fetchClientsOptions() {
      const res = await api.get('/clients', { params: { size: 100 } })
      this.clientsOptions = res.data.content
    },
    async fetchCarsOptions() {
      const res = await api.get('/cars', { params: { size: 100 } })
      this.carsOptions = res.data.content
    },
    async fetchParkingSpacesOptions() {
      const res = await api.get('/parking-spaces', { params: { size: 100 } })
      this.parkingSpacesOptions = res.data.content
    },

    //основная таблица клиентов
    async fetchClients(search?: string, page?: number, size?: number) {
      if (search !== undefined) this.clientsSearch = search
      if (page !== undefined) this.clientsPage = page
      if (size !== undefined) this.clientsSize = size

      const res = await api.get('/clients', {
        params: {
          name: this.clientsSearch,
          page: this.clientsPage,
          size: this.clientsSize
        }
      })
      this.clients = res.data.content
      this.totalClients = res.data.totalElements
    },
    //crud с клиентами + обновление
    async addClient(client: any) {
      await api.post('/clients', client)
      this.fetchClients()
      this.fetchClientsOptions()
    },
    async updateClient(id: number, client: any) {
      await api.put(`/clients/${id}`, client)
      this.fetchClients()
      this.fetchClientsOptions()
    },
    async deleteClient(id: number) {
      await api.delete(`/clients/${id}`)
      this.fetchClients()
      this.fetchClientsOptions()
    },

    //основная таблица машин
    async fetchCars(search?: string, page?: number, size?: number) {
      if (search !== undefined) this.carsSearch = search
      if (page !== undefined) this.carsPage = page
      if (size !== undefined) this.carsSize = size

      const res = await api.get('/cars', {
        params: {
          search: this.carsSearch,
          page: this.carsPage,
          size: this.carsSize
        }
      })
      this.cars = res.data.content
      this.totalCars = res.data.totalElements
    },
    //crud с машинами + обновление
    async addCar(car: any) {
      await api.post('/cars', car)
      this.fetchCars()
      this.fetchCarsOptions()
    },
    async updateCar(id: number, car: any) {
      await api.put(`/cars/${id}`, car)
      this.fetchCars()
      this.fetchCarsOptions()
    },
    async deleteCar(id: number) {
      await api.delete(`/cars/${id}`)
      this.fetchCars()
      this.fetchCarsOptions()
    },

    //основная таблица мест
    async fetchParkingSpaces(search?: string, page?: number, size?: number) {
      if (search !== undefined) this.parkingSpacesSearch = search
      if (page !== undefined) this.parkingSpacesPage = page
      if (size !== undefined) this.parkingSpacesSize = size

      const res = await api.get('/parking-spaces', {
        params: {
          search: this.parkingSpacesSearch,
          page: this.parkingSpacesPage,
          size: this.parkingSpacesSize
        }
      })
      this.parkingSpaces = res.data.content
      this.totalParkingSpaces = res.data.totalElements
    },
    //crud с клиентами + обновление
    async addParkingSpace(space: any) {
      await api.post('/parking-spaces', space)
      this.fetchParkingSpaces()
      this.fetchParkingSpacesOptions()
    },
    async updateParkingSpace(id: number, space: any) {
      await api.put(`/parking-spaces/${id}`, space)
      this.fetchParkingSpaces()
      this.fetchParkingSpacesOptions()
    },
    async deleteParkingSpace(id: number) {
      await api.delete(`/parking-spaces/${id}`)
      this.fetchParkingSpaces()
      this.fetchParkingSpacesOptions()
    }
  }
})