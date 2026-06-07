<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import type{ 
  Client, 
  Car,
  CarDto,
  ParkingSpace, 
  Reservation,
  ReservationDto
} from './stores/parkingStore'

import { useParkingStore } from './stores/parkingStore'

import Button from 'primevue/button'
import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Dialog from 'primevue/dialog'
import Dropdown from 'primevue/dropdown'
import Checkbox from 'primevue/checkbox'
import InputSwitch from 'primevue/inputswitch'

const store = useParkingStore()
const activePage = ref(1)

// Фильтры и поиск
const searchClient = ref('')
const searchCar = ref('')
const searchBooking = ref('')

// Управление отображением модалок
const clientDialog = ref(false)
const carDialog = ref(false)
const spaceDialog = ref(false)
const bookingDialog = ref(false)

// Формы
const clientForm = reactive<Client>({ fullName: '' })
const carForm = reactive<Car>({ id: undefined, numberCar: '', clientId: undefined })
const spaceForm = reactive<ParkingSpace>({ numberSpace: '' })
const bookingForm = reactive<Reservation>({ id: undefined, parkingId: 0, carId: 0, isPaid: false })

onMounted(() => {
  store.fetchClients()
  store.fetchCars()
  store.fetchSpaces()
  store.fetchReservations()
  store.fetchAvailableSpaces()
})

// Поиск по ФИО и Госномеру
const onSearchClient = () => store.fetchClients(searchClient.value)
const onSearchCar = () => store.fetchCars(searchCar.value)
const onSearchBooking = () => store.fetchReservations(searchBooking.value)

// Клиент CRUD операции
const openClientDialog = (data?: Client) => {
  if (data) {
    Object.assign(clientForm, data)
  } else {
    clientForm.id = undefined
    clientForm.fullName = ''
  }
  clientDialog.value = true
}
const saveClient = async () => {
  if (clientForm.id) {
    await store.updateClient(clientForm)
  } else {
    await store.createClient(clientForm)
  }
  clientDialog.value = false
}
const deleteClient = (id: number) => {
  if (confirm('Вы действительно хотите удалить клиента?')) {
    store.deleteClient(id)
  }
}

// Авто CRUD
const openCarDialog = (data?: CarDto) => {
  if (data) {
    carForm.id = data.id
    carForm.numberCar = data.numberCar
    carForm.clientId = data.clientId
    // Поле clientName сознательно игнорируется, так как сервер его не ждет
  } else {
    carForm.id = undefined
    carForm.numberCar = ''
    carForm.clientId = undefined
  }
  carDialog.value = true
}
const saveCar = async () => {
  if (carForm.id) {
    await store.updateCar(carForm)
  } else {
    await store.createCar(carForm)
  }
  carDialog.value = false
}
const deleteCar = (id: number) => {
  if (confirm('Вы действительно хотите удалить автомобиль?')) {
    store.deleteCar(id)
  }
}

// Парковочное место CRUD
const openSpaceDialog = (data?: ParkingSpace) => {
  if (data) {
    Object.assign(spaceForm, data)
  } else {
    spaceForm.id = undefined
    spaceForm.numberSpace = 'N_'
  }
  spaceDialog.value = true
}
const saveSpace = async () => {
  if (!spaceForm.numberSpace.startsWith('N_')) {
    alert('Номер места должен начинаться с N_ согласно правилам валидации!')
    return
  }
  if (spaceForm.id) {
    await store.updateSpace(spaceForm)
  } else {
    await store.createSpace(spaceForm)
  }
  spaceDialog.value = false
}
const deleteSpace = (id: number) => {
  if (confirm('Вы действительно хотите удалить место?')) {
    store.deleteSpace(id)
  }
}

// Бронирование
const openBookingDialog = () => {
  store.fetchAvailableSpaces()
  store.fetchCars()
  bookingForm.id = undefined
  bookingForm.parkingId = 0
  bookingForm.carId = 0
  bookingForm.isPaid = false
  bookingDialog.value = true
}
const saveBooking = async () => {
  if (!bookingForm.parkingId || !bookingForm.carId) {
    alert('Выберите автомобиль и место!')
    return
  }
  await store.createReservation(bookingForm)
  bookingDialog.value = false
}
const togglePayment = (res: ReservationDto) => {
  if (res.id) store.togglePayment(res.id, res.isPaid)
}
const releaseSpace = (id: number) => {
  if (confirm('Освободить парковочное место?')) {
    store.releaseReservation(id)
  }
}

// Утилита
const formatDate = (dateStr?: string) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('ru-RU', { hour12: false })
}
</script>


<template>
  <div class="p-4" style="font-family: var(--font-family);">
    <h2 class="text-center mb-4">Администрирование автостоянки</h2>
    
    <div class="flex justify-content-center mb-4">
      <span class="p-buttonset">
        <Button 
          label="Учет (Клиенты, Авто, Места)" 
          :severity="activePage === 1 ? 'primary' : 'secondary'"
          @click="activePage = 1" 
        />
        <Button 
          label="Бронирование и Оплата" 
          :severity="activePage === 2 ? 'primary' : 'secondary'"
          @click="activePage = 2" 
        />
      </span>
    </div>

    <!-- Страница 1: Учет мест, автомобилей, владельцев -->
    <div v-if="activePage === 1" class="grid">
      <!-- Блок Клиенты -->
      <div class="col-12 md:col-4">
        <Card>
          <template #title>Клиенты</template>
          <template #content>
            <div class="flex gap-2 mb-3">
              <InputText v-model="searchClient" placeholder="Поиск по ФИО" class="w-full" @input="onSearchClient" />
              <Button icon="pi pi-plus" severity="success" @click="openClientDialog()" />
            </div>
            <DataTable :value="store.clients" class="p-datatable-sm" responsiveLayout="scroll">
              <Column field="fullName" header="ФИО"></Column>
              <Column header="Действия" style="width: 100px">
                <template #body="slotProps">
                  <div class="flex gap-1">
                    <Button icon="pi pi-pencil" severity="warning" text @click="openClientDialog(slotProps.data)" />
                    <Button icon="pi pi-trash" severity="danger" text @click="deleteClient(slotProps.data.id)" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </template>
        </Card>
      </div>

      <!-- Блок Автомобили -->
      <div class="col-12 md:col-4">
        <Card>
          <template #title>Автомобили</template>
          <template #content>
            <div class="flex gap-2 mb-3">
              <InputText v-model="searchCar" placeholder="Поиск по госномеру" class="w-full" @input="onSearchCar" />
              <Button icon="pi pi-plus" severity="success" @click="openCarDialog()" />
            </div>
            <DataTable :value="store.cars" class="p-datatable-sm" responsiveLayout="scroll">
              <Column field="numberCar" header="Госномер"></Column>
              <Column field="clientName" header="Владелец"></Column>
              <Column header="Действия" style="width: 100px">
                <template #body="slotProps">
                  <div class="flex gap-1">
                    <Button icon="pi pi-pencil" severity="warning" text @click="openCarDialog(slotProps.data)" />
                    <Button icon="pi pi-trash" severity="danger" text @click="deleteCar(slotProps.data.id)" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </template>
        </Card>
      </div>

      <!-- Блок Парковочные места -->
      <div class="col-12 md:col-4">
        <Card>
          <template #title>Парковочные места</template>
          <template #content>
            <div class="flex gap-2 mb-3">
              <span class="w-full text-xs text-color-secondary flex align-items-center">Формат имени места: N_[номер]</span>
              <Button icon="pi pi-plus" severity="success" @click="openSpaceDialog()" />
            </div>
            <DataTable :value="store.spaces" class="p-datatable-sm" responsiveLayout="scroll">
              <Column field="numberSpace" header="Номер места"></Column>
              <Column header="Действия" style="width: 100px">
                <template #body="slotProps">
                  <div class="flex gap-1">
                    <Button icon="pi pi-pencil" severity="warning" text @click="openSpaceDialog(slotProps.data)" />
                    <Button icon="pi pi-trash" severity="danger" text @click="deleteSpace(slotProps.data.id)" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </template>
        </Card>
      </div>
    </div>

    <!-- Страница 2: Бронирование/освобождение и оплата -->
    <div v-if="activePage === 2" class="grid justify-content-center">
      <div class="col-12 lg:col-10">
        <Card>
          <template #title>Активные бронирования</template>
          <template #content>
            <div class="flex justify-content-between gap-3 mb-4 flex-wrap">
              <div class="flex gap-2">
                <InputText v-model="searchBooking" placeholder="Поиск по госномеру или ФИО" style="width: 300px" @input="onSearchBooking" />
              </div>
              <Button label="Оформить бронирование" icon="pi pi-plus" severity="success" @click="openBookingDialog" />
            </div>

            <DataTable :value="store.reservations" responsiveLayout="scroll">
              <Column field="parkingSpaceNumber" header="Место"></Column>
              <Column field="carNumber" header="Госномер автомобиля"></Column>
              <Column field="clientFullName" header="ФИО владельца"></Column>
              <Column field="startTime" header="Время заезда">
                <template #body="slotProps">
                  {{ formatDate(slotProps.data.startTime) }}
                </template>
              </Column>
              <Column field="isPaid" header="Оплата">
                <template #body="slotProps">
                  <div class="flex align-items-center gap-2">
                    <InputSwitch 
                      v-model="slotProps.data.isPaid" 
                      @change="togglePayment(slotProps.data)" 
                    />
                    <span :class="slotProps.data.isPaid ? 'text-green-600' : 'text-red-600'">
                      {{ slotProps.data.isPaid ? 'Оплачено' : 'Долг' }}
                    </span>
                  </div>
                </template>
              </Column>
              <Column header="Действия">
                <template #body="slotProps">
                  <Button label="Освободить место" severity="danger" text size="small" icon="pi pi-sign-out" @click="releaseSpace(slotProps.data.id)" />
                </template>
              </Column>
            </DataTable>
          </template>
        </Card>
      </div>
    </div>

    <!-- Диалоговые окна CRUD (PrimeVue Dialog) -->
    
    <!-- Клиент -->
    <Dialog v-model:visible="clientDialog" header="Редактирование клиента" modal :style="{width: '450px'}">
      <div class="field mt-2">
        <label for="fullName">ФИО клиента</label>
        <InputText id="fullName" v-model="clientForm.fullName" required autofocus class="w-full mt-2" />
      </div>
      <template #footer>
        <Button label="Отмена" icon="pi pi-times" text @click="clientDialog = false" />
        <Button label="Сохранить" icon="pi pi-check" severity="success" @click="saveClient" />
      </template>
    </Dialog>

    <!-- Автомобиль -->
    <Dialog v-model:visible="carDialog" header="Редактирование автомобиля" modal :style="{width: '450px'}">
      <div class="field mt-2">
        <label for="numberCar">Госномер (до 9 символов)</label>
        <InputText id="numberCar" v-model="carForm.numberCar" required max-length="9" class="w-full mt-2" />
      </div>
      <div class="field mt-3">
        <label for="clientSelect" class="block mb-2">Владелец</label>
        <Dropdown id="clientSelect" v-model="carForm.clientId" :options="store.clients" optionValue="id" optionLabel="fullName" placeholder="Выберите владельца" class="w-full" />
      </div>
      <template #footer>
        <Button label="Отмена" icon="pi pi-times" text @click="carDialog = false" />
        <Button label="Сохранить" icon="pi pi-check" severity="success" @click="saveCar" />
      </template>
    </Dialog>

    <!-- Парковочное место -->
    <Dialog v-model:visible="spaceDialog" header="Редактирование места" modal :style="{width: '450px'}">
      <div class="field mt-2">
        <label for="numberSpace">Номер места (обязателен префикс N_)</label>
        <InputText id="numberSpace" v-model="spaceForm.numberSpace" placeholder="N_10" required class="w-full mt-2" />
      </div>
      <template #footer>
        <Button label="Отмена" icon="pi pi-times" text @click="spaceDialog = false" />
        <Button label="Сохранить" icon="pi pi-check" severity="success" @click="saveSpace" />
      </template>
    </Dialog>

    <!-- Создание Бронирования -->
    <Dialog v-model:visible="bookingDialog" header="Новое бронирование" modal :style="{width: '450px'}">
      <div class="field mt-2">
        <label for="spaceSelect" class="block mb-2">Свободные места (только с N_)</label>
        <Dropdown id="spaceSelect" v-model="bookingForm.parkingId" :options="store.availableSpaces" optionValue="id" optionLabel="numberSpace" placeholder="Выберите место" class="w-full" />
      </div>
      <div class="field mt-3">
        <label for="carSelect" class="block mb-2">Автомобиль</label>
        <Dropdown id="carSelect" v-model="bookingForm.carId" :options="store.cars" optionValue="id" optionLabel="numberCar" placeholder="Выберите автомобиль" class="w-full" />
      </div>
      <div class="field mt-3 flex align-items-center gap-2">
        <Checkbox v-model="bookingForm.isPaid" binary id="isPaidBook" />
        <label for="isPaidBook">Сразу оплатить</label>
      </div>
      <template #footer>
        <Button label="Отмена" icon="pi pi-times" text @click="bookingDialog = false" />
        <Button label="Забронировать" icon="pi pi-check" severity="success" @click="saveBooking" />
      </template>
    </Dialog>
  </div>
</template>



<style>
/* Подключение Flexbox утилит от PrimeFlex (если не установлены локально, можно использовать базовый CSS) */
.flex { display: flex; }
.flex-column { flex-direction: column; }
.justify-content-center { justify-content: center; }
.justify-content-between { justify-content: space-between; }
.align-items-center { align-items: center; }
.gap-1 { gap: 0.25rem; }
.gap-2 { gap: 0.5rem; }
.gap-3 { gap: 1rem; }
.grid { display: flex; flex-wrap: wrap; margin-right: -0.5rem; margin-left: -0.5rem; margin-top: -0.5rem; }
.col-12 { width: 100%; padding: 0.5rem; }
@media (min-width: 768px) {
  .md\\:col-4 { width: 33.3333%; }
}
@media (min-width: 992px) {
  .lg\\:col-10 { width: 83.3333%; }
}
.w-full { width: 100%; }
.text-center { text-align: center; }
.mb-4 { margin-bottom: 1.5rem; }
.mb-3 { margin-bottom: 1rem; }
.mt-2 { margin-top: 0.5rem; }
.mt-3 { margin-top: 1rem; }
.field { margin-bottom: 1rem; }
</style>