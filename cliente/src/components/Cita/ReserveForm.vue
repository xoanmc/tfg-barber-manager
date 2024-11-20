<template>
  <div class="reserva-container">
    <h2>Reserva tu Cita</h2>

    <!-- Lista de barberos disponibles -->
    <div>
      <label for="barbero">Selecciona Barbero:</label>
      <select v-model="cita.barberoId" id="barbero">
        <option
          v-for="barbero in barberos"
          :key="barbero.id"
          :value="barbero.id"
        >
          {{ barbero.nombre }}
        </option>
      </select>
    </div>

    <!-- Lista de servicios disponibles -->
    <div>
      <label for="servicio">Selecciona Servicio:</label>
      <select v-model="cita.servicioId" id="servicio">
        <option
          v-for="servicio in servicios"
          :key="servicio.id"
          :value="servicio.id"
        >
          {{ servicio.nombre }} - {{ servicio.precio }}€
        </option>
      </select>
    </div>

    <!-- Calendario siempre visible con selector de fecha y hora -->
    <div class="datepicker-container">
      <label for="fecha">Selecciona Fecha y Hora:</label>
      <Datepicker
        v-model="cita.fechaHora"
        :inline="true"
        :disable-dates="disablePastDates"
        :enable-time-picker="true"
        :time-picker-options="{ step: 30 }"
        :translations="translations"
        :format="'yyyy-MM-dd HH:mm'"
      />
    </div>

    <!-- Botón para reservar -->
    <div>
      <button @click="reservarCita" :disabled="!cita.fechaHora || !cita.servicioId">
        Reservar
      </button>
    </div>
  </div>
</template>

<script>
import CitaRepository from "@/repositories/CitaRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ServicesRepository from "@/repositories/ServicesRepository";
import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

export default {
  components: {
    Datepicker,
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
      translations: {
        weekDays: ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"],
        months: [
          "Enero",
          "Febrero",
          "Marzo",
          "Abril",
          "Mayo",
          "Junio",
          "Julio",
          "Agosto",
          "Septiembre",
          "Octubre",
          "Noviembre",
          "Diciembre",
        ],
        pickHour: "Selecciona Hora",
        pickMinute: "Selecciona Minutos",
        pickSecond: "Selecciona Segundos",
        ok: "Aceptar",
        cancel: "Cancelar",
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
    disablePastDates(date) {
      return date < new Date();
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
/* Estilo para centrar contenedor principal */
.reserva-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}

/* Centra contenedor del calendario */
.datepicker-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

/* botón esté alineado correctamente */
button {
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 20px;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.datepicker {
  max-width: 400px;
  margin: 0 auto;
}
</style>
