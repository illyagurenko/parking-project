<template>
  <div class="management-page">
    <h2>Management Dashboard</h2>

    <div class="grid-container">
      <!-- Clients Section -->
      <div class="card">
        <div class="card-header">
          <h3>Clients</h3>
          <Button icon="pi pi-plus" label="Add Client" @click="openClientDialog()" size="small" />
        </div>
        <div class="search-bar">
          <InputText v-model="clientSearch" placeholder="Search by full name..." @input="fetchClients" />
        </div>
        <DataTable 
          :value="store.clients" 
          lazy 
          paginator 
          :rows="store.clientsSize" 
          :totalRecords="store.totalClients"
          :first="store.clientsPage * store.clientsSize"
          @page="onClientPage"
          class="p-datatable-sm" 
          responsiveLayout="scroll"
        >
          <Column field="id" header="ID"></Column>
          <Column field="fullName" header="Full Name"></Column>
          <Column header="Actions">
            <template #body="slotProps">
              <Button icon="pi pi-pencil" class="p-button-text p-button-sm" @click="openClientDialog(slotProps.data)" />
              <Button icon="pi pi-trash" class="p-button-text p-button-danger p-button-sm" @click="deleteClient(slotProps.data.id)" />
            </template>
          </Column>
        </DataTable>
      </div>

      <!-- Cars Section -->
      <div class="card">
        <div class="card-header">
          <h3>Cars</h3>
          <Button icon="pi pi-plus" label="Add Car" @click="openCarDialog()" size="small" />
        </div>
        <div class="search-bar">
          <InputText v-model="carSearch" placeholder="Search by number..." @input="fetchCars" />
        </div>
        <DataTable 
          :value="store.cars" 
          lazy 
          paginator 
          :rows="store.carsSize" 
          :totalRecords="store.totalCars"
          :first="store.carsPage * store.carsSize"
          @page="onCarPage"
          class="p-datatable-sm" 
          responsiveLayout="scroll"
        >          
          <Column field="id" header="ID"></Column>
          <Column field="numberCar" header="Number"></Column>
          <Column field="fullName" header="Owner"></Column>
          <Column header="Actions">
            <template #body="slotProps">
              <Button icon="pi pi-pencil" class="p-button-text p-button-sm" @click="openCarDialog(slotProps.data)" />
              <Button icon="pi pi-trash" class="p-button-text p-button-danger p-button-sm" @click="deleteCar(slotProps.data.id)" />
            </template>
          </Column>
        </DataTable>
      </div>

      <!-- Parking Spaces Section -->
      <div class="card">
        <div class="card-header">
          <h3>Parking Spaces</h3>
          <Button icon="pi pi-plus" label="Add Space" @click="openSpaceDialog()" size="small" />
        </div>
        <DataTable 
          :value="store.parkingSpaces" 
          lazy 
          paginator 
          :rows="store.parkingSpacesSize" 
          :totalRecords="store.totalParkingSpaces"
          :first="store.parkingSpacesPage * store.parkingSpacesSize"
          @page="onSpacePage"
          class="p-datatable-sm" 
          responsiveLayout="scroll"
        >
          <Column field="id" header="ID"></Column>
          <Column field="numberSpace" header="Space Number"></Column>
          <Column header="Actions">
            <template #body="slotProps">
              <Button icon="pi pi-pencil" class="p-button-text p-button-sm" @click="openSpaceDialog(slotProps.data)" />
              <Button icon="pi pi-trash" class="p-button-text p-button-danger p-button-sm" @click="deleteSpace(slotProps.data.id)" />
            </template>
          </Column>
        </DataTable>
      </div>
    </div>

    <!-- Client Dialog -->
    <Dialog v-model:visible="clientDialog" :header="editingClient ? 'Edit Client' : 'New Client'" modal :style="{width: '400px'}">
      <div class="field">
        <label for="fullName">Full Name</label>
        <InputText id="fullName" v-model="clientForm.fullName" autofocus />
      </div>
      <template #footer>
        <Button label="Cancel" icon="pi pi-times" class="p-button-text" @click="clientDialog = false" />
        <Button label="Save" icon="pi pi-check" @click="saveClient" />
      </template>
    </Dialog>

    <!-- Car Dialog -->
    <Dialog v-model:visible="carDialog" :header="editingCar ? 'Edit Car' : 'New Car'" modal :style="{width: '400px'}">
      <div class="field">
        <label for="numberCar">Car Number</label>
        <InputText id="numberCar" v-model="carForm.numberCar" autofocus />
      </div>
      <div class="field">
        <label>Owner (Client)</label>
        <Select v-model="carForm.clientId" :options="store.clientsOptions" optionLabel="fullName" optionValue="id" filter showClear placeholder="Select an owner" style="width: 100%" />
      </div>
      <template #footer>
        <Button label="Cancel" icon="pi pi-times" class="p-button-text" @click="carDialog = false" />
        <Button label="Save" icon="pi pi-check" @click="saveCar" />
      </template>
    </Dialog>

    <!-- Space Dialog -->
    <Dialog v-model:visible="spaceDialog" :header="editingSpace ? 'Edit Space' : 'New Space'" modal :style="{width: '400px'}">
      <div class="field">
        <label for="numberSpace">Space Number (e.g. N_01)</label>
        <InputText id="numberSpace" v-model="spaceForm.numberSpace" autofocus />
      </div>
      <template #footer>
        <Button label="Cancel" icon="pi pi-times" class="p-button-text" @click="spaceDialog = false" />
        <Button label="Save" icon="pi pi-check" @click="saveSpace" />
      </template>
    </Dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useManagementStore } from '../stores/managementStore'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Dialog from 'primevue/dialog'
