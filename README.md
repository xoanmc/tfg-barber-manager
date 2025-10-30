💈 Aplicación Web para la Gestión de Barberías (Spring + Vue + PostgreSQL)

# Aplicación Web para la Gestión de Barberías
Aplicación full-stack para gestionar reservas, clientes y servicios en barberías, con paneles diferenciados para **cliente**, **barbero** y **administrador**. Moderniza la operativa del negocio y mejora la experiencia del usuario con reservas online, notificaciones y valoraciones.

Este repositorio contiene el código fuente de una aplicación web diseñada para facilitar la gestión de servicios en barberías. La aplicación está orientada tanto a barberos como a clientes, y tiene como objetivo optimizar la operatividad, mejorar la experiencia del cliente y modernizar la interacción entre ambos.  
> 📄 **Memoria del TFG (UDC):** [http://hdl.handle.net/2183/41551](http://hdl.handle.net/2183/41551)

---

## ✨ Descripción del Proyecto
En la era digital, la gestión eficiente de citas y servicios es fundamental para el éxito de las barberías.  
Esta aplicación busca resolver las limitaciones de los métodos tradicionales, como el uso de agendas manuales o llamadas telefónicas, mediante las siguientes funcionalidades clave:

### Funcionalidades Principales
- **Sistema de reservas y citas en línea**: Permite a los clientes agendar sus citas con facilidad y a los barberos optimizar su tiempo.  
- **Gestión de clientes y servicios**: Historial de servicios, preferencias personalizadas y administración de peinados.  
- **Panel de control para barberos**: Herramientas para la gestión de citas, servicios y perfiles profesionales.  
- **Feedback y calificaciones**: Mejora continua basada en las opiniones de los clientes.  
- **Sistema de notificaciones**: Alertas para recordar citas y notificar cambios en tiempo real.  

---

### 👤 Para clientes
- Reserva de citas online con selección de servicios.  
- Perfil e historial de citas.  
- **Recomendación de cortes** en función de la forma del rostro.  
- Valoraciones y comentarios tras el servicio.  
- Notificaciones recordatorio de cita.  

### 💇 Para barberos
- Agenda y gestión de disponibilidad.  
- Historial de clientes y servicios realizados.  
- Perfil profesional (especialidades, horarios).  
- Notificaciones de nuevas reservas/cambios.  

### 🧑‍💼 Para administradores
- Gestión de catálogo de servicios y precios.  
- Gestión de usuarios (clientes y barberos).  
- Creación de promociones y configuración global.  
- Panel de control con métricas básicas.  

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

## ⚙️ Configuración del Proyecto

### Requisitos Previos
- **Java 17**
- **PostgreSQL**
- **Node.js y npm** (para el frontend)

### Instalación

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu-usuario/tu-repo.git
   cd tu-repo
   ```

2. Configura la base de datos:
   - Cambia las credenciales en el archivo `application.yml` ubicado en `src/main/resources` según tu configuración local.

3. Ejecuta el backend:
   ```bash
   mvn spring-boot:run
   ```

4. Ejecuta el frontend:
   - Navega al directorio `cliente/` y ejecuta:
     ```bash
     npm install
     npm run serve
     ```

5. Accede a la aplicación desde tu navegador:
   - **Frontend**: `http://localhost:8080`
   - **Backend API**: `http://localhost:1234/api`
