<template>
  <div class="container py-5">
    <h2 class="text-center mb-4">Mis Citas</h2>

    <div v-if="citas.length" class="row g-3">
      <div class="col-12" v-for="cita in citas" :key="cita.id">
        <div class="card shadow-sm">
          <div class="card-body text-center d-flex flex-column align-items-center">
            <h5 class="card-title">{{ cita.servicio.nombre }}</h5>
            <p class="card-text"><strong>Fecha y Hora:</strong> {{ new Date(cita.fechaHora).toLocaleString() }}</p>
            <p class="card-text"><strong>Estado:</strong> {{ cita.estado }}</p>
            <p class="card-text"><strong>Barbero:</strong> {{ cita.barbero.nombre }} {{ cita.barbero.apellido }}</p>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center">
      <p class="text-muted">No tienes citas reservadas.</p>
      <router-link to="/reserve" class="btn btn-primary mt-3">¿Quieres hacer una reserva?</router-link>
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
