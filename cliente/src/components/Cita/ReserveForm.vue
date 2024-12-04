<template>
  <div class="reserva-container">
    <h2 class="reserva-titulo">Reserva tu Cita</h2>

    <div class="form-group">
      <label for="barbero" class="form-label">Selecciona Barbero:</label>
      <select v-model="cita.barberoId" id="barbero" class="form-select">
        <option
          v-for="barbero in barberos"
          :key="barbero.id"
          :value="barbero.id"
        >
          {{ barbero.nombre }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="servicio" class="form-label">Selecciona Servicio:</label>
      <select v-model="cita.servicioId" id="servicio" class="form-select">
        <option
          v-for="servicio in servicios"
          :key="servicio.id"
          :value="servicio.id"
        >
          {{ servicio.nombre }} - {{ servicio.precio }}€
        </option>
      </select>
    </div>

    <div class="datepicker-container">
      <label for="fecha" class="form-label">Selecciona Fecha y Hora:</label>
      <flat-pickr
        v-model="cita.fechaHora"
        :config="flatpickrConfig"
        class="datepicker"
      />
    </div>

    <div>
      <button @click="reservarCita" :disabled="!cita.fechaHora || !cita.servicioId">
        Reservar
      </button>
    </div>
  </div>
</template>

<script>
import flatPickr from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";
import { Spanish } from "flatpickr/dist/l10n/es.js";
import CitaRepository from "@/repositories/CitaRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ServicesRepository from "@/repositories/ServicesRepository";

export default {
  components: {
    flatPickr,
  },
  data() {
    return {
      barberos: [],
      servicios: [],
      cita: {
        barberoId: null,
        servicioId: null,
        fechaHora: null,
        clienteId: 1,
      },
      flatpickrConfig: {
  enableTime: true,
  dateFormat: "Y-m-d\\TH:i:S", //cambiar formato a 'yyyy-MM-ddTHH:mm:ss'
  minDate: "today",
  time_24hr: true,
  minuteIncrement: 30,
  locale: Spanish,
}

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
        console.error("Error cargando barberos", error);
      }
    },
    async cargarServicios() {
      try {
        const servicios = await ServicesRepository.findAllServicios();
        this.servicios = servicios.sort((a, b) => a.nombre.localeCompare(b.nombre));
      } catch (error) {
        console.error("Error cargando servicios", error);
      }
    },
    async reservarCita() {
      try {
        const citaData = {
          barbero: { id: this.cita.barberoId, autoridad: "EMPLEADO" },
          servicio: { id: this.cita.servicioId },
          fechaHora: this.cita.fechaHora,
          cliente: { id: this.cita.clienteId, autoridad: "CLIENTE" },
        };
        console.log("Datos de la cita que se enviarán:", citaData);
        await CitaRepository.reservarCita(citaData);
        alert("Cita reservada con éxito");
      } catch (error) {
        console.error("Error reservando cita", error);
        alert("Hubo un error al reservar la cita");
      }
    },
  },
};
</script>


<style scoped>
.reserva-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
  font-family: Arial, sans-serif;
  padding: 20px;
}

.reserva-titulo {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 30px;
  margin-top: 10px;
  color: #333;
}

/* ajustes para pantallas pequeñas (menos de 768px) */
@media (max-width: 768px) {
  .reserva-titulo {
    margin-top: 20px;
    font-size: 1.8rem;
  }
}

/* ajustes para pantallas muy pequeñas (menos de 480px) */
@media (max-width: 480px) {
  .reserva-titulo {
    margin-top: 30px; /* Espacio adicional para teléfonos más compactos */
    font-size: 1.6rem; /* Ajustar tamaño del texto aún más */
  }
}

.form-label {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 10px;
  display: block;
  text-align: left;
  color: #444;
}

.form-group {
  margin-bottom: 20px;
  width: 100%;
  max-width: 400px;
  text-align: left;
}

.form-select {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-size: 1rem;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.datepicker-container {
  margin: 20px 0;
}

button {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
}

button:hover {
  background-color: #0056b3;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>
