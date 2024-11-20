<template>
  <div class="register-container">
    <h2>Registrarse</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="name">Nombre:</label>
        <input type="text" id="name" v-model="nombre" class="form-control" required />
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
        <label for="email">Email:</label>
        <input
          type="email"
          id="email"
          v-model="email"
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
      <div class="form-group">
        <label>Fecha de Nacimiento:</label>
        <div class="fecha-nacimiento">
          <select v-model="fechaNacimiento.dia" required>
            <option disabled value="">Día</option>
            <option v-for="dia in 31" :key="dia" :value="dia">{{ dia }}</option>
          </select>
          <select v-model="fechaNacimiento.mes" required>
            <option disabled value="">Mes</option>
            <option v-for="(mes, index) in meses" :key="index" :value="index + 1">{{ mes }}</option>
          </select>
          <select v-model="fechaNacimiento.anio" required>
            <option disabled value="">Año</option>
            <option v-for="anio in anios" :key="anio" :value="anio">{{ anio }}</option>
          </select>
        </div>
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
      email: "", // Nuevo campo
      telefono: "",
      fechaNacimiento: { dia: "", mes: "", anio: "" }, // Fecha de nacimiento
      login: "",
      password: "",
      confirmPassword: "",
      error: "",
      meses: [
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
      anios: Array.from({ length: 100 }, (_, i) => new Date().getFullYear() - i), // Últimos 100 años
    };
  },
  methods: {
    async handleRegister() {
      try {
        if (this.password !== this.confirmPassword) {
          this.error = "Las contraseñas no coinciden.";
          return;
        }

        const fechaNacimiento = `${this.fechaNacimiento.anio}-${this.fechaNacimiento.mes}-${this.fechaNacimiento.dia}`;
        await auth.registerCliente({
          nombre: this.nombre,
          apellido: this.apellido,
          email: this.email, // Nuevo campo
          telefono: this.telefono,
          fechaNacimiento, // Formateo de fecha
          login: this.login,
          password: this.password,
        });

        // Autenticar automáticamente después del registro
        await auth.login({
          login: this.login,
          password: this.password,
        });

        this.$router.push("/"); // Redirige a la página principal tras el registro
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

.fecha-nacimiento select {
  margin-right: 5px;
  padding: 5px;
  font-size: 1em;
}

.btn-primary {
  width: 100%;
}
</style>
