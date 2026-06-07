<script setup lang="ts">
import { ref } from 'vue'
import { useParkingStore } from '../stores/parkingStore'

import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'

const parkingStore = useParkingStore()
const spaceNumber = ref<string>('')

const onAddParkingSpace = async () => {
  if (!spaceNumber.value.trim()) return
  try {
    await parkingStore.addSpace(spaceNumber.value.trim())
    spaceNumber.value = ''
  } catch (error) {
    console.error('Ошибка создания парковочного места:', error)
  }
}
</script>

<template>
  <Card class="management-card">
    <template #title>
      <h2>Управление парковочными местами</h2>
    </template>
    
    <template #content>
      <!-- Форма создания парковочного места -->
      <div class="form-grid">
        <div class="input-group">
          <label for="space-number">Номер места</label>
          <InputText id="space-number" v-model="spaceNumber" placeholder="Например, А-101" />
        </div>

        <Button 
          label="Создать место" 
          icon="pi pi-map-marker" 
          @click="onAddParkingSpace" 
          :disabled="!spaceNumber.trim()"
        />
      </div>

      <!-- Таблица мест -->
      <DataTable :value="parkingStore.spaces" class="p-datatable-sm" stripedRows>
        <Column field="id" header="ID места" />
        <Column field="numberSpace" header="Номер места" />
        <Column header="Действие">
          <template #body="{ data }">
            <Button icon="pi pi-trash" severity="danger" text rounded @click="parkingStore.deleteSpace(data.id)" />
          </template>
        </Column>
      </DataTable>
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
</style>