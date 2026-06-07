<script setup lang="ts">
import { ref, computed } from 'vue'
import { useCarStore } from '../stores/carStore' 
import { useClientStore } from '../stores/clientStore'

import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Dialog from 'primevue/dialog'

const carStore = useCarStore()
const clientStore = useClientStore()

const carNumber = ref<string>('')
const fullName = ref<string>('')
const searchCarNumber = ref<string>('')
const searchFullName = ref<string>('')

  // Состояния для модального окна редактирования

const isEditDialogVisible = ref<boolean>(false)
const editingCarId = ref<number | null>(null)
const editCarOwnerName = ref<string>('')
const editCarNumber = ref<string>('')


const loadAllData = () => {
  carStore.fetchCars()
  clientStore.fetchClients()
}

// Логика объединения таблиц автомобилей и их владельцев
const combinedCarsData = computed(() => {
  if (!carStore.cars || !Array.isArray(carStore.cars)) return []
  return carStore.cars.map(car => {
    const client = clientStore.clients.find(c => c.id === car.clientId)
    return {
      ...car,
      clientFullName: client ? client.fullName : 'Неизвестный клиент'
    }
  })
})

const onEditCarClick = (car: any) => {
  editingCarId.value = car.id
  editCarNumber.value = car.numberCar
  editCarOwnerName.value = car.clientFullName // Записываем ФИО владельца для справки
  isEditDialogVisible.value = true
}

// Сохранение изменений
const onSaveCarEdit = async () => {
  if (!editCarNumber.value.trim() || !editingCarId.value) return

  try {
    // Вызываем метод стора, передавая только ID машины и её новый номер
    await carStore.updateCar(editingCarId.value, editCarNumber.value.trim())
    
    // Закрываем окно и сбрасываем ID
    isEditDialogVisible.value = false
    editingCarId.value = null
  } catch (error) {
    console.error('Ошибка при сохранении номера машины:', error)
  }
}

// Добавление автомобиля и клиента с проверкой на дубликаты ФИО
const onAddClientAndCar = async () => {
  if (!fullName.value.trim() || !carNumber.value.trim()) return

  const targetedName = fullName.value.trim()

  try {
    // 1. Поиск существующего клиента на бэкенде
    await clientStore.fetchClientByFullName(targetedName)
    
    let existingClient = clientStore.clients.find(
      c => c.fullName.toLowerCase() === targetedName.toLowerCase()
    )

    let finalClientId: number | null = existingClient ? existingClient.id : null

    // 2. Если клиент не найден, создаем новую запись
    if (!finalClientId) {
      await clientStore.addClients(targetedName)
      
      const newClient = clientStore.clients.find(
        c => c.fullName.toLowerCase() === targetedName.toLowerCase()
      )
      if (newClient) finalClientId = newClient.id
    }

    // 3. Создаем автомобиль с привязкой к ID клиента
    if (finalClientId) {
      await carStore.addCars(carNumber.value.trim(), finalClientId)
      
      // Очистка полей ввода
      carNumber.value = ''
      fullName.value = ''
      
      // Восстанавливаем полный список клиентов в сторе
      await clientStore.fetchClients()
    } else {
      console.error('Ошибка: не удалось определить ID клиента')
    }
  } catch (error) {
    console.error('Ошибка при обработке формы:', error)
  }
}

// Сброс результатов поиска к исходному списку
const onResetSearch = async () => {
  searchCarNumber.value = ''
  searchFullName.value = ''
  await loadAllData()
}

// Поиск автомобиля по государственному номеру
const onSearchCar = async () => {
  if (!searchCarNumber.value.trim()) {
    await carStore.fetchCars()
    return
  }
  await carStore.fetchCarsByNumber(searchCarNumber.value.trim())
}

// Поиск владельца по ФИО и фильтрация его автомобилей
const onSearchClient = async () => {
  const query = searchFullName.value.trim()
  
  if (!query) {
    await loadAllData()
    return
  }

  await clientStore.fetchClientByFullName(query)

  if (clientStore.clients.length > 0) {
    const foundClientId = clientStore.clients[0].id
    
    await carStore.fetchCars() 
    carStore.cars = carStore.cars.filter(car => car.clientId === foundClientId)
  } else {
    carStore.cars = []
    alert('Владелец с таким ФИО не найден!')
  }
}
</script>

