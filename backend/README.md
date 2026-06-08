# Parking Backend 
Сервер для системы управления автостоянкой.
## Стек и Библиотеки
- **Java 21**
- **Spring Boot 3.5.14**
- **Spring Web** 
- **Spring JDBC** 
- **PostgreSQL JDBC Driver**
- **Spring Boot Starter Validation** 
## База Данных

В базе данных `parking_db` создаются 4 основные таблицы:

1. `clients` (id, full_name) — данные владельцев.
2. `cars` (id, number_car, client_id) — автомобили, привязанные к владельцам.
3. `parking_spaces` (id, number_space) — доступные парковочные места.
4. `reservations` (id, parking_id, car_id, is_paid, start_time, end_time) — учет занятости мест машинами.

## API Эндпоинты

Эндпоинты (базовый URL `/api`):

### Клиенты
- `GET /clients` — получить список всех клиентов, с поиском по имени.
- `POST /clients` — создать нового клиента.
- `PUT /clients/{id}` — обновить данные клиента.
- `DELETE /clients/{id}` — удалить клиента.

### Автомобили
- `GET /cars` — получить список автомобилей с привязанными владельцами.
- `POST /cars` — добавить новый автомобиль.
- `PUT /cars/{id}` — изменить данные автомобиля.
- `DELETE /cars/{id}` — удалить автомобиль.

### Парковочные места
- `GET /parking-spaces` — получить список всех мест.
- `POST /parking-spaces` — добавить новое место.
- `PUT /parking-spaces/{id}` — изменить номер места.
- `DELETE /parking-spaces/{id}` — удалить место.

### Бронирования
- `GET /reservations` — получить историю и текущие бронирования, есть фильтрация по имени и номеру.
- `POST /reservations` — забронировать место для машины.
- `PUT /reservations/{id}` — обновить бронь.
- `DELETE /reservations/{id}` — удалить запись о бронировании.

## Как запустить локально
Запустить локальный сервер PostgreSQL и добавить в переменное окружение параметры по типу:
порт 5432, пользователь: `postgres`, пароль: `postgres`, база данных: `parking_db`. 
Схема создастся автоматически при старте из файла `schema.sql`.
Откройте терминал в папке `backend` и запустите:
```bash
mvn spring-boot:run
```
Сервер запустится на порту `8080`.
