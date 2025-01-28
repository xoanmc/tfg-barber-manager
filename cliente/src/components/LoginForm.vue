<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow-lg" style="max-width: 400px; width: 100%; border-radius: 15px;">
      <div class="card-body p-4">
        <h2 class="text-center text-primary fw-bold mb-4">Iniciar Sesión</h2>
        <form @submit.prevent="autenticarme">
          <div class="mb-4">
            <label for="username" class="form-label fs-5 fw-semibold text-secondary">Login</label>
            <input type="text" id="username" class="form-control" v-model="auxLogin"
              :class="{ 'is-invalid': errors.login }" required />
            <div v-if="errors.login" class="invalid-feedback">
              {{ errors.login }}
            </div>
          </div>

          <div class="mb-4">
            <label for="password" class="form-label fs-5 fw-semibold text-secondary">Contraseña</label>
            <input type="password" id="password" class="form-control" v-model="auxPass"
              :class="{ 'is-invalid': errors.password }" required />
            <div v-if="errors.password" class="invalid-feedback">
              {{ errors.password }}
            </div>
          </div>

          <div v-if="errors.general" class="alert alert-danger text-center mb-4">
            {{ errors.general }}
          </div>

          <div class="text-center mb-4">
            <router-link to="/forgotPassword" class="text-muted small">
              ¿Olvidaste tu contraseña?
            </router-link>
          </div>

          <div class="d-grid">
            <button type="submit" class="btn btn-primary btn-lg rounded-pill">
              Iniciar Sesión
            </button>
          </div>
        </form>

        <div class="text-center mt-4">
          <p class="small">
            ¿Todavía no eres miembro?
            <router-link to="/register" class="text-primary fw-normal"
              style="font-size: 0.95rem;">Regístrate</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import auth from "../common/auth.js";
export default {
  data() {
    return {
      auxLogin: "",
      auxPass: "",
      errors: {
        login: null,
        password: null,
        general: null,
      },
    };
  },
  methods: {
    async autenticarme() {
      // Resetear mensajes de error antes de intentar autenticación
      this.errors = {
        login: null,
        password: null,
        general: null,
      };

      try {
        // Llamada al método de autenticación
        await auth.login({
          login: this.auxLogin,
          password: this.auxPass,
        });

        // Redirigir al inicio si el login es exitoso
        this.$router.push("/");
      } catch (e) {
        // Analizar el tipo de error recibido del servidor
        if (e.response && e.response.status === 401) {
          // Credenciales incorrectas
          this.errors.general = "Credenciales incorrectas. Por favor, verifica tu login y contraseña.";
        } else if (e.response && e.response.status === 404) {
          // Usuario no encontrado
          this.errors.login = "El usuario no existe.";
        } else {
          // Error genérico
          this.errors.general = "Ocurrió un error inesperado. Por favor, inténtalo más tarde.";
        }

        console.error("Error al iniciar sesión:", e);
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

.text-primary {
  font-weight: bold;
  font-size: 1.5rem;
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

.form-label {
  font-size: 1.1rem;
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

.text-muted {
  font-size: 0.9rem;
}

.text-muted a {
  text-decoration: none;
  color: #6c757d;
}

.text-muted a:hover {
  color: #0056b3;
  text-decoration: underline;
}

.text-primary.small {
  font-size: 0.85rem;
}

.vh-100 {
  height: 100vh !important;
}
</style>
