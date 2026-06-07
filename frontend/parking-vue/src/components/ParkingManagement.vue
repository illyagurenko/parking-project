<script setup lang="ts">
import { ref } from 'vue'
import { useParkingStore } from '../stores/parkingStore'

import Card from 'primevue/card'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Dialog from 'primevue/dialog'

const parkingStore = useParkingStore()
const spaceNumber = ref<string>('')

  // Состояния для редактирования
const isEditDialogVisible = ref<boolean>(false)
const editingSpaceId = ref<number | null>(null)
const editSpaceNumber = ref<string>('')

const onAddParkingSpace = async () => {
  if (!spaceNumber.value.trim()) return
  try {
    await parkingStore.addSpace(spaceNumber.value.trim())
    spaceNumber.value = ''
  } catch (error) {
    console.error('Ошибка создания парковочного места:', error)
  }
}

// Открытие окна редактирования места
const onEditSpaceClick = (space: any) => {
  editingSpaceId.value = space.id
  editSpaceNumber.value = space.numberSpace
  isEditDialogVisible.value = true
}

// Сохранение изменений места
const onSaveSpaceEdit = async () => {
  if (!editSpaceNumber.value.trim() || !editingSpaceId.value) return
  try {
    await parkingStore.updateSpace(editingSpaceId.value, editSpaceNumber.value.trim())
    isEditDialogVisible.value = false
    editingSpaceId.value = null
  } catch (error) {
    console.error('Ошибка при обновлении парковочного места:', error)
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
      <!-- Таблица мест -->
      <DataTable :value="parkingStore.spaces" class="p-datatable-sm" stripedRows>
        <Column field="id" header="ID места" />
        <Column field="numberSpace" header="Номер места" />
        <Column header="Действия">
          <template #body="{ data }">
            <div class="actions-cell">
              <Button icon="pi pi-pencil" severity="warn" text rounded @click="onEditSpaceClick(data)" />
              <Button icon="pi pi-trash" severity="danger" text rounded @click="parkingStore.deleteSpace(data.id)" />
            </div>
          </template>
        </Column>
      </DataTable>

      <!-- МОДАЛЬНОЕ ОКНО РЕДАКТИРОВАНИЯ ПАРКОВКИ -->
      <Dialog 
        v-model:visible="isEditDialogVisible" 
        modal 
        header="Редактирование парковочного места" 
        :style="{ width: '25rem' }"
      >
        <div class="edit-dialog-form">
          <div class="input-group">
            <label for="edit-space-number">Номер места</label>
            <InputText id="edit-space-number" v-model="editSpaceNumber" class="full-width" />
          </div>
          
          <div class="dialog-actions">
            <Button label="Отмена" severity="secondary" text @click="isEditDialogVisible = false" />
            <Button label="Сохранить" severity="success" @click="onSaveSpaceEdit" :disabled="!editSpaceNumber.trim()" />
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