<template>
  <div class="app-layout">
    <header class="app-header">
      <div class="logo">
        <i class="pi pi-car"></i> Parking Admin
      </div>
      <nav class="app-nav">
        <RouterLink to="/management" active-class="active-link">Management</RouterLink>
        <RouterLink to="/reservations" active-class="active-link">Reservations</RouterLink>
      </nav>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<style>
body {
  margin: 0;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  background-color: var(--p-surface-50);
  color: var(--p-text-color);
}
.app-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  background-color: var(--p-surface-0);
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}
.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: var(--p-primary-color);
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.app-nav {
  display: flex;
  gap: 1.5rem;
}
.app-nav a {
  text-decoration: none;
  color: var(--p-text-color);
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: var(--p-border-radius);
  transition: background-color 0.2s, color 0.2s;
}
.app-nav a:hover {
  background-color: var(--p-surface-100);
}
.active-link {
  background-color: var(--p-primary-50);
  color: var(--p-primary-color) !important;
}
.app-main {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}
</style>