<template>
  <Card class="management-card">
    <template #title>
      <h2>Управление автомобилями и владельцами</h2>
    </template>
    
    <template #content>
      <!-- Форма добавления автомобиля и владельца -->
      <div class="form-grid">
        <div class="input-group">
          <label for="full-name">ФИО владельца</label>
          <InputText id="full-name" v-model="fullName" placeholder="Введите ФИО" />
        </div>

        <div class="input-group">
          <label for="car-number">Номер машины</label>
          <InputText id="car-number" v-model="carNumber" placeholder="Например, А777АА" />
        </div>

        <Button 
          label="Добавить" 
          icon="pi pi-plus" 
          @click="onAddClientAndCar" 
          :disabled="!carNumber.trim() || !fullName.trim()"
        />
      </div>

      <!-- Панель поиска -->
      <div class="search-bar-container">
        <!-- Поиск по номеру машины -->
        <div class="search-row">
          <div class="input-group search-input">
            <InputText 
              v-model="searchCarNumber" 
              placeholder="Поиск по гос. номеру (А777АА)..." 
              @keyup.enter="onSearchCar"
            />
          </div>
          <Button label="Найти авто" icon="pi pi-search" severity="secondary" @click="onSearchCar" />
        </div>

        <!-- Поиск по ФИО владельца -->
        <div class="search-row">
          <div class="input-group search-input">
            <InputText 
              v-model="searchFullName" 
              placeholder="Поиск по ФИО (Иванов И.И.)..." 
              @keyup.enter="onSearchClient"
            />
          </div>
          <Button label="Найти владельца" icon="pi pi-user" severity="secondary" @click="onSearchClient" />
        </div>

        <!-- Кнопка сброса всех фильтров -->
        <Button class="reset-btn" label="Сбросить все фильтры" icon="pi pi-refresh" severity="help" text @click="onResetSearch" />
      </div>

      <!-- Таблица автомобилей -->
<DataTable :value="combinedCarsData" class="p-datatable-sm" stripedRows>
        <Column field="id" header="ID автомобиля" />
        <Column field="numberCar" header="Гос. номер" />
        <Column field="clientFullName" header="ФИО Владельца" />
        <Column header="Действия">
          <template #body="{ data }">
            <div class="actions-cell">
              <!-- Кнопка редактирования -->
              <Button icon="pi pi-pencil" severity="warn" text rounded @click="onEditCarClick(data)" />
              <!-- Кнопка удаления -->
              <Button icon="pi pi-trash" severity="danger" text rounded @click="carStore.deleteCar(data.id)" />
            </div>
          </template>
        </Column>
      </DataTable>

      <!-- МОДАЛЬНОЕ ОКНО РЕДАКТИРОВАНИЯ -->
      <Dialog 
        v-model:visible="isEditDialogVisible" 
        modal 
        header="Редактирование данных" 
        :style="{ width: '25rem' }"
      >
  <div class="edit-dialog-form">
    <!-- Поле ФИО заблокировано для ввода (disabled) -->
    <div class="input-group">
      <label for="edit-full-name">Владелец автомобиля (нельзя изменить)</label>
      <InputText id="edit-full-name" v-model="editCarOwnerName" disabled class="full-width" />
    </div>
          <div class="input-group">
            <label for="edit-car-number">Номер машины</label>
            <InputText id="edit-car-number" v-model="editCarNumber" class="full-width" />
          </div>
          
          <div class="dialog-actions">
            <Button label="Отмена" severity="secondary" text @click="isEditDialogVisible = false" />
            <Button label="Сохранить" severity="success" @click="onSaveCarEdit" :disabled="!editCarNumber.trim()" />
          </div>
        </div>
      </Dialog>
    </template>
  </Card>
</template>

<style scoped>
.management-card {
  box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
}

.form-grid {
  display: flex;
  gap: 1rem;
  align-items: flex-end;
  margin-bottom: 1.5rem;
  background: var(--p-content-background);
  padding: 1rem;
  border: 1px solid var(--p-content-border-color);
  border-radius: var(--p-content-border-radius);
}

.search-input {
  flex: 1;
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
  font-weight: 500;
}

.search-bar-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
  background: var(--p-content-background);
  padding: 1rem;
  border-radius: var(--p-content-border-radius);
  border: 1px solid var(--p-content-border-color);
}

.search-row {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.reset-btn {
  align-self: flex-start;
  margin-top: 0.5rem;
}

.actions-cell {
  display: flex;
  gap: 0.5rem;
}

.edit-dialog-form {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
  padding-top: 0.5rem;
}

.full-width {
  width: 100%;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  margin-top: 1rem;
}
</style>