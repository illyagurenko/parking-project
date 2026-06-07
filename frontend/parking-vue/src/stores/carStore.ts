import { defineStore } from "pinia";
import { ref } from 'vue';
import axios from 'axios';
import type { Car } from '../types';

const API_URL = 'http://localhost:8080/api/cars';

export const useCarStore = defineStore('car', () => {
    const cars = ref<Car[]>([]);
    
    // const carCount = computed(() => {
    //     return todos.value.length;

    async function fetchCars() {
        const response = await axios.get(API_URL)
        cars.value = response.data
    }

    async function fetchCarsByNumber(numberCar: string) {
        try{
            const response = await axios.get(`${API_URL}/${numberCar}`)
            if (response.data) {
                // Оборачиваем один объект в квадратные скобки [ ... ], чтобы сделать массивом
                cars.value = [response.data] 
            } else {
                // Если сервер вернул пустоту (null)
                cars.value = [] 
            }
        } catch (error) {
            console.error("Ошибка поиска:", error)
            cars.value = [] // В случае ошибки тоже отдаем пустой массив
        }
    }

    async function addCars(numberCar: string, clientId: number) {
        if(!numberCar.trim()) return 
         await axios.post(API_URL, {
            numberCar: numberCar,
            clientId: clientId 
        })

        await fetchCars();
    }

        async function deleteCar(id: number) {
        await axios.delete(`${API_URL}/${id}`)
        cars.value = cars.value.filter(t => t.id !== id)
        
    }
    return { cars, fetchCars, fetchCarsByNumber, addCars, deleteCar };
})