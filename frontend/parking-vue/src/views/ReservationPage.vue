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

      <DataTable :value="store.reservations" class="p-datatable-sm" responsiveLayout="scroll" paginator :rows="10">
        <Column field="id" header="ID"></Column>
        <Column field="parkingNumber" header="Space"></Column>
        <Column field="carNumber" header="Car"></Column>
        <Column field="clientFullName" header="Owner"></Column>
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
        <Select v-model="resForm.parkingId" :options="mgmtStore.parkingSpaces" optionLabel="numberSpace" optionValue="id" filter placeholder="Select Space" style="width: 100%" />

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
        <input type="datetime-local" v-model="resForm.endTimeLocal" style="padding:0.5rem; width:100%; border:1px solid #ccc; border-radius:4px" />
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

const store = useReservationStore()
const mgmtStore = useManagementStore()

const carSearch = ref('')
const clientSearch = ref('')

onMounted(() => {
  store.fetchReservations()
  mgmtStore.fetchParkingSpaces()
  mgmtStore.fetchCars()
})

const carOptions = computed(() => {
  return mgmtStore.cars.map(c => ({
    label: `${c.numberCar} (${c.clientFullName || 'No owner'})`,
    value: c.id
  }))
})

// эта функция делает запрос на получение всех бронирований
const fetchReservations = () => {
  store.fetchReservations(carSearch.value, clientSearch.value)
}

const resDialog = ref(false)
const editing = ref(false)
const resForm = ref({ id: null, parkingId: null as number | null, carId: null as number | null, isPaid: false, endTimeLocal: '' })

// эта функция открывает окно для брони
const openDialog = (res?: any) => {
  if (res) {
    editing.value = true
    resForm.value = { 
      ...res, 
      endTimeLocal: res.endTime ? new Date(res.endTime).toISOString().slice(0,16) : ''
    }
  } else {
    editing.value = false
    resForm.value = { id: null, parkingId: mgmtStore.parkingSpaces[0]?.id || null, carId: mgmtStore.cars[0]?.id || null, isPaid: false, endTimeLocal: '' }
  }
  resDialog.value = true
}

// эта функция сохраняет бронь
const saveRes = () => {
  const payload = {
    ...resForm.value,
    endTime: resForm.value.endTimeLocal ? new Date(resForm.value.endTimeLocal).toISOString() : null
  }
  if (editing.value) {
    store.updateReservation(payload.id!, payload)
  } else {
    store.addReservation(payload)
  }
  resDialog.value = false
}

// эта функция удаляет бронь
const deleteRes = (id: number) => {
  if(confirm('Are you sure?')) store.deleteReservation(id)
}

// эта функция переключает статус оплаты
const togglePayment = (res: any) => {
  store.togglePayment(res.id, res)
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
