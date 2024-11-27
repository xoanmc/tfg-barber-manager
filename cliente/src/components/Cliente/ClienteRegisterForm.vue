<template>
  <div class="reserva-container">
    <h2 class="reserva-titulo">Formulario de Registro</h2>
    <form @submit.prevent="handleRegister" class="register-form">
      <!-- Nombre -->
      <div class="form-group">
        <label for="name" class="form-label">Nombre:</label>
        <input type="text" id="name" v-model="nombre" class="form-control" required />
      </div>

      <!-- Apellido -->
      <div class="form-group">
        <label for="apellido" class="form-label">Apellido:</label>
        <input type="text" id="apellido" v-model="apellido" class="form-control" required />
      </div>

      <!-- Email -->
      <div class="form-group">
        <label for="email" class="form-label">Email:</label>
        <input type="email" id="email" v-model="email" class="form-control" required />
      </div>

      <!-- Login -->
      <div class="form-group">
        <label for="login" class="form-label">Login:</label>
        <input type="text" id="login" v-model="login" class="form-control" minlength="4" required />
      </div>

      <!-- Contraseña -->
      <div class="form-group">
        <label for="password" class="form-label">Contraseña:</label>
        <input type="password" id="password" v-model="password" class="form-control" minlength="4" required />
      </div>

      <!-- Fecha de Nacimiento -->
      <div class="form-group">
        <label class="form-label">Fecha de Nacimiento:</label>
        <div class="fecha-nacimiento">
          <select v-model="fechaNacimiento.dia" class="form-control-select" required>
            <option disabled value="">Día</option>
            <option v-for="dia in 31" :key="dia" :value="dia">{{ dia }}</option>
          </select>
          <select v-model="fechaNacimiento.mes" class="form-control-select" required>
            <option disabled value="">Mes</option>
            <option v-for="(mes, index) in meses" :key="index" :value="index + 1">{{ mes }}</option>
          </select>
          <select v-model="fechaNacimiento.anio" class="form-control-select" required>
            <option disabled value="">Año</option>
            <option v-for="anio in anios" :key="anio" :value="anio">{{ anio }}</option>
          </select>
        </div>
      </div>

      <!-- Teléfono -->
      <div class="form-group">
        <label for="telefono" class="form-label">Teléfono:</label>
        <input type="text" id="telefono" v-model="telefono" class="form-control" required />
      </div>

      <!-- Botón Registrarse -->
      <div>
        <button class="btn-primary" type="submit">Registrarse</button>
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
      email: "",
      telefono: "",
      fechaNacimiento: { dia: "", mes: "", anio: "" },
      login: "",
      password: "",
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
      anios: Array.from({ length: 100 }, (_, i) => new Date().getFullYear() - i),
    };
  },
  methods: {
    async handleRegister() {
      try {

        // Limpiar los valores antes de construir la fecha
        const dia = String(this.fechaNacimiento.dia).padStart(2, '0').trim();
        const mes = String(this.fechaNacimiento.mes).padStart(2, '0').trim();
        const anio = String(this.fechaNacimiento.anio).trim();

        // Construir la fecha en formato 'yyyy-MM-dd'
        const fechaNacimiento = `${anio}-${mes}-${dia}`;

        // Realizar el registro
        await auth.registerCliente({
          nombre: this.nombre,
          apellido: this.apellido,
          email: this.email,
          telefono: this.telefono,
          fechaNacimiento,
          login: this.login,
          password: this.password,
        });

        alert("Registro exitoso. Por favor, revisa tu correo para confirmar tu cuenta.");
        this.$router.push("/");
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
/* Contenedor principal */
.reserva-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-family: Arial, sans-serif;
  padding: 20px;
  max-width: 400px;
  margin: 0 auto;
}

/* Título */
.reserva-titulo {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 30px;
  color: #333;
}

/* Estilo de los campos de formulario */
.form-label {
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 5px;
  display: block;
  text-align: left;
  color: #444;
}

.form-group {
  margin-bottom: 20px;
  width: 100%;
}

.form-control {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-size: 1rem;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

/* Estilo para los select en "Fecha de Nacimiento" */
.form-control-select {
  width: 32%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-size: 1rem;
  margin-right: 2%;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.form-control-select:last-child {
  margin-right: 0;
}

/* Botón */
.btn-primary {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  width: 100%;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
