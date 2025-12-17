# 🛒 Tech Demo – Online Shop

Aplicación **full-stack** de gestión de pedidos desarrollada como prueba técnica, compuesta por:

- **Backend**: Spring Boot (Java 17, JPA, H2)
- **Frontend**: Angular 21
- **Infraestructura**: Docker & Docker Compose

---

## 📐 Arquitectura del proyecto

```
tech-demo-online-shop
│
├── shop-backend        # API REST (Spring Boot)
│   ├── src
│   ├── Dockerfile
│   └── pom.xml
│
├── shop-frontend       # Angular 21
│   ├── src
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
├── docker-compose.yml  # Orquestación frontend + backend
└── README.md
```

---

## 🚀 Funcionalidades principales

### Backend

- Gestión de pedidos y productos
- Pedidos con múltiples productos
- Estados controlados del pedido
- Reglas de negocio para edición y estados finales
- Paginación
- Validaciones y manejo global de errores

### Base de datos

- **H2 en memoria**
- Datos de prueba cargados automáticamente
- Reinicio completo al reiniciar la aplicación

---

## 🌐 Endpoints de la API

### 📦 Productos

| Método | Endpoint | Descripción |
|------|---------|-------------|
| GET | `/api/v1/products` | Obtiene todos los productos disponibles |

---

### 🧾 Pedidos

| Método | Endpoint | Descripción |
|------|---------|-------------|
| GET | `/api/v1/orders` | Lista paginada de pedidos |
| GET | `/api/v1/orders/{id}` | Obtiene un pedido por ID |
| POST | `/api/v1/orders` | Crea un nuevo pedido |
| PUT | `/api/v1/orders/{id}` | Modifica datos del cliente y pedido |
| PATCH | `/api/v1/orders/{id}/status` | Actualiza únicamente el estado del pedido |

#### Ejemplo: Crear pedido

```json
{
  "customerName": "John Doe",
  "customerContact": "john@doe.com",
  "items": [
    {
      "productId": "uuid-product",
      "quantity": 2
    }
  ]
}
```

---

## 🖥️ Frontend

- Angular 21 con componentes standalone
- Formularios reactivos
- Modales para crear y editar pedidos
- Paginación visual
- Estilos según estado del pedido

---

## 🐳 Docker

### Arranque del proyecto

```bash
docker compose build
docker compose up
```

### Accesos

- Frontend: http://localhost:4200
- Backend: http://localhost:8080

---

## 🔮 Posibles ampliaciones

- Persistencia con PostgreSQL
- Autenticación JWT
- Gestión de usuarios
- Filtros avanzados
- Tests automáticos
- CI/CD

---

Proyecto desarrollado como prueba técnica full-stack.
