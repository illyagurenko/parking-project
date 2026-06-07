import { defineStore } from "pinia";
import { ref } from 'vue';
import axios from 'axios';
import type { CarWithClient } from '../types';
import type { AddCarWithClientPayload } from '../types';

const API_URL = 'http://localhost:8080/api/cars';

export const useCarStore = defineStore('car', () => {
    const cars = ref<CarWithClient[]>([]);  // ← другой тип!

    async function fetchCars() {
        const response = await axios.get(API_URL);
        cars.value = response.data;  // уже содержит clientFullName
    }

    async function fetchCarsByNumber(numberCar: string) {
        try {
            const response = await axios.get(`${API_URL}/${numberCar}`);
            cars.value = response.data ? [response.data] : [];
        } catch (error) {
            cars.value = [];
        }
    }

    async function addCars(numberCar: string, clientId: number) {
        await axios.post(API_URL, { numberCar, clientId });
        await fetchCars();
    }

    async function updateCar(id: number, numberCar: string) {
        await axios.put(`${API_URL}/${id}`, { numberCar });
        await fetchCars();
    }

    async function deleteCar(id: number) {
        await axios.delete(`${API_URL}/${id}`);
        cars.value = cars.value.filter(c => c.id !== id);
    }
    async function addCarWithClient(payload: AddCarWithClientPayload) {
        await axios.post(`${API_URL}/with-client`, payload);
        await fetchCars();  // обновляем список
    }

    async function fetchCarsByOwnerName(name: string) {
    const response = await axios.get(`${API_URL}/search-by-owner`, {
        params: { name }
    });
    cars.value = response.data;
}

    return { cars, fetchCars, fetchCarsByNumber, addCars, updateCar, deleteCar, addCarWithClient, fetchCarsByOwnerName };
});