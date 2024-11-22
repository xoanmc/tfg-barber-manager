
# Aplicación Web para la Gestión de Barberías

Este repositorio contiene el código fuente de una aplicación web diseñada para facilitar la gestión de servicios en barberías. La aplicación está orientada tanto a barberos como a clientes, y tiene como objetivo optimizar la operatividad, mejorar la experiencia del cliente y modernizar la interacción entre ambos.

## Descripción del Proyecto

En la era digital, la gestión eficiente de citas y servicios es fundamental para el éxito de las barberías. Esta aplicación busca resolver las limitaciones de los métodos tradicionales, como el uso de agendas manuales o llamadas telefónicas, mediante las siguientes funcionalidades clave:

### Funcionalidades Principales
- **Sistema de reservas y citas en línea**: Permite a los clientes agendar sus citas con facilidad y a los barberos optimizar su tiempo.
- **Gestión de clientes y servicios**: Historial de servicios, preferencias personalizadas y administración de peinados.
- **Panel de control para barberos**: Herramientas para la gestión de citas, servicios y perfiles profesionales.
- **Feedback y calificaciones**: Mejora continua basada en las opiniones de los clientes.
- **Sistema de notificaciones**: Alertas para recordar citas y notificar cambios en tiempo real.

## Tecnologías Utilizadas

- **Backend**: Java Spring Boot
- **Frontend**: Vue.js
- **Base de Datos**: PostgreSQL
- **Control de Versiones**: Git
- **Notificaciones y Emails**: Configuración SMTP para envío de correos electrónicos.

## Estado Actual del Proyecto

El proyecto está en fase de desarrollo. Las funcionalidades básicas están en proceso de implementación y se espera realizar iteraciones constantes para añadir mejoras y nuevas características.

### Próximos Pasos
- Integración de un sistema de autenticación por correo electrónico para la confirmación de registros.
- Finalización de las interfaces de usuario para clientes y barberos.
- Optimización del sistema de notificaciones y recordatorios.

## Configuración del Proyecto

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


## Licencia

Desarrollado como parte del Trabajo Fin de Grado en la Facultad de Informática de la Universidad de A Coruña.
