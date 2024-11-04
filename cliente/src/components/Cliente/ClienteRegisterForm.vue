<template>
  <div class="register-container">
    <h2>Registrarse</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="name">Nombre:</label>
        <input type="text" id="name" v-model="nombre" class="form-control" />
      </div>
      <div class="form-group">
        <label for="apellido">Apellido:</label>
        <input
          type="text"
          id="apellido"
          v-model="apellido"
          class="form-control"
          required
        />
      </div>
      <div class="form-group">
        <label for="telefono">Teléfono:</label>
        <input
          type="text"
          id="telefono"
          v-model="telefono"
          class="form-control"
          required
        />
      </div>
      <div class="form-group">
        <label for="edad">Edad:</label>
        <input
          type="number"
          id="edad"
          v-model.number="edad"
          min="0"
          class="form-control"
          required
        />
      </div>
      <div class="form-group">
        <label for="login">Login:</label>
        <input
          type="text"
          id="login"
          v-model="login"
          class="form-control"
          minlength="4"
          required
        />
      </div>
      <div class="form-group">
        <label for="password">Contraseña:</label>
        <input
          type="password"
          id="password"
          v-model="password"
          class="form-control"
          minlength="4"
          required
        />
      </div>
      <div class="form-group">
        <label for="confirmPassword">Confirmar Contraseña:</label>
        <input
          type="password"
          id="confirmPassword"
          v-model="confirmPassword"
          class="form-control"
          required
        />
      </div>
      <div class="d-grid gap-2">
        <button class="btn btn-primary">Registrarse</button>
      </div>
    </form>
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
  </div>
</template>

<script>
import auth from "@/common/auth.js";

export default {
  data() {
    return {
      nombre: "",
      apellido: "",
      telefono: "",
      edad: 0, // Campo edad
      login: "",
      password: "",
      confirmPassword: "",
      error: "",
    };
  },
  methods: {
    async handleRegister() {
      try {
        if (this.password !== this.confirmPassword) {
          this.error = "Las contraseñas no coinciden.";
          return;
        }

        await auth.registerCliente({
          nombre: this.nombre,
          apellido: this.apellido,
          telefono: this.telefono,
          edad: this.edad, // Incluimos el campo edad
          login: this.login,
          password: this.password,
        });

        // autenticar automáticamente después del registro
        await auth.login({
          login: this.login,
          password: this.password,
        });

        this.$router.push("/"); // redirige a la página principal tras del registro
      } catch (e) {
        console.error(e);
        if (e.response?.data?.message) {
          alert(e.response.data.message);
        } else {
          alert(e.message);
        }
      }
    },
  },
};
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.form-group {
  margin-bottom: 15px;
}

.btn-primary {
  width: 100%;
}
</style>
