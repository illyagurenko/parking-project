import { defineStore } from 'pinia'
import { api } from './api'

export const useManagementStore = defineStore('management', {
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
    async fetchClientsOptions() {
      try {
        const res = await api.get('/clients', { params: { size: 100 } })
        this.clientsOptions = res.data.content
      } catch (error: any) {
        console.error('Ошибка загрузки списка клиентов:', error)
      }
    },
    async fetchCarsOptions() {
      try {
        const res = await api.get('/cars', { params: { size: 100 } })
        this.carsOptions = res.data.content
      } catch (error: any) {
        console.error('Ошибка загрузки списка машин:', error)
      }
    },
    async fetchParkingSpacesOptions() {
      try {
        const res = await api.get('/parking-spaces', { params: { size: 100 } })
        this.parkingSpacesOptions = res.data.content
      } catch (error: any) {
        console.error('Ошибка загрузки списка парковочных мест:', error)
      }
    },

    async fetchClients(search?: string, page?: number, size?: number) {
      if (search !== undefined) this.clientsSearch = search
      if (page !== undefined) this.clientsPage = page
      if (size !== undefined) this.clientsSize = size

      try {
        const res = await api.get('/clients', {
          params: {
            name: this.clientsSearch,
            page: this.clientsPage,
            size: this.clientsSize
          }
        })
        this.clients = res.data.content
        this.totalClients = res.data.totalElements
      } catch (error: any) {
        console.error('Не удалось загрузить клиентов', error)
      }
    },
    async addClient(client: any) {
      try {
        await api.post('/clients', client)
        this.fetchClients()
        this.fetchClientsOptions()
      } catch (error: any) {
        console.error('Не удалось добавить клиента', error)
      }
    },
    async updateClient(id: number, client: any) {
      try {
        await api.put(`/clients/${id}`, client)
        this.fetchClients()
        this.fetchClientsOptions()
      } catch (error: any) {
        console.error('Не удалось обновить клиента', error)
      }
    },
    async deleteClient(id: number) {
      try {
        await api.delete(`/clients/${id}`)
        this.fetchClients()
        this.fetchClientsOptions()
      } catch (error: any) {
        console.error('Не удалось удалить клиента', error)
      }
    },

    async fetchCars(search?: string, page?: number, size?: number) {
      if (search !== undefined) this.carsSearch = search
      if (page !== undefined) this.carsPage = page
      if (size !== undefined) this.carsSize = size

      try {
        const res = await api.get('/cars', {
          params: {
            search: this.carsSearch,
            page: this.carsPage,
            size: this.carsSize
          }
        })
        this.cars = res.data.content
        this.totalCars = res.data.totalElements
      } catch (error: any) {
        console.error('Не удалось загрузить автомобили', error)
      }
    },
    async addCar(car: any) {
      try {
        await api.post('/cars', car)
        this.fetchCars()
        this.fetchCarsOptions()
      } catch (error: any) {
        console.error('Не удалось добавить автомобиль', error)
      }
    },
    async updateCar(id: number, car: any) {
      try {
        await api.put(`/cars/${id}`, car)
        this.fetchCars()
        this.fetchCarsOptions()
      } catch (error: any) {
        console.error('Не удалось обновить автомобиль', error)
      }
    },
    async deleteCar(id: number) {
      try {
        await api.delete(`/cars/${id}`)
        this.fetchCars()
        this.fetchCarsOptions()
      } catch (error: any) {
        console.error('Не удалось удалить автомобиль', error)
      }
    },

    async fetchParkingSpaces(search?: string, page?: number, size?: number) {
      if (search !== undefined) this.parkingSpacesSearch = search
      if (page !== undefined) this.parkingSpacesPage = page
      if (size !== undefined) this.parkingSpacesSize = size

      try {
        const res = await api.get('/parking-spaces', {
          params: {
            search: this.parkingSpacesSearch,
            page: this.parkingSpacesPage,
            size: this.parkingSpacesSize
          }
        })
        this.parkingSpaces = res.data.content
        this.totalParkingSpaces = res.data.totalElements
      } catch (error: any) {
        console.error('Не удалось загрузить парковочные места', error)
      }
    },
    async addParkingSpace(space: any) {
      try {
        await api.post('/parking-spaces', space)
        this.fetchParkingSpaces()
        this.fetchParkingSpacesOptions()
      } catch (error: any) {
        console.error('Не удалось добавить парковочное место', error)
      }
    },
    async updateParkingSpace(id: number, space: any) {
      try {
        await api.put(`/parking-spaces/${id}`, space)
        this.fetchParkingSpaces()
        this.fetchParkingSpacesOptions()
      } catch (error: any) {
        console.error('Не удалось обновить парковочное место', error)
      }
    },
    async deleteParkingSpace(id: number) {
      try {
        await api.delete(`/parking-spaces/${id}`)
        this.fetchParkingSpaces()
        this.fetchParkingSpacesOptions()
      } catch (error: any) {
        console.error('Не удалось удалить парковочное место', error)
      }
    }
  }
})