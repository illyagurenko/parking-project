import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import type { Client } from '../types';

const API_URL = 'http://localhost:8080/api/parking';


export const useParkingStore = defineStore('parkingStore', () => {
  const spaces = ref<Client[]>([]);

  async function fetchSpaces() {
    const response = await axios.get(API_URL)
    spaces.value = response.data
  }

  async function addSpace(numberSpace: string) {
    if (!numberSpace.trim()) return
            await axios.post(API_URL, {
            numberSpace: numberSpace
        })
    await fetchSpaces()
  }

  async function deleteSpace(id: number) {
    await axios.delete(`${API_URL}/${id}`)
    await fetchSpaces()
  }

  return { spaces, fetchSpaces, addSpace, deleteSpace }
})