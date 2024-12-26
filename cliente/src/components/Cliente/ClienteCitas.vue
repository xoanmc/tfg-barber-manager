<template>
  <div class="container py-5">
    <h2 class="text-center mb-4">Mis Citas</h2>

    <div v-if="citas.length" class="row g-3">
      <div
        class="col-12"
        v-for="cita in citas"
        :key="cita.id"
      >
        <!-- Aplicar clase 'rechazada' si la cita está rechazada -->
        <div
          class="card shadow-sm"
          :class="{ 'rechazada': cita.estado === 'Rechazada' }"
        >
          <div class="card-body text-center d-flex flex-column align-items-center">
            <h5 class="card-title">{{ cita.servicio.nombre }}</h5>
            <p class="card-text">
              <strong>Fecha y Hora:</strong>
              {{ new Date(cita.fechaHora).toLocaleString() }}
            </p>
            <p class="card-text">
              <strong>Estado:</strong>
              <span
                :class="{
                  'text-success': cita.estado === 'Confirmada',
                  'text-warning': cita.estado === 'Pendiente',
                  'text-danger': cita.estado === 'Rechazada'
                }"
              >
                {{ cita.estado }}
              </span>
            </p>
            <p class="card-text">
              <strong>Barbero:</strong>
              {{ cita.barbero.nombre }} {{ cita.barbero.apellido }}
            </p>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center">
      <p class="text-muted">No tienes citas reservadas.</p>
      <router-link to="/reserve" class="btn btn-primary mt-3">
        ¿Quieres hacer una reserva?
      </router-link>
    </div>
  </div>
</template>

<script>
import CitaRepository from "@/repositories/CitaRepository";
import AccountRepository from "@/repositories/AccountRepository";

export default {
  data() {
    return {
      citas: [],
      myuser: null,
    };
  },
  mounted() {
    this.fetchCitas();
  },
  methods: {
    async fetchCitas() {
      try {
        this.myuser = await AccountRepository.getAccount();

        if (!this.myuser || !this.myuser.id) {
          throw new Error("No se pudo obtener la información del cliente autenticado.");
        }

        this.citas = await CitaRepository.getCitasCliente(this.myuser.id);
      } catch (err) {
        console.error("Error al obtener las citas del cliente:", err);
      }
    },
  },
};
</script>

<style scoped>

h2 {
  padding: 30px 20px;
  font-size: 2rem;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95); /* Sombreado para mejorar la legibilidad */
  color: #f8f9fa; /* Color claro para contraste */
}

.card {
  border-radius: 10px;
  background-color: #f8f9fa;
  transition: opacity 0.3s ease-in-out; /* transición suave para opacidad */
}

.card.rechazada {
  opacity: 0.5;
}

.card-title {
  font-size: 1.5rem;
  font-weight: bold;
}

.card-text {
  font-size: 1rem;
  margin: 0.5rem 0;
}

.text-success {
  color: #28a745; /* verde Confirmada */
}

.text-warning {
  color: #ffc107; /* amarillo  Pendiente */
}

.text-muted {
  text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.7);
  font-size: 1.5rem;
  font-weight: bold;
  color: #f8f9fa;
}


.text-danger {
  color: #dc3545; /* rojo Rechazada */
}
</style>
