# Java Explore With Me

Проект **Java Explore With Me** — это RESTful сервис для организации и управления мероприятиями.  
Позволяет создавать, просматривать, редактировать и удалять события, а также управлять пользователями и заявками на участие.

## Основные возможности

- Создание, редактирование и удаление мероприятий
- Регистрация пользователей
- Управление заявками на участие в мероприятиях
- Поиск и фильтрация событий
- Поддержка ролей и прав доступа

## Технологии

- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- MapStruct
- Lombok

## Архитектура проекта

Проект построен по классической многослойной архитектуре:

- **Контроллеры (Controller)** — принимают HTTP-запросы, валидируют входящие данные и передают их в сервисный слой.
- **Сервисный слой (Service)** — содержит бизнес-логику приложения и координирует работу между слоями.
- **Репозитории (Repository)** — обеспечивают доступ к базе данных через Spring Data JPA.
- **DTO и мапперы (DTO & Mapper)** — используются для передачи данных между слоями и преобразования сущностей в объекты, пригодные для обмена по API.
- **Модели (Model)** — представляют сущности базы данных.

Используется принцип разделения ответственности (Separation of Concerns), что облегчает сопровождение и расширение кода.

## Установка и запуск

1. Чтобы проект запустился, локально должна быть поднята PostgreSQL с базой explore_with_me и пользователем postgres с паролем postgres.
   Если базы и пользователя нет — нужно создать, либо изменить эти параметры под свою базу в `application.properties`.

   ```application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/explore_with_me
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Соберите и запустите приложение:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. По умолчанию сервис будет доступен по адресу:  
   `http://localhost:8080`

## API

Проект реализует REST API.

### Примеры основных эндпоинтов

#### События

- `GET /events` — получить список опубликованных событий
- `GET /events/{id}` — получить информацию о конкретном событии
- `POST /users/{userId}/events` — создать новое событие (требуется авторизация)
- `PATCH /users/{userId}/events/{eventId}` — редактировать своё событие
- `DELETE /users/{userId}/events/{eventId}` — удалить своё событие

#### Заявки на участие

- `GET /users/{userId}/requests` — получить список своих заявок
- `POST /users/{userId}/requests` — подать заявку на участие
- `PATCH /users/{userId}/requests/{requestId}/cancel` — отменить заявку

#### Пользователи и администрирование

- `GET /admin/users` — получить список всех пользователей (админ)
- `POST /admin/users` — добавить нового пользователя (админ)
- `DELETE /admin/users/{userId}` — удалить пользователя (админ)

#### Категории

- `GET /categories` — список всех категорий
- `GET /categories/{catId}` — информация о категории (по ID)

Формат запросов/ответов — JSON.

### Примеры запросов и ответов

#### Получение списка событий

```http
GET /events HTTP/1.1
Host: localhost:8080
Accept: application/json
```

**Ответ:**

```json
[
   {
      "id": 1,
      "title": "Концерт рок-группы",
      "annotation": "Ночной концерт в клубе",
      "category": {
         "id": 3,
         "name": "Музыка"
      },
      "eventDate": "2025-08-10T20:00:00",
      "paid": true,
      "participantLimit": 100,
      "confirmedRequests": 23,
      "state": "PUBLISHED",
      "description": "Выступление легендарной группы!",
      "location": {
         "lat": 55.7522,
         "lon": 37.6156
      },
      "initiator": {
         "id": 12,
         "name": "Иван Иванов"
      },
      "views": 348
   },
   {
      "id": 2,
      "title": "Выставка современного искусства",
      "annotation": "Экспозиция молодых художников",
      "category": {
         "id": 5,
         "name": "Искусство"
      },
      "eventDate": "2025-09-01T18:00:00",
      "paid": false,
      "participantLimit": 50,
      "confirmedRequests": 12,
      "state": "PUBLISHED",
      "description": "Современное искусство глазами нового поколения",
      "location": {
         "lat": 59.9343,
         "lon": 30.3351
      },
      "initiator": {
         "id": 13,
         "name": "Анна Смирнова"
      },
      "views": 215
   }
]

```

---

#### Создание нового события

```http
POST /users/1/events HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: application/json

{
  "title": "Вечеринка на крыше",
  "annotation": "Летняя вечеринка с живой музыкой",
  "category": 4,
  "eventDate": "2025-08-15T21:00:00",
  "paid": false,
  "participantLimit": 30,
  "description": "Приглашаем всех друзей!",
  "requestModeration": true,
  "location": {
    "lat": 55.751244,
    "lon": 37.618423
  }
}
```

**Ответ:**

```json
{
   "id": 10,
   "title": "Вечеринка на крыше",
   "annotation": "Летняя вечеринка с живой музыкой",
   "category": {
      "id": 4,
      "name": "Вечеринки"
   },
   "eventDate": "2025-08-15T21:00:00",
   "paid": false,
   "participantLimit": 30,
   "confirmedRequests": 0,
   "state": "PENDING",
   "description": "Приглашаем всех друзей!",
   "location": {
      "lat": 55.751244,
      "lon": 37.618423
   },
   "initiator": {
      "id": 1,
      "name": "Иван Иванов"
   },
   "views": 0
}
```

## Тесты

Для тестирования API в проекте есть коллекция Postman:  
[`postman/feature.json`](https://github.com/semigris/java-explore-with-me/blob/main/postman/feature.json)

Её можно импортировать в Postman и использовать готовые запросы для проверки работы сервиса.