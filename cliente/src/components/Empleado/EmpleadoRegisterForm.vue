<template>
  <div class="register-container">
    <h2>Registrar Empleado</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="name">Nombre:</label>
        <input
          type="text"
          id="name"
          v-model="nombre"
          class="form-control"
          required
        />
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
        <label for="puesto">Puesto:</label>
        <input
          type="text"
          id="puesto"
          v-model="puesto"
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
          class="form-control"
          min="0"
          required
        />
      </div>
      <div class="form-group">
        <label for="salario">Salario:</label>
        <input
          type="number"
          id="salario"
          v-model.number="salario"
          step="50"
          min="0"
          class="form-control"
          required
        />
      </div>
      <div class="form-group">
        <label for="contrato">Contrato (Fecha de inicio):</label>
        <input
          type="text"
          id="contrato"
          v-model="contrato"
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
        <button class="btn btn-primary">Registrar Empleado</button>
      </div>
    </form>
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";

export default {
  data() {
    return {
      nombre: "",
      apellido: "",
      telefono: "",
      puesto: "",
      edad: 0,
      salario: 0,
      contrato: "",
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

        // Llamar a la API para registrar el nuevo empleado
        await AccountRepository.registerEmpleado({
          nombre: this.nombre,
          apellido: this.apellido,
          telefono: this.telefono,
          puesto: this.puesto,
          edad: this.edad,
          salario: this.salario,
          contrato: this.contrato,
          login: this.login,
          password: this.password,
        });

        // Redirigir a la lista de empleados después del registro exitoso
        this.$router.push("/users/empleados");
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

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
