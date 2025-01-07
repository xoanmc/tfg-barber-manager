<template>
  <div class="container py-5">
    <h2 class="text-center mb-4">Citas Programadas</h2>

    <div v-if="citas.length" class="row g-3">
      <div class="col-12" v-for="cita in citas" :key="cita.id">
        <!-- Aplicar clase 'rechazada' si la cita está rechazada -->
        <div class="card shadow-sm" :class="{ 'rechazada': cita.estado === 'Rechazada' }">
          <div class="card-body text-center d-flex flex-column align-items-center">
            <h5 class="card-title">{{ cita.servicio.nombre }}</h5>
            <p class="card-text">
              <strong>Fecha y Hora:</strong>
              {{ formatFechaHora(cita.fechaHora) }}
            </p>
            <p class="card-text">
              <strong>Cliente:</strong>
              {{ cita.cliente.nombre }}
            </p>
            <p class="card-text">
              <strong>Preferencias del Cliente:</strong>
              {{ cita.preferencias || "No especificadas" }}
            </p>
            <p class="card-text">
              <strong>Estado:</strong>
              {{ cita.estado }}
            </p>

            <!-- Mostrar botones solo si el estado es 'Pendiente' -->
            <div v-if="cita.estado === 'Pendiente'" class="mt-3">
              <button class="btn btn-success me-2" @click="confirmarCita(cita.id)">
                Confirmar
              </button>
              <button class="btn btn-danger" @click="rechazarCita(cita.id)">
                Rechazar
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center">
      <p class="text-muted">No tienes citas programadas.</p>
    </div>
  </div>
</template>

<script>
import CitaRepository from "@/repositories/CitaRepository";

export default {
  data() {
    return {
      citas: [],
    };
  },
  mounted() {
    this.fetchCitas();
  },
  methods: {
    async fetchCitas() {
      try {
        const citas = await CitaRepository.getCitasBarbero();

        // Combinar fecha y hora en `fechaHora`
        this.citas = citas.filter(cita => cita.estado !== "Cancelada").map(cita => ({
          ...cita,
          fechaHora: `${cita.fecha}T${cita.hora}`,
        }));
      } catch (error) {
        console.error("Error obteniendo las citas del barbero", error);
      }
    },

    formatFechaHora(fechaHora) {
      if (!fechaHora) {
        return "Fecha/Hora no disponibles";
      }
      try {
        const fecha = new Date(fechaHora);
        return new Intl.DateTimeFormat("es-ES", {
          dateStyle: "long", // fecha en formato largo
          timeStyle: "short", // hora sin segundos
        }).format(fecha);
      } catch (error) {
        console.error("Error al formatear la fecha y hora:", error);
        return "Fecha/Hora inválidas";
      }
    },

    async confirmarCita(citaId) {
      try {
        await CitaRepository.confirmarCita(citaId);
        await this.fetchCitas(); // Actualizar las citas después de confirmar
        alert("Cita confirmada exitosamente");
      } catch (error) {
        console.error("Error confirmando cita", error);
        alert("Error al confirmar la cita");
      }
    },

    async rechazarCita(citaId) {
      try {
        await CitaRepository.rechazarCita(citaId);
        await this.fetchCitas(); // Actualizar las citas después de rechazar
        alert("Cita rechazada exitosamente");
      } catch (error) {
        console.error("Error rechazando cita", error);
        alert("Error al rechazar la cita");
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
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95);
  color: #f8f9fa;
}

.card {
  border-radius: 10px;
  background-color: #f8f9fa;
  transition: opacity 0.3s ease-in-out;
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

.text-muted {
  text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.7);
  font-size: 1.5rem;
  font-weight: bold;
  color: #f8f9fa;
}
</style>
