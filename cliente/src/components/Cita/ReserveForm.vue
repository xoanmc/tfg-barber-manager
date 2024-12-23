<template>
  <div class="reserva-container">
    <h2 class="reserva-titulo">Reserva tu Cita</h2>

    <!-- seleccionar barbero -->
    <div class="form-group">
      <label for="barbero" class="form-label">Selecciona Barbero:</label>
      <select v-model="cita.barberoId" id="barbero" class="form-select" @change="actualizarHorariosDisponibles">
        <option value="" selected>Cualquiera</option>
        <option v-for="barbero in barberos" :key="barbero.id" :value="barbero.id">
          {{ barbero.nombre }}
        </option>
      </select>
    </div>

    <!-- seleccionar servicio -->
    <div class="form-group">
      <label for="servicio" class="form-label">Selecciona Servicio:</label>
      <select v-model="cita.servicioId" id="servicio" class="form-select">
        <option v-for="servicio in servicios" :key="servicio.id" :value="servicio.id">
          {{ servicio.nombre }} - {{ servicio.precio }}€
        </option>
      </select>
    </div>

    <!-- seleccionar horario -->
    <div class="datepicker-container">
      <label for="fecha" class="form-label">Selecciona Fecha y Hora:</label>
      <flat-pickr ref="flatpickrInstance" v-model="cita.fechaHora" :config="flatpickrConfig" class="datepicker" />
    </div>

    <!-- botón de reservar -->
    <div>
      <button @click="reservarCita" :disabled="!cita.fechaHora || !cita.servicioId">
        Reservar
      </button>
    </div>

    <!-- botón de PayPal -->
    <div id="paypal-button-container" style="margin-top: 20px;"></div>
  </div>
</template>

<script>
import flatPickr from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";
import { Spanish } from "flatpickr/dist/l10n/es.js";
import CitaRepository from "@/repositories/CitaRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ServicesRepository from "@/repositories/ServicesRepository";
import HorariosBarberosRepository from "@/repositories/HorariosBarberosRepository";

export default {
  components: {
    flatPickr,
  },
  data() {
    return {
      barberos: [],
      servicios: [],
      horariosDisponibles: [], // Horarios traídos del backend
      cita: {
        barberoId: null,
        servicioId: null,
        fechaHora: null,
      },
      flatpickrConfig: {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        minDate: "today",
        time_24hr: true,
        locale: Spanish,
        enable: [], // Fechas habilitadas
        disable: [(date) => date.getDay() === 0], // Deshabilitar domingos
        minuteIncrement: 30,
        minTime: "09:00",
        maxTime: "21:00",
      },
    };
  },
  mounted() {
    this.cargarBarberos();
    this.cargarServicios().then(() => {
      this.initPayPalButton();
    });
    this.actualizarHorariosDisponibles(); // Cargar todos los horarios al inicio
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
    async actualizarHorariosDisponibles() {
      try {
        if (this.cita.barberoId) {
          this.horariosDisponibles = await HorariosBarberosRepository.obtenerHorariosPorBarbero(this.cita.barberoId);
        } else {
          this.horariosDisponibles = await HorariosBarberosRepository.obtenerTodosLosHorarios();
        }

        console.log("Horarios disponibles recibidos:", this.horariosDisponibles);

        if (this.horariosDisponibles && this.horariosDisponibles.length > 0) {
          this.flatpickrConfig.enable = this.generarRangosPermitidos();
        } else {
          this.flatpickrConfig.enable = [];
        }

        this.$nextTick(() => {
          this.$refs.flatpickrInstance.fp.set("enable", this.flatpickrConfig.enable);
        });
      } catch (error) {
        console.error("Error actualizando horarios disponibles", error);
        this.flatpickrConfig.enable = [];
      }
    },
    generarRangosPermitidos() {
      if (!this.horariosDisponibles || this.horariosDisponibles.length === 0) {
        console.warn("No hay horarios disponibles para generar rangos");
        return [];
      }

      const enabledDates = [];
      const diasMap = {
        Lunes: 1,
        Martes: 2,
        Miércoles: 3,
        Jueves: 4,
        Viernes: 5,
        Sábado: 6,
        Domingo: 0,
      };

      const hoy = new Date(); // Fecha actual

      // Iterar por horarios disponibles
      this.horariosDisponibles.forEach((horario) => {
        const { diaSemana, horaInicio, horaFin, barbero } = horario;

        // Si hay un barbero seleccionado, omitir horarios de otros barberos
        if (this.cita.barberoId && barbero.id !== this.cita.barberoId) {
          return;
        }

        // Configurar fecha base
        let fechaBase = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate());

        for (let i = 0; i < 30; i++) { // Próximos 30 días
          if (fechaBase.getDay() === diasMap[diaSemana]) {
            const fechaInicio = new Date(fechaBase);
            fechaInicio.setHours(...horaInicio.split(":").map(Number), 0, 0);

            const fechaFin = new Date(fechaBase);
            fechaFin.setHours(...horaFin.split(":").map(Number), 0, 0);

            // Agregar intervalos de 30 minutos
            let current = new Date(fechaInicio);
            while (current <= fechaFin) {
              enabledDates.push(new Date(current));
              current.setMinutes(current.getMinutes() + 30);
            }
          }
          fechaBase.setDate(fechaBase.getDate() + 1);
        }
      });

      //console.log("Fechas permitidas generadas:", enabledDates);
      return enabledDates;
    },


    async reservarCita() {
      try {
        const fechaSeleccionada = new Date(this.cita.fechaHora);
        let barberoIdSeleccionado = this.cita.barberoId;

        // Verificar si la hora seleccionada está dentro de los horarios permitidos
        const horarioValido = this.horariosDisponibles.some((h) => {
          const horaInicio = new Date(fechaSeleccionada);
          horaInicio.setHours(...h.horaInicio.split(":").map(Number), 0, 0);

          const horaFin = new Date(fechaSeleccionada);
          horaFin.setHours(...h.horaFin.split(":").map(Number), 0, 0);

          const esValido = fechaSeleccionada >= horaInicio && fechaSeleccionada <= horaFin;

          if (esValido && !barberoIdSeleccionado) {
            barberoIdSeleccionado = h.barbero.id;
          }
          return esValido;
        });

        if (!horarioValido) {
          alert("La hora seleccionada no está disponible para este barbero.");
          return;
        }

        // Aquí enviamos solo el ID de barbero y servicio
        const citaData = {
          barberoId: barberoIdSeleccionado,       // Solo el ID del barbero
          servicioId: this.cita.servicioId,       // Solo el ID del servicio
          fechaHora: this.cita.fechaHora,         // Fecha y hora seleccionada
        };

        console.log("Datos de la cita enviados al backend:", citaData);

        await CitaRepository.reservarCita(citaData);
        alert("Cita reservada con éxito");
      } catch (error) {
        console.error("Error reservando cita:", error);
        alert("Hubo un error al reservar la cita");
      }
    },
    initPayPalButton() {
      if (!window.paypal) {
        console.error("PayPal SDK no está cargado.");
        return;
      }
      window.paypal.Buttons({
        createOrder: (data, actions) => {
          // Crear el pedido de PayPal con los detalles del servicio
          const selectedService = this.servicios.find(s => s.id === this.cita.servicioId);
          const amount = selectedService ? selectedService.precio : 0;

          return actions.order.create({
            purchase_units: [
              {
                description: "Pago de Reserva",
                amount: {
                  value: amount.toFixed(2),
                  currency_code: "EUR",
                },
              },
            ],
          });
        },
        onApprove: async (data, actions) => {
          const order = await actions.order.capture();
          console.log("Pago realizado con éxito:", order);

          alert("¡Pago completado! Tu cita ha sido reservada.");
          await this.reservarCita();
        },
        onError: (err) => {
          console.error("Error en PayPal:", err);
          alert("Hubo un problema con el pago.");
        },
      }).render("#paypal-button-container");
    }
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
  color: #333;
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
