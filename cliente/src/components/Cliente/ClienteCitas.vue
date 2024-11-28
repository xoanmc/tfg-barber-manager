<template>
  <div>
    <!-- Mostrar citas reservadas -->
    <div class="citas-container" v-if="citas.length">
      <h2>Mis Citas</h2>
      <ul>
        <li v-for="cita in citas" :key="cita.id">
          <p><strong>Servicio:</strong> {{ cita.servicio.nombre }}</p>
          <p><strong>Fecha y Hora:</strong> {{ new Date(cita.fechaHora).toLocaleString() }}</p>
          <p><strong>Estado:</strong> {{ cita.estado }}</p>
          <p><strong>Barbero:</strong> {{ cita.barbero.nombre }} {{ cita.barbero.apellido }}</p>
        </li>
      </ul>
    </div>

    <!-- Mostrar mensaje si no hay citas -->
    <div v-else>
      <p>No tienes citas reservadas.</p>
      <router-link to="/reserve" class="reserva-link">¿Quieres hacer una reserva?</router-link>
    </div>
  </div>
</template>

<script>
import CitaRepository from "@/repositories/CitaRepository"; // Importar el repositorio de citas
import AccountRepository from "@/repositories/AccountRepository"; // Importar el repositorio de cuentas

export default {
  data() {
    return {
      citas: [], // Lista de citas reservadas por el cliente autenticado
      myuser: null, // Información del cliente autenticado
    };
  },
  mounted() {
    this.fetchCitas(); // Llamar al método para obtener citas al cargar el componente
  },
  methods: {
    async fetchCitas() {
      try {
        // Obtener los datos del cliente autenticado
        this.myuser = await AccountRepository.getAccount();

        if (!this.myuser || !this.myuser.id) {
          throw new Error("No se pudo obtener la información del cliente autenticado.");
        }

        // Llama al endpoint para obtener citas usando el ID del cliente
        this.citas = await CitaRepository.getCitasCliente(this.myuser.id);
      } catch (err) {
        console.error("Error al obtener las citas del cliente:", err);
      }
    },
  },
};
</script>

<style scoped>
.citas-container {
  margin-top: 20px;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 15px;
  background-color: #f9f9f9;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 15px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
}

li:last-child {
  border-bottom: none;
}

p {
  margin: 5px 0;
}

/* Estilo para el enlace de reserva */
.reserva-link {
  color: #007bff;
  text-decoration: underline;
  cursor: pointer;
}

.reserva-link:hover {
  color: #0056b3;
  text-decoration: none;
}
</style>
