<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow-lg" style="max-width: 400px; width: 100%; border-radius: 15px;">
      <div class="card-body p-4">
        <h4 class="text-center text-primary mb-4">Recupera tu contraseña</h4>
        <form @submit.prevent="enviarSolicitudRecuperacion">
          <div class="mb-4">
            <label for="email" class="form-label fs-5 fw-semibold text-secondary">
              Ingresa tu correo electrónico
            </label>
            <input type="email" id="email" class="form-control" v-model="email" placeholder="correo@ejemplo.com"
              required />
          </div>

          <div class="d-grid">
            <button type="submit" class="btn btn-primary btn-lg rounded-pill">
              Enviar Instrucciones
            </button>
          </div>
        </form>

        <div class="text-center mt-4">
          <router-link to="/login" class="text-muted small">
            Volver a iniciar sesión
          </router-link>
        </div>
      </div>
    </div>

    <div v-if="mostrarAlerta" class="modal d-flex align-items-center justify-content-center"
      style="background-color: rgba(0,0,0,0.5); position: fixed; top: 0; left: 0; width: 100%; height: 100%; z-index: 1050;">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ alertaTitulo }}</h5>
            <button type="button" class="btn-close" @click="cerrarAlerta"></button>
          </div>
          <div class="modal-body">
            <p>{{ alertaMensaje }}</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="cerrarAlerta">Aceptar</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PasswordRecoveryRepository from "@/repositories/PasswordRecoveryRepository";

export default {
  data() {
    return {
      email: "",
      mostrarAlerta: false,
      alertaTitulo: "",
      alertaMensaje: "",
    };
  },
  methods: {
    async enviarSolicitudRecuperacion() {
      try {
        await PasswordRecoveryRepository.solicitarRecuperacion(this.email);
        this.mostrarAlertaPersonalizada("¡Éxito!", "Hemos enviado un correo con las instrucciones para recuperar tu contraseña.");
      } catch (e) {
        console.error(e);
        this.mostrarAlertaPersonalizada("Error", "Hubo un error al procesar tu solicitud. Por favor, inténtalo nuevamente.");
      }
    },
    mostrarAlertaPersonalizada(titulo, mensaje) {
      this.alertaTitulo = titulo;
      this.alertaMensaje = mensaje;
      this.mostrarAlerta = true;
    },
    cerrarAlerta() {
      this.mostrarAlerta = false;
      if (this.alertaTitulo === "¡Éxito!") {
        this.$router.push("/login"); // Redirigir solo si la operación fue exitosa
      }
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

.card-body {
  padding: 2rem;
}

h4 {
  font-weight: bold;
  color: #007bff;
}

.form-control {
  border-radius: 10px;
  background-color: #f8f9fa;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;
}

.form-control:focus {
  box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
  border-color: #007bff;
}

.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
}

.btn-primary:hover {
  background-color: #0056b3;
  border-color: #004085;
}

.text-muted a {
  text-decoration: none;
  color: #6c757d;
}

.text-muted a:hover {
  color: #0056b3;
  text-decoration: underline;
}

.modal {
  z-index: 1050 !important;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close {
  border: none;
  background-color: transparent;
}
</style>