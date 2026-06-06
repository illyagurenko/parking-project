<template>
  <div style="padding: 20px; font-family: sans-serif;">
    <h2>Тест хранилища машин</h2>

    <!-- Форма добавления -->
    <div style="margin-bottom: 20px;">
      <input 
        v-model="newCarInput" 
        placeholder="Введите номер авто" 
        @keyup.enter="addNewCar"
      />
      <button @click="addNewCar">Добавить в БД</button>
    </div>

    <!-- Вывод списка из Pinia -->
    <div v-if="carStore.cars.length === 0">
      Машин пока нет или данные загружаются...
    </div>
    
    <ul v-else>
      <li v-for="car in carStore.cars" :key="car.id">
        ID: {{ car.id }} | Номер: {{ car.numberCar }} | ID Владельца: {{ car.clientId }}
      </li>
    </ul>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
// Укажи правильный путь до твоего файла стора!
import { useCarStore } from './stores/carStore'; 

const carStore = useCarStore(); // Инициализируем стор Pinia
const newCarInput = ref('');    // Переменная для инпута

// Функция добавления по кнопке
const addNewCar = async () => {
  await carStore.addCars(newCarInput.value); // Вызываем экшен из стора
  newCarInput.value = ''; // Очищаем поле ввода после успеха
};

// onMounted срабатывает, когда страница только открылась
onMounted(() => {
  carStore.fetchCars(); // Сразу дергаем бэкенд, чтобы заполнить список
});
</script>