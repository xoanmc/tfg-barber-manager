# 💈 Aplicación Web para la Gestión de Barberías (Spring + Vue + PostgreSQL)

Aplicación full-stack para gestionar reservas, clientes y servicios en barberías, con paneles diferenciados para **cliente**, **barbero** y **administrador**. Moderniza la operativa del negocio y mejora la experiencia del usuario con reservas online, notificaciones y valoraciones.

> 📄 **Memoria del TFG (UDC):** http://hdl.handle.net/2183/41551

---

## ✨ Características clave

**Para clientes**
- Reserva de citas online con selección de servicios.
- Perfil e historial de citas.
- **Recomendación de cortes** en función de la forma del rostro.
- Valoraciones y comentarios tras el servicio.
- Notificaciones recordatorio de cita.

**Para barberos**
- Agenda y gestión de disponibilidad.
- Historial de clientes y servicios realizados.
- Perfil profesional (especialidades, horarios).
- Notificaciones de nuevas reservas/cambios.

**Para administradores**
- Gestión de catálogo de servicios y precios.
- Gestión de usuarios (clientes y barberos).
- Creación de promociones y configuración global.

---

## 🧱 Arquitectura

Stack principal:
- **Backend:** Java **Spring Boot** (API REST)
- **Frontend:** **Vue.js**
- **Base de datos:** **PostgreSQL**
- **Otros:** Maven, Node.js, Git/GitHub

```mermaid
flowchart LR
  subgraph Client
    UI[Vue.js SPA]
  end

  subgraph Server
    API[Spring Boot REST API]
    DB[(PostgreSQL)]
  end


  UI -->|HTTP/JSON| API
  API --> DB
```

## 🔧 Configuración del frontend

El cliente de Vue puede apuntar a distintos backends mediante la
variable de entorno `VUE_APP_BACKEND_URL`.

- Durante el desarrollo se utiliza la URL definida en
  `.env.development`.
- Para producción se puede definir en `.env.production`.

También es posible establecer la variable manualmente antes de ejecutar
los comandos de Vue CLI:

```bash
VUE_APP_BACKEND_URL=http://localhost:8080/api npm run serve
VUE_APP_BACKEND_URL=https://backend.example.com/api npm run build
```

Estas rutas se leen en `cliente/src/constants.js`.

