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

    async function addCars(numberCar: string) {
        if(!numberCar.trim()) return 
         await axios.post(API_URL, {
            numberCar: numberCar,
            clientId: 1 
        })

        await fetchCars();
    }
    return { cars, fetchCars, addCars };
})