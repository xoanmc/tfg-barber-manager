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
              <input
                type="password"
                id="password"
                class="form-control"
                v-model="password"
                placeholder="Nueva contraseña"
                required
              />
            </div>
  
            <div class="mb-4">
              <label for="confirmPassword" class="form-label fs-5 fw-semibold text-secondary">
                Confirma tu Contraseña
              </label>
              <input
                type="password"
                id="confirmPassword"
                class="form-control"
                v-model="confirmPassword"
                placeholder="Confirma la nueva contraseña"
                required
              />
            </div>
  
            <div class="d-grid">
              <button type="submit" class="btn btn-primary btn-lg rounded-pill">
                Restablecer Contraseña
              </button>
            </div>
          </form>
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
      };
    },
    mounted() {
      this.token = this.$route.query.token;
      console.log("Token capturado:", this.token);
    },
    methods: {
      async restablecerContraseña() {
        if (this.password !== this.confirmPassword) {
          alert("Las contraseñas no coinciden.");
          return;
        }
  
        try {
          await PasswordRecoveryRepository.restablecerContraseña(this.token, this.password);
          alert("Contraseña restablecida con éxito.");
          this.$router.push("/login");
        } catch (err) {
          console.error(err);
          alert("Hubo un error al restablecer tu contraseña. Por favor, inténtalo de nuevo.");
        }
      },
    },
  };
  </script>
  