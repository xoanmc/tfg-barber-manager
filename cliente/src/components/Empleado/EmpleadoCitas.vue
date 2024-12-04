<template>
  <div class="container py-5">
    <h2 class="text-center mb-4">Citas Programadas</h2>

    <div v-if="citas.length" class="row g-3">
      <div class="col-12" v-for="cita in citas" :key="cita.id">
        <div class="card shadow-sm">
          <div class="card-body text-center d-flex flex-column align-items-center">
            <h5 class="card-title">{{ cita.servicio.nombre }}</h5>
            <p class="card-text"><strong>Fecha y Hora:</strong> {{ new Date(cita.fechaHora).toLocaleString() }}</p>
            <p class="card-text"><strong>Cliente:</strong> {{ cita.cliente.nombre }}</p>
            <p class="card-text"><strong>Estado:</strong> {{ cita.estado }}</p>

            <div class="mt-3">
              <button class="btn btn-success me-2" @click="confirmarCita(cita.id)">Confirmar</button>
              <button class="btn btn-danger" @click="rechazarCita(cita.id)">Rechazar</button>
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
        this.citas = await CitaRepository.getCitasBarbero();
      } catch (error) {
        console.error("Error obteniendo las citas del barbero", error);
      }
    },
    async confirmarCita(citaId) {
      try {
        await CitaRepository.confirmarCita(citaId);
        await this.fetchCitas();
        alert("Cita confirmada exitosamente");
      } catch (error) {
        console.error("Error confirmando cita", error);
        alert("Error al confirmar la cita");
      }
    },
    async rechazarCita(citaId) {
      try {
        await CitaRepository.rechazarCita(citaId);
        await this.fetchCitas();
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
.card {
  border-radius: 10px;
  background-color: #f8f9fa;
}
.card-title {
  font-size: 1.5rem;
  font-weight: bold;
}
.card-text {
  font-size: 1rem;
  margin: 0.5rem 0;
}
</style>
