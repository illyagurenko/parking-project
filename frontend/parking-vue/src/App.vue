<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useCarStore } from './stores/carStore' 
import { useClientStore } from './stores/clientStore'

// Компоненты PrimeVue
import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'

const carStore = useCarStore()
const clientStore = useClientStore()

// Локальные переменные для формы добавления
const carNumber = ref<string>('')
const fullName = ref<string>('')

// Загрузка данных при монтировании
onMounted(() => {
  carStore.fetchCars()
  clientStore.fetchClients()
})

// Вычисляемое свойство для объединения данных машин и клиентов
const combinedCarsData = computed(() => {
  return carStore.cars.map(car => {
    // Ищем клиента, у которого id совпадает с clientId из машины
    const client = clientStore.clients.find(c => c.id === car.clientId)
    return {
      ...car,
      // Если клиент найден, берем его ФИО, иначе выводим заглушку
      clientFullName: client ? client.fullName : 'Неизвестный клиент'
    }
  })
})

// Обработчик отправки формы
const onAddClientAndCar = async () => {
  // Проверяем, что оба поля заполнены
  if (!fullName.value.trim() || !carNumber.value.trim()) return

  try {
    // 1. Создаем клиента (метод сам вызовет fetchClients внутри себя)
    await clientStore.addClients(fullName.value.trim())

    // 2. Ищем только что созданного клиента в обновленном массиве стора
    const createdClient = clientStore.clients.find(
      c => c.fullName.toLowerCase() === fullName.value.trim().toLowerCase()
    )

    if (createdClient && createdClient.id) {
      // 3. Создаем машину, привязывая её к ID найденного клиента
      await carStore.addCars(carNumber.value.trim(), createdClient.id)
      
      // Очищаем поля формы после успешного добавления всего процесса
      carNumber.value = ''
      fullName.value = ''
    } else {
      console.error('Не удалось найти созданного клиента для привязки автомобиля')
    }
  } catch (error) {
    console.error('Ошибка при добавлении данных:', error)
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
        <!-- Форма для добавления автомобиля и клиента -->
        <div class="car-form">
          <div class="input-group">
            <label for="full-name">ФИО владельца</label>
            <InputText
              id="full-name" 
              v-model="fullName" 
              placeholder="Введите ФИО" 
            />
          </div>

          <div class="input-group">
            <label for="car-number">Номер машины</label>
            <InputText 
              id="car-number" 
              v-model="carNumber" 
              placeholder="Например, А777АА" 
            />
          </div>

          <Button 
            label="Добавить" 
            icon="pi pi-plus" 
            @click="onAddClientAndCar" 
            :disabled="!carNumber.trim() || !fullName.trim()"
          />
        </div>

        <!-- Таблица для вывода объединенного списка -->
        <DataTable :value="combinedCarsData" class="p-datatable-sm" stripedRows>
          <Column field="id" header="ID автомобиля" />
          <Column field="numberCar" header="Гос. номер" />
          <Column field="clientFullName" header="ФИО Владельца" />
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
