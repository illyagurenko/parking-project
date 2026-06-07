<script setup lang="ts">
import { onMounted } from 'vue'
import { useCarStore } from './stores/carStore' 
import { useClientStore } from './stores/clientStore'
import { useParkingStore } from './stores/parkingStore'

// Импортируем наши новые компоненты
import CarManagement from './components/CarManagement.vue'
import ParkingManagement from './components/ParkingManagement.vue'

const carStore = useCarStore()
const clientStore = useClientStore()
const parkingStore = useParkingStore()

// Единственное, что делает главный файл — загружает данные при открытии страницы
onMounted(() => {
  carStore.fetchCars()
  clientStore.fetchClients()
  parkingStore.fetchSpaces()
})
</script>

<template>
  <div class="container">
    <!-- Компонент 1 -->
    <CarManagement />
    
    <!-- Компонент 2 -->
    <ParkingManagement />
  </div>
</template>

<style scoped>
/* Оставляем только глобальные стили для контейнера страницы */
.container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 0 1rem;
  display: flex;
  flex-direction: column;
  gap: 2.5rem;
}
</style>