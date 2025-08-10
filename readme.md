# UserAPI — Spring Boot REST сервис

Простой учебный REST‑сервис на Spring Boot для работы со списком пользователей. Данные хранятся в памяти (H2), доступен фильтр по странам, добавление записи, валидация и интеграционные тесты.

---

## Функциональность

- Получение списка всех пользователей
- Фильтрация по странам
- Добавление нового пользователя (JSON)
- Валидация полей (`age ≥ 0` и пр.)
- Понятные ответы на ошибки (400 при неизвестной стране)
- Встроенная H2‑консоль по `http://localhost:8080/h2-ui`

---

## Стек

Spring Boot **3.5.4**, Spring Web, Spring Data JPA, H2 Database (in‑memory), Lombok, Hibernate Validator 8, JUnit 5

---

## Модель данных

**User**  
`id: Long`, `firstName: String`, `age: Integer`, `country: Country`

`Country` — перечисление (например: `RUSSIA`, `ITALY`, `USA`, `GERMANY`, `JAPAN`).

> Валидация: поле `age` аннотировано `@Min(0)`. При передаче неизвестного значения `country` возвращается 400 с сообщением об ошибке.

---

## REST‑эндпойнты

- `GET  /user-api/v1/users` — список всех пользователей  
- `GET  /user-api/v1/additional-info?countries=GERMANY,ITALY` — фильтр по странам  
- `POST /user-api/v1/users` — добавление пользователя

**Пример тела запроса (POST):**
```json
{
  "firstName": "Anna",
  "age": 28,
  "country": "GERMANY"
}
```

**Пример ошибки (неизвестная страна):**
```json
{
  "error": "Unsupported country. Allowed: [RUSSIA, ITALY, USA, GERMANY, JAPAN]"
}
```

---

## Быстрый старт

### Требования
- Java 17
- Maven 3.9+

### Сборка и тесты
```bash
mvn clean test
```
Ожидаемо: `BUILD SUCCESS` и все тесты зелёные.

### Запуск приложения
```bash
mvn spring-boot:run
```
Сервер поднимется на `http://localhost:8080`.

### Примеры `curl`
```bash
# 1. Список
curl -i http://localhost:8080/user-api/v1/users

# 2. Добавление
curl -i -X POST http://localhost:8080/user-api/v1/users \
     -H "Content-Type: application/json" \
     -d '{"firstName":"Anna","age":28,"country":"GERMANY"}'

# 3. Фильтр
curl -i "http://localhost:8080/user-api/v1/additional-info?countries=GERMANY,ITALY"

# 4. Ошибка (неизвестная страна)
curl -i -X POST http://localhost:8080/user-api/v1/users \
     -H "Content-Type: application/json" \
     -d '{"firstName":"Pierre","age":33,"country":"FRANCE"}'
```

### Доступ к H2 Console
- Откройте `http://localhost:8080/h2-ui`
- JDBC URL: `jdbc:h2:mem:userdb`
- User: `sa`, Password: пусто

---

## Архитектура и компоненты

| Компонент | Реализация |
|---|---|
| Модель | `User` (`@Entity`, `@Table`, `@Min(0)` для `age`), `Country` (enum) |
| Хранилище | `UserRepository extends JpaRepository<User, Long>` + метод `findByCountryInOrderByCountryAsc` |
| Сервис | `UserService` — `findAll()`, `findByCountries()`, `save()` (опц. `@Validated`, `@Valid`) |
| Контроллер | `UserController` — REST‑эндпойнты (см. выше) |
| Обработчик ошибок | `GlobalExceptionHandler @RestControllerAdvice` — перехват `InvalidFormatException` и ответ 400 |
| Начальные данные | `data.sql` — 5 демонстрационных записей |
| Тесты | `UserapiApplicationTests` — 4 сценария (список, добавление, фильтр, валидация) |

---

## Структура проекта

```
userapi/
├── pom.xml
├── src
│   ├── main
│   │   ├── java/com/example/userapi/...
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/java/com/example/userapi/
│       └── UserapiApplicationTests.java
└── screens/              # скриншоты (опционально для отчёта)
```

## Тестирование

Интеграционные тесты `UserapiApplicationTests` покрывают:
- загрузку стартовых данных (`listNotEmpty`),
- добавление пользователя (`addUserWorks`),
- фильтрацию (`filterByCountries`),
- валидацию возраста (`ageMustBePositive`, ожидается `ConstraintViolationException`).

Запуск: `mvn test` или ▶ в IDE.

---

## Конфигурация (H2)

Файл `src/main/resources/application.properties` (ключевое):
```properties
spring.application.name=userapi
spring.datasource.url=jdbc:h2:mem:userdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.defer-datasource-initialization=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-ui
```

---


