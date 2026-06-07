import { defineStore } from "pinia";
import { ref } from 'vue';
import axios from 'axios';
import type { Client } from '../types';

const API_URL = 'http://localhost:8080/api/clients';

export const useClientStore = defineStore('client', () => {
    const clients = ref<Client[]>([]);


 async function fetchClients() {
        const response = await axios.get(API_URL)
        clients.value = response.data
    }

    async function fetchClientByFullName(fullName: string) {
        try{
            const response = await axios.get(`${API_URL}/${fullName}`)
            if (response.data) {
                // Оборачиваем один объект в квадратные скобки [ ... ], чтобы сделать массивом
                clients.value = [response.data] 
            } else {
                // Если сервер вернул пустоту (null)
                clients.value = [] 
            }
        } catch (error) {
            console.error("Ошибка поиска:", error)
            clients.value = [] // В случае ошибки тоже отдаем пустой массив
        }
    }

    async function addClients(fullName: string) {
        if(!fullName.trim()) return 
         await axios.post(API_URL, {
            fullName: fullName
        })

        await fetchClients();
    }

    async function deleteClient(id: number) {
        await axios.delete(`${API_URL}/${id}`)
        clients.value = clients.value.filter(t => t.id !== id)
        
    }
    return { clients, fetchClients, fetchClientByFullName, addClients, deleteClient };
})