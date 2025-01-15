<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow-lg" style="max-width: 400px; width: 100%; border-radius: 15px;">
      <div class="card-body p-4">
        <h4 class="text-center text-primary mb-4">Restablece tu contraseña</h4>
        <form @submit.prevent="restablecerContraseña">
          <div class="mb-4">
            <label for="password" class="form-label fs-5 fw-semibold text-secondary">
              Nueva Contraseña
            </label>
            <input type="password" id="password" class="form-control" v-model="password" placeholder="Nueva contraseña"
              required />
          </div>

          <div class="mb-4">
            <label for="confirmPassword" class="form-label fs-5 fw-semibold text-secondary">
              Confirma tu Contraseña
            </label>
            <input type="password" id="confirmPassword" class="form-control" v-model="confirmPassword"
              placeholder="Confirma la nueva contraseña" required />
          </div>

          <div class="d-grid">
            <button type="submit" class="btn btn-primary btn-lg rounded-pill">
              Restablecer Contraseña
            </button>
          </div>
        </form>
      </div>
    </div>
    <!-- Modal de alerta -->
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
      password: "",
      confirmPassword: "",
      token: "",
      mostrarAlerta: false,
      alertaTitulo: "",
      alertaMensaje: "",
    };
  },
  mounted() {
    this.token = this.$route.query.token;
    console.log("Token capturado:", this.token);
  },
  methods: {
    mostrarAlertaPersonalizada(titulo, mensaje) {
      this.alertaTitulo = titulo;
      this.alertaMensaje = mensaje;
      this.mostrarAlerta = true;
    },
    cerrarAlerta() {
      this.mostrarAlerta = false;
      if (this.alertaTitulo === "¡Éxito!") {
        this.$router.push("/login");
      }
    },
    async restablecerContraseña() {
      if (this.password !== this.confirmPassword) {
        this.mostrarAlertaPersonalizada("Error", "Las contraseñas no coinciden.");
        return;
      }

      try {
        await PasswordRecoveryRepository.restablecerContraseña(this.token, this.password);
        this.mostrarAlertaPersonalizada("¡Éxito!", "Contraseña restablecida con éxito.");
      } catch (err) {
        console.error(err);
        this.mostrarAlertaPersonalizada("Error", "Hubo un error al restablecer tu contraseña. Por favor, inténtalo de nuevo.");
      }
    },
  },
};
</script>

<style scoped>
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