import Select from 'primevue/select'

const store = useManagementStore()

const clientSearch = ref('')
const carSearch = ref('')

onMounted(() => {
  store.fetchClients()
  store.fetchCars()
  store.fetchParkingSpaces()
  
  // Загружаем списки для выпадающих меню
  store.fetchClientsOptions()
})

// Поиск сбрасывает страницу на 0
const fetchClients = () => store.fetchClients(clientSearch.value, 0, store.clientsSize)
const fetchCars = () => store.fetchCars(carSearch.value, 0, store.carsSize)

// Обработчики пагинации
const onClientPage = (event: any) => {
  store.fetchClients(clientSearch.value, event.page, event.rows)
}

const onCarPage = (event: any) => {
  store.fetchCars(carSearch.value, event.page, event.rows)
}

const onSpacePage = (event: any) => {
  store.fetchParkingSpaces('', event.page, event.rows)
}

// Client Logic
const clientDialog = ref(false)
const editingClient = ref(false)
const clientForm = ref({ id: null, fullName: '' })

const openClientDialog = (client?: any) => {
  if (client) {
    editingClient.value = true
    clientForm.value = { ...client }
  } else {
    editingClient.value = false
    clientForm.value = { id: null, fullName: '' }
  }
  clientDialog.value = true
}

const saveClient = () => {
  if (editingClient.value) {
    store.updateClient(clientForm.value.id!, clientForm.value)
  } else {
    store.addClient(clientForm.value)
  }
  clientDialog.value = false
}

const deleteClient = (id: number) => {
  if(confirm('Are you sure?')) store.deleteClient(id)
}

// Car Logic
const carDialog = ref(false)
const editingCar = ref(false)
const carForm = ref({ id: null, numberCar: '', clientId: null })

const openCarDialog = (car?: any) => {
  if (car) {
    editingCar.value = true
    carForm.value = { ...car }
  } else {
    editingCar.value = false
    carForm.value = { id: null, numberCar: '', clientId: null }
  }
  carDialog.value = true
}

const saveCar = () => {
  if (editingCar.value) {
    store.updateCar(carForm.value.id!, carForm.value)
  } else {
    store.addCar(carForm.value)
  }
  carDialog.value = false
}

const deleteCar = (id: number) => {
  if(confirm('Are you sure?')) store.deleteCar(id)
}

// Space Logic
const spaceDialog = ref(false)
const editingSpace = ref(false)
const spaceForm = ref({ id: null, numberSpace: 'N_' })

const openSpaceDialog = (space?: any) => {
  if (space) {
    editingSpace.value = true
    spaceForm.value = { ...space }
  } else {
    editingSpace.value = false
    spaceForm.value = { id: null, numberSpace: 'N_' }
  }
  spaceDialog.value = true
}
const saveSpace = () => {
  if (editingSpace.value) {
    store.updateParkingSpace(spaceForm.value.id!, spaceForm.value)
  } else {
    store.addParkingSpace(spaceForm.value)
  }
  spaceDialog.value = false
}
const deleteSpace = (id: number) => {
  if(confirm('Are you sure?')) store.deleteParkingSpace(id)
}

</script>

<style scoped>
.grid-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2rem;
}
@media(min-width: 1024px) {
  .grid-container {
    grid-template-columns: repeat(2, 1fr);
  }
}
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
.card-header h3 {
  margin: 0;
  color: var(--p-text-color);
}
.search-bar {
  margin-bottom: 1rem;
}
.search-bar .p-inputtext {
  width: 100%;
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
.field .p-inputtext {
  width: 100%;
}
</style>