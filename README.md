# Alberto Arroyo Santofimia

![image](https://github.com/user-attachments/assets/cd99e2d6-f4da-453f-a88a-e8c30087fa9a)


# Agencia de Turismo - API REST

## Wiki de la aplicación

https://github.com/AlbertoArroyoS/albertoArroyo_pruebatec4/wiki

## Descripción

Esta API REST permite la gestión de reservas de hoteles y vuelos para una agencia de turismo. La aplicación proporciona funcionalidades para buscar y reservar hoteles y vuelos, así como gestionar la información de estos servicios. Se han aplicado buenas prácticas en el desarrollo utilizando tecnologías modernas.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot** (Backend)
- **Spring Security** (Autenticación y autorización)
- **JPA + Hibernate** (Persistencia de datos)
- **JUnit & Mockito** (Testing)
- **Swagger** (Documentación de API)
- **MySQL** (Base de datos)
- **Git & GitHub** (Control de versiones)

---

## Requisitos para la ejecución

1. **Instalar Java 17**
2. **Instalar Maven**
3. **Configurar base de datos MySQL**
4. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/AlbertoArroyoS/albertoArroyo_pruebatec4.git
   ```
5. **En la carpeta /jar/**:
   
   ![image](https://github.com/user-attachments/assets/e2f588b3-ca75-444e-adc1-a07a1bd62d0c)

   Ejercutar el comando para cargar la aplicación
   
   ```bash
   java -jar travelagency-0.0.1-SNAPSHOT.jar
   ```
   
   ![image](https://github.com/user-attachments/assets/2f6f373b-fe57-4d89-a235-f0fef945849e)

6. **Acceder a la API** en:
   ```
   http://localhost:8080
   ```
7. **Acceder a la documentación Swagger**:
   ```
   http://localhost:8080/swagger-ui.html
   ```
8. **Usuario de prueba para los endpoint que requieren autenticación **:
   ```
   user.name=hackaboss
   user.password=1234
   ```

---

## Endpoints Disponibles

** En el Wiki están todos los Endpoints explicados punto por punto

### Hoteles

- **Obtener todos los hoteles**  
  `GET /agency/hotels`
- **Obtener habitaciones disponibles por fecha y destino**  
  `GET /agency/rooms?dateFrom=dd/MM/yyyy&dateTo=dd/MM/yyyy&destination="nombre_destino"`
- **Reservar una habitación**  
  `POST /agency/room-booking/new`
  ```json
  {
  "hotel": {
    "hotelCode": "AR-0002",
    "name": "Atlantis Resort",
    "city": "Miami",
    "roomType": "DOBLE",
    "ratePerNight": 630,
    "dateFrom": "2024-02-10",
    "dateTo": "2024-03-20"
  },
  "hosts": [
    {
      "name": "Alberto",
      "surname": "Arroyo",
      "phone": "123456789",
      "dni": "12345678B"
    },
    {
      "name": "Ana",
      "surname": "Pérez",
      "phone": "987654321",
      "dni": "87654321Z"
    },
    {
      "name": "Luis",
      "surname": "Martinez",
      "phone": "555666777",
      "dni": "23456789C"
    }
  ]
  }

  ```
- **CRUD de hoteles** (Requiere autenticación)
  - `POST /agency/hotels/new`
  - `PUT /agency/hotels/edit/{id}`
  - `DELETE /agency/hotels/delete/{id}`
  - `GET /agency/hotels/{id}`

### Vuelos

- **Obtener todos los vuelos**  
  `GET /agency/flights`
- **Obtener vuelos disponibles por fecha y destino**  
  `GET /agency/flights?dateFrom=dd/MM/yyyy&dateTo=dd/MM/yyyy&origin="ciudad1"&destination="ciudad2"`
- **Reservar un vuelo**  
  `POST /agency/flight-booking/new`
  ```json
  {
  "flight": {
    "flightNumber": "BAMI-1235"
  },
  "passengers": [
    {
      "dni": "12345678A",
      "name": "John",
      "surname": "Doe",
      "phone": "123456789"
    },
    {
      "dni": "87654321B",
      "name": "Alice",
      "surname": "Smith",
      "phone": "987654321"
    }
  ]
  }

  ```
- **CRUD de vuelos** (Requiere autenticación)
  - `POST /agency/flights/new`
  - `PUT /agency/flights/edit/{id}`
  - `DELETE /agency/flights/delete/{id}`
  - `GET /agency/flights/{id}`

---

## Seguridad y Autenticación

Para acceder a los endpoints protegidos, se requiere autenticación mediante **Spring Security**.


## Validaciones Implementadas

✅ Validación de existencia para hoteles, vuelos y reservas.  
✅ Prevención de reservas duplicadas.  
✅ No se puede eliminar un hotel o vuelo si tiene reservas activas.  
✅ Mensajes de error adecuados si no hay resultados en búsquedas.  

---

## Testing

Se han implementado pruebas unitarias con JUnit y Mockito.


