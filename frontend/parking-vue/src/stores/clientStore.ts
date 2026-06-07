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
        const response = await axios.get(`${API_URL}/${fullName}`)
        clients.value = response.data
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