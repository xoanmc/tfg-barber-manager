<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">

    <!-- Banner de éxito animado -->
    <div v-if="showBanner" class="alert alert-success text-center fade-in" role="alert">
      <p class="mb-2 fw-bold">¡Reserva realizada con éxito!</p>
      <p>Recibirá un mail cuando el barbero confirme la cita.</p>
      <button class="btn btn-primary mt-3" @click="redirectToHome">Aceptar</button>
    </div>

    <!-- Formulario de pago -->
    <div v-else class="card shadow-lg w-100 mx-3" style="max-width: 500px; border-radius: 15px;">
      <div class="card-body p-4">
        <h2 class="card-title text-center text-primary mb-4">Confirma tu Reserva</h2>

        <!-- Resumen de la cita -->
        <div class="mb-4">
          <h3 class="fs-5 fw-semibold text-secondary mb-3">Resumen de la cita:</h3>
          <p><strong>Barbero: </strong> {{ cita.barberoId ? obtenerNombreBarbero(cita.barberoId) : "Cualquiera" }}</p>
          <p><strong>Servicio: </strong> {{ obtenerNombreServicio(cita.servicioId) }}</p>
          <p><strong>Precio: </strong>
            <span v-if="precioConDescuento !== precioOriginal">
              <s class="text-danger fw-bold me-2">{{ precioOriginal.toFixed(2) }} €</s>
              <span class="text-success fw-bold">{{ precioConDescuento.toFixed(2) }} €</span>
            </span>
            <span v-else>
              {{ precioOriginal.toFixed(2) }} €
            </span>
          </p>
          <p><strong>Fecha:</strong> {{ cita.fecha }}</p>
          <p><strong>Hora:</strong> {{ cita.hora }}</p>
        </div>

        <!-- Selección de método de pago -->
        <div class="mb-4 text-center">
          <h3 class="fs-5 fw-semibold text-secondary mb-3">¿Deseas pagar ahora con PayPal?</h3>
          <div class="d-flex justify-content-center">
            <label class="switch">
              <input type="checkbox" v-model="metodoPago" true-value="paypal" false-value="sin_pago" />
              <span class="slider"></span>
            </label>
          </div>
        </div>

        <!-- Botón de PayPal -->
        <div v-if="metodoPago === 'paypal'" class="mb-4">
          <PayPalButton :amount="precioConDescuento" @pago-completado="reservarCita" @pago-error="handlePayPalError" />
        </div>

        <!-- Botón de Reservar -->
        <div class="d-grid">
          <button @click="reservarCita" :disabled="metodoPago === 'paypal'" class="btn btn-primary btn-lg rounded-pill">
            Reservar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import PayPalButton from "@/components/Cita/PayPalButton";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ServicesRepository from "@/repositories/ServicesRepository";
import CitaRepository from "@/repositories/CitaRepository";
import PromocionRepository from "@/repositories/PromocionRepository";

export default {
  components: {
    PayPalButton,
  },
  data() {
    return {
      showBanner: false,
      cita: {},
      barberos: [],
      servicios: [],
      precioOriginal: 0,
      precioConDescuento: 0,
      metodoPago: "sin_pago", // 'paypal' o 'sin_pago'
      alertaTitulo: "",
      alertaMensaje: "",
    };
  },
  async mounted() {
    const citaData = this.$route.query.cita ? JSON.parse(this.$route.query.cita) : null;

    if (!citaData) {
      console.error("No se recibieron datos de la cita. Redirigiendo a la página de reserva.");
      this.$router.push({ name: "reserve" });
      return;
    }

    this.cita = citaData;
    console.log("Cita recibida en PaymentScreen:", this.cita);

    await this.cargarDatos();
  },
  methods: {
    async cargarDatos() {
      try {
        this.barberos = await UsuarioRepository.findAllBarberos();
        this.servicios = await ServicesRepository.findAllServicios();

        const servicioSeleccionado = this.servicios.find(
          (servicio) => servicio.id === this.cita.servicioId
        );

        if (servicioSeleccionado) {
          this.precioOriginal = servicioSeleccionado.precio;

          const promociones = await PromocionRepository.getPromociones(
            servicioSeleccionado.id,
            this.cita.fecha
          );

          const promocionesValidas = promociones.filter((promocion) => {
            const fechaActual = new Date(this.cita.fecha);
            return (
              promocion.servicioId === servicioSeleccionado.id && 
              promocion.activo &&
              fechaActual >= new Date(promocion.fechaInicio) &&
              fechaActual <= new Date(promocion.fechaFin)
            );
          });

          if (promocionesValidas.length > 0) {
            const mayorPromocion = promocionesValidas.reduce((max, promocion) =>
              promocion.porcentajeDescuento > max.porcentajeDescuento ? promocion : max
            );
            const descuento = mayorPromocion.porcentajeDescuento / 100;
            this.precioConDescuento = this.precioOriginal * (1 - descuento);
          } else {
            this.precioConDescuento = this.precioOriginal; 
          }
        }
      } catch (error) {
        console.error("Error cargando datos:", error);
      }
    },
    obtenerNombreBarbero(barberoId) {
      const barbero = this.barberos.find((b) => b.id === barberoId);
      return barbero ? barbero.nombre : "Cualquiera";
    },
    obtenerNombreServicio(servicioId) {
      const servicio = this.servicios.find((s) => s.id === servicioId);
      return servicio ? servicio.nombre : "";
    },
    async reservarCita() {
      try {
        console.log("Reservando cita con datos:", this.cita);
        await CitaRepository.reservarCita(this.cita);
        this.showBanner = true;
      } catch (error) {
        console.error("Error al reservar la cita:", error);
        alert("Ocurrió un error al reservar la cita. Por favor, inténtelo de nuevo.");
      }
    },
    handlePayPalError(error) {
      console.error("Error con PayPal:", error);
      alert("Hubo un problema con el pago de PayPal. Intente de nuevo.");
    },
    redirectToHome() {
      this.$router.push({ name: "Home" });
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

.alert {
  max-width: 400px;
  margin: 0 auto;
  border-radius: 15px;
  animation: fadeIn 0.5s ease-in-out;
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

.switch {
  position: relative;
  display: inline-block;
  width: 34px;
  height: 20px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 34px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 14px;
  width: 14px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked+.slider {
  background-color: #007bff;
}

input:checked+.slider:before {
  transform: translateX(14px);
}

.text-danger {
  color: red !important;
}

.text-success {
  color: green !important;
}

.fw-bold {
  font-weight: bold;
}

.me-2 {
  margin-right: 0.5rem;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
