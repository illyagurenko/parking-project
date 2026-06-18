import { createRouter, createWebHistory } from 'vue-router'
import ManagementPage from '../views/ManagmentPage.vue'
import ReservationPage from '../views/ReservationPage.vue'

// роутер для навигации по страницам приложения
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/management'
    },
    {
      path: '/management',
      name: 'management',
      component: ManagementPage
    },
    {
      path: '/reservations',
      name: 'reservations',
      component: ReservationPage
    }
  ]
})

export default router
