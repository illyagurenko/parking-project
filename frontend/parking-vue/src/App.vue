<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useCarStore } from './stores/carStore' // Укажите правильный путь к вашему файлу стора

// Импортируем компоненты PrimeVue
import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import Button from 'primevue/button'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'

// Подключаем наш стор
const carStore = useCarStore()

// Локальные переменные для формы добавления
const carNumber = ref<string>('')
const clientId = ref<number | null>(null)

// При монтировании страницы запрашиваем список машин с сервера
onMounted(() => {
  carStore.fetchCars()
})

// Обработчик отправки формы
const onAddCar = async () => {
  // Проверяем, что поля заполнены
  if (carNumber.value.trim() && clientId.value !== null) {
    // Передаем в метод стора номер машины и ID клиента
    await carStore.addCars(carNumber.value, clientId.value)
    
    // Очищаем поля формы после добавления
    carNumber.value = ''
    clientId.value = null
  }
}
</script>

<template>
  <div class="container">
    <Card>
      <template #title>
        <h2>Управление автомобилями</h2>
      </template>
      
      <template #content>
        <!-- Форма для добавления автомобиля -->
        <div class="car-form">
          <div class="input-group">
            <label for="car-number">Номер машины</label>
            <InputText 
              id="car-number" 
              v-model="carNumber" 
              placeholder="Например, А777АА" 
            />
          </div>

          <div class="input-group">
            <label for="client-id">ID Клиента</label>
            <InputNumber 
              id="client-id" 
              v-model="clientId" 
              placeholder="Введите ID" 
              :useGrouping="false"
            />
          </div>

          <Button 
            label="Добавить" 
            icon="pi pi-plus" 
            @click="onAddCar" 
            :disabled="!carNumber.trim() || clientId === null"
          />
        </div>

        <!-- Таблица для вывода списка машин -->
        <DataTable :value="carStore.cars" class="p-datatable-sm" stripedRows>
          <Column field="id" header="ID автомобиля" />
          <Column field="numberCar" header="Гос. номер" />
          <Column field="clientId" header="ID Владельца (Клиента)" />
          <Column header="Действие">
            <template #body="{ data }">
              <Button 
                icon="pi pi-trash" 
                severity="danger" 
                text 
                rounded 
                @click="carStore.deleteCar(data.id)" 
              />
            </template>
          </Column>
        </DataTable>
      </template>
    </Card>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 3rem auto;
  padding: 0 1rem;
}

.car-form {
  display: flex;
  gap: 1rem;
  align-items: flex-end;
  margin-bottom: 2rem;
  background: var(--p-content-background);
  padding: 1rem;
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-content-border-radius);
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  flex: 1;
}

.input-group label {
  font-size: 0.85rem;
  color: var(--p-text-color-secondary);
}
</style>