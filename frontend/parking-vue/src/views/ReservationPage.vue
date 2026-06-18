<template>
  <div class="reservation-page">
    <h2>Reservations</h2>

    <div class="card">
      <div class="card-header">
        <div class="filters">
          <InputText v-model="carSearch" placeholder="Search by Car Number" @input="fetchReservations" />
          <InputText v-model="clientSearch" placeholder="Search by Client Name" @input="fetchReservations" />
        </div>
        <Button icon="pi pi-plus" label="New Reservation" @click="openDialog()" />
      </div>

      <DataTable 
        :value="store.reservations" 
        lazy 
        paginator 
        :rows="store.size" 
        :totalRecords="store.totalReservations"
        :first="store.page * store.size"
        @page="onPage"
        class="p-datatable-sm" 
        responsiveLayout="scroll"
      >
        <Column field="id" header="ID"></Column>
        <Column field="numberSpace" header="Space"></Column>
        <Column field="numberCar" header="Car"></Column>
        <Column field="fullName" header="Owner"></Column>
        <Column header="Time">
          <template #body="slotProps">
            {{ new Date(slotProps.data.startTime).toLocaleString() }} - 
            {{ slotProps.data.endTime ? new Date(slotProps.data.endTime).toLocaleString() : 'Active' }}
          </template>
        </Column>
        <Column header="Payment Status">
          <template #body="slotProps">
            <Button 
              :icon="slotProps.data.isPaid ? 'pi pi-check-circle' : 'pi pi-times-circle'" 
              :class="slotProps.data.isPaid ? 'p-button-success p-button-sm' : 'p-button-warning p-button-sm'" 
              :label="slotProps.data.isPaid ? 'Paid' : 'Unpaid'"
              @click="togglePayment(slotProps.data)" 
            />
          </template>
        </Column>
        <Column header="Actions">
          <template #body="slotProps">
            <Button icon="pi pi-pencil" class="p-button-text p-button-sm" @click="openDialog(slotProps.data)" />
            <Button icon="pi pi-trash" class="p-button-text p-button-danger p-button-sm" @click="deleteRes(slotProps.data.id)" />
          </template>
        </Column>
      </DataTable>
    </div>

    <Dialog v-model:visible="resDialog" :header="editing ? 'Edit Reservation' : 'New Reservation'" modal :style="{width: '500px'}">
      <div class="field">
        <label>Parking Space</label>
        <Select v-model="resForm.parkingId" :options="mgmtStore.parkingSpacesOptions" optionLabel="numberSpace" optionValue="id" filter placeholder="Select Space" style="width: 100%" />
      </div>
      <div class="field">
        <label>Car</label>
        <Select v-model="resForm.carId" :options="carOptions" optionLabel="label" optionValue="value" filter placeholder="Select Car" style="width: 100%" />
      </div>
      <div class="field-checkbox">
        <label style="display:flex; align-items:center; gap: 0.5rem">
          <input type="checkbox" v-model="resForm.isPaid" /> Is Paid
        </label>
      </div>
      <div class="field">
        <label>End Time (optional to release)</label>
        <input 
        type="datetime-local" 
        v-model="resForm.endTimeLocal" 
        :min="new Date().toISOString().slice(0, 16)" 
        style="padding:0.5rem; width:100%; border:1px solid #ccc; border-radius:4px" />
      </div>
      <template #footer>
        <Button label="Cancel" icon="pi pi-times" class="p-button-text" @click="resDialog = false" />
        <Button label="Save" icon="pi pi-check" @click="saveRes" />
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useReservationStore } from '../stores/reservationStore'
import { useManagementStore } from '../stores/managementStore'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Dialog from 'primevue/dialog'
import Select from 'primevue/select'

//стор со всеми сущностями
const store = useReservationStore()
//стор с бронью
const mgmtStore = useManagementStore()

//переменные для поиска с фильтрами
const carSearch = ref('')
const clientSearch = ref('')

//при отрисовки страницы этот хук выполняет вопросы
onMounted(() => {
  store.fetchReservations()
  //выпадающие машин и мест
  mgmtStore.fetchParkingSpacesOptions()
  mgmtStore.fetchCarsOptions()
})

//из таблицы для выпадающего окна берем авто+владельца
const carOptions = computed(() => {
  return mgmtStore.carsOptions.map(c => ({
    label: `${c.numberCar} (${c.fullName || 'No owner'})`,
    value: c.id
  }))
})

//открыто лиокно
const resDialog = ref(false)
//редактировать?
const editing = ref(false)
//форма
const resForm = ref({ id: null, parkingId: null as number | null, carId: null as number | null, isPaid: false, endTimeLocal: '' })

const openDialog = (res?: any) => {
  //если редактировать
  if (res) {
    //ставим флаг
    editing.value = true
    //копируем все и смотрим чтоб время не налл
    resForm.value = { 
      ...res, 
      endTimeLocal: res.endTime ? new Date(res.endTime).toISOString().slice(0,16) : ''
    }
  } else {
    //иначе сохраняем
    editing.value = false
    resForm.value = { 
      id: null, 
      parkingId: mgmtStore.parkingSpacesOptions[0]?.id || null, 
      carId: mgmtStore.carsOptions[0]?.id || null, 
      isPaid: false, 
      endTimeLocal: '' 
    }
  }
  resDialog.value = true
}

const saveRes = () => {
  // смотрим чтоб время не было позже текущего
  if (resForm.value.endTimeLocal && new Date(resForm.value.endTimeLocal) < new Date()) {
    return alert('End time cannot be in the past')
  }

  const payload = {
    ...resForm.value,
    //приводим время к джава виду
    endTime: resForm.value.endTimeLocal ? new Date(resForm.value.endTimeLocal).toISOString() : null
  }
  //обновить
  if (editing.value) {
    store.updateReservation(payload.id!, payload)
  } else {
    //create
    store.addReservation(payload)
  }
  resDialog.value = false
}
//delete
const deleteRes = (id: number) => {
  if(confirm('Are you sure?')) store.deleteReservation(id)
}
//обновить флаг оплаты
const togglePayment = (res: any) => {
  store.togglePayment(res.id, res)
}

//при поиске с фильтром перебрасывать на 0 стр
const fetchReservations = () => {
  store.fetchReservations(carSearch.value, clientSearch.value, 0, store.size)
}
// настройка пагинации
const onPage = (event: any) => {
  store.fetchReservations(carSearch.value, clientSearch.value, event.page, event.rows)
}

</script>

<style scoped>
.card {
  background: var(--p-surface-0);
  padding: 1.5rem;
  border-radius: var(--p-border-radius);
  box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
.filters {
  display: flex;
  gap: 1rem;
}
.field {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
}
.field label {
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.field-checkbox {
  margin-bottom: 1.5rem;
}
</style>