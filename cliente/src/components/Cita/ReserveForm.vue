<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow-lg" style="max-width: 500px; width: 100%; border-radius: 15px;">
      <div class="card-body p-4">
        <h2 class="card-title text-center text-primary mb-4">Reserva tu Cita</h2>

        <!-- Selección de barbero -->
        <div class="mb-4">
          <label for="barbero" class="form-label fs-6 fw-semibold text-secondary">Selecciona Barbero:</label>
          <select v-model="cita.barberoId" id="barbero" class="form-select" @change="actualizarHorariosDisponibles">
            <option v-for="barbero in barberos" :key="barbero.id" :value="barbero.id">
              {{ barbero.nombre }}
            </option>
          </select>
        </div>

        <!-- Selección de fecha -->
        <div class="mb-4">
          <label for="fecha" class="form-label fs-6 fw-semibold text-secondary">Selecciona Fecha:</label>
          <flat-pickr ref="flatpickrInstance" v-model="cita.fecha" :config="flatpickrConfig"
            @change="actualizarHorariosDisponibles" class="form-control datepicker" />
        </div>

        <!-- Selección de hora -->
        <div v-if="horariosDisponibles.length > 0" class="mb-4">
          <label for="hora" class="form-label fs-6 fw-semibold text-secondary">Selecciona Hora:</label>
          <select v-model="cita.hora" id="hora" class="form-select">
            <option v-for="hora in horariosDisponibles" :key="hora" :value="hora">
              {{ hora }}
            </option>
          </select>
        </div>
        <div v-else class="text-muted mb-4">
          <p>No hay horarios disponibles para la fecha seleccionada.</p>
        </div>

        <!-- Selección de servicio -->
        <div class="mb-4">
          <label for="servicio" class="form-label fs-6 fw-semibold text-secondary">Selecciona Servicio:</label>
          <select v-model="cita.servicioId" id="servicio" class="form-select">
            <option v-for="servicio in servicios" :key="servicio.id" :value="servicio.id">
              {{ servicio.nombre }}
            </option>
          </select>
        </div>

        <!-- Selección de preferencias -->
        <div class="mb-4">
          <label for="preferencias" class="form-label fs-6 fw-semibold text-secondary">Preferencias (Opcional):</label>
          <textarea v-model="cita.preferencias" id="preferencias" class="form-control" rows="3"
            placeholder="Escribe aquí tus preferencias de servicio..."></textarea>
        </div>

        <!-- Botón de continuar -->
        <div class="d-grid">
          <button @click="continuarPago" :disabled="!cita.fecha || !cita.hora || !cita.servicioId"
            class="btn btn-primary btn-lg rounded-pill">
            Continuar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import flatPickr from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";
import { Spanish } from "flatpickr/dist/l10n/es.js";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ServicesRepository from "@/repositories/ServicesRepository";
import HorariosBarberosRepository from "@/repositories/HorariosBarberosRepository";
import PromocionRepository from "@/repositories/PromocionRepository";

export default {
  components: {
    flatPickr,
  },
  data() {
    return {
      barberos: [],
      servicios: [],
      horariosDisponibles: [],
      cita: {
        barberoId: null,
        servicioId: null,
        fecha: null,
        hora: null,
        preferencias: "",
      },
      flatpickrConfig: {
        enableTime: false,
        dateFormat: "Y-m-d",
        minDate: "today",
        locale: Spanish,
      },
    };
  },
  mounted() {
    this.cargarBarberos();
    this.cargarServicios();
  },
  methods: {
    async cargarBarberos() {
      try {
        const barberos = await UsuarioRepository.findAllBarberos();
        this.barberos = barberos.sort((a, b) => a.nombre.localeCompare(b.nombre));
      } catch (error) {
        console.error("Error cargando barberos:", error);
      }
    },
    async cargarServicios(fechaSeleccionada = null) {
      try {
        const servicios = await ServicesRepository.findAllServicios();
        const fecha = fechaSeleccionada || this.cita.fecha || new Date().toISOString().split("T")[0];

        for (let servicio of servicios) {
          const promociones = await PromocionRepository.getPromociones();
          servicio.promocionAplicable = promociones.find(
            (promocion) =>
              promocion.servicioId === servicio.id &&
              promocion.activo &&
              new Date(fecha) >= new Date(promocion.fechaInicio) &&
              new Date(fecha) <= new Date(promocion.fechaFin)
          );

          if (servicio.promocionAplicable) {
            servicio.precioConDescuento =
              servicio.precio -
              (servicio.precio * servicio.promocionAplicable.porcentajeDescuento) / 100;
          } else {
            servicio.precioConDescuento = servicio.precio;
          }
        }

        this.servicios = servicios.sort((a, b) => a.nombre.localeCompare(b.nombre));
      } catch (error) {
        console.error("Error cargando servicios:", error);
      }
    },
    async actualizarHorariosDisponibles() {
      if (!this.cita.barberoId || !this.cita.fecha) {
        console.log("Barbero o fecha no seleccionados");
        return;
      }

      try {
        const horarios = await HorariosBarberosRepository.obtenerSlotsDisponibles(
          this.cita.barberoId,
          this.cita.fecha
        );
        this.horariosDisponibles = horarios;

        // Recalcular servicios y precios según la fecha seleccionada
        await this.cargarServicios(this.cita.fecha);
      } catch (error) {
        console.error("Error obteniendo horarios disponibles:", error);
      }
    },
    continuarPago() {
      if (!this.cita.barberoId || !this.cita.servicioId || !this.cita.fecha || !this.cita.hora) {
        alert("Por favor, completa todos los campos antes de continuar.");
        return;
      }

      const formattedHora = this.cita.hora.length === 5 ? this.cita.hora : this.cita.hora.slice(0, 5);
      this.cita.hora = formattedHora;

      this.$router.push({
        name: "PaymentScreen",
        query: { cita: JSON.stringify(this.cita) },
      });
    },
  },
};
</script>

<style scoped>
.card {
  border: none;
  border-radius: 15px;
  background-color: #f8f9fa;
}

.card-title {
  font-weight: bold;
}

.form-control,
.form-select {
  border-radius: 10px;
  background-color: #f8f9fa;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;
}

.form-control:focus,
.form-select:focus {
  box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
  border-color: #007bff;
}

.form-label {
  font-size: 1rem;
  font-weight: 600;
  color: #6c757d;
}

.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
}

.btn-primary:hover {
  background-color: #0056b3;
  border-color: #004085;
}
</style>
