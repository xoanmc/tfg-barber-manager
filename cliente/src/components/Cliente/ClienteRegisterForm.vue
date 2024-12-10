<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow-lg w-100" style="max-width: 400px; border-radius: 15px;">
      <div class="card-body p-4">
        <h2 class="text-center text-primary fw-bold mb-4">¡Hazte Miembro!</h2>
        <form @submit.prevent="handleRegister">
          <div class="mb-3">
            <label for="name" class="form-label">Nombre*</label>
            <input
              type="text"
              id="name"
              v-model="nombre"
              class="form-control"
              required
            />
          </div>

          <div class="mb-3">
            <label for="apellido" class="form-label">Apellido*</label>
            <input
              type="text"
              id="apellido"
              v-model="apellido"
              class="form-control"
              required
            />
          </div>

          <div class="mb-3">
            <label for="email" class="form-label">Email*</label>
            <input
              type="email"
              id="email"
              v-model="email"
              class="form-control"
              required
            />
          </div>

          <div class="mb-3">
            <label for="login" class="form-label">Login*</label>
            <input
              type="text"
              id="login"
              v-model="login"
              class="form-control"
              minlength="4"
              required
            />
          </div>

          <div class="mb-3">
            <label for="password" class="form-label">Contraseña*</label>
            <input
              type="password"
              id="password"
              v-model="password"
              class="form-control"
              minlength="4"
              required
            />
          </div>

          <div class="mb-3">
            <label for="telefono" class="form-label">Teléfono</label>
            <input
              type="text"
              id="telefono"
              v-model="telefono"
              class="form-control"
              required
            />
          </div>

          <div class="mb-3">
            <label for="profile-picture" class="form-label">Foto de Perfil</label>
            <input
              type="file"
              id="profile-picture"
              class="form-control"
              @change="onFileChange"
            />
          </div>

          <div class="mb-3">
            <label class="form-label">Fecha de Nacimiento*</label>
            <div class="d-flex gap-2">
              <select v-model="fechaNacimiento.dia" class="form-control" required>
                <option disabled value="">Día</option>
                <option v-for="dia in 31" :key="dia" :value="dia">{{ dia }}</option>
              </select>
              <select v-model="fechaNacimiento.mes" class="form-control" required>
                <option disabled value="">Mes</option>
                <option v-for="(mes, index) in meses" :key="index" :value="index + 1">{{ mes }}</option>
              </select>
              <select v-model="fechaNacimiento.anio" class="form-control" required>
                <option disabled value="">Año</option>
                <option v-for="anio in anios" :key="anio" :value="anio">{{ anio }}</option>
              </select>
            </div>
          </div>

          <div class="d-grid">
            <button class="btn btn-primary btn-lg rounded-pill" type="submit">
              Registrarse
            </button>
          </div>
        </form>

        <div v-if="error" class="text-danger mt-3 text-center">
          {{ error }}
        </div>
      </div>
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
      fotoPerfil: null,
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
  onFileChange(event) {
    this.fotoPerfil = event.target.files[0];
  },
  async handleRegister() {
    try {
      // Validación y preparación de datos
      const dia = String(this.fechaNacimiento.dia).padStart(2, "0").trim();
      const mes = String(this.fechaNacimiento.mes).padStart(2, "0").trim();
      const anio = String(this.fechaNacimiento.anio).trim();
      const fechaNacimiento = `${anio}-${mes}-${dia}`;

      const formData = new FormData();
      formData.append("nombre", this.nombre);
      formData.append("apellido", this.apellido);
      formData.append("email", this.email);
      formData.append("telefono", this.telefono);
      formData.append("fechaNacimiento", fechaNacimiento);
      formData.append("login", this.login);
      formData.append("password", this.password);
      if (this.fotoPerfil) {
        formData.append("fotoPerfil", this.fotoPerfil);
      }

      // Redirige inmediatamente al usuario a la pantalla de confirmación
      this.$router.push("/emailConfirmation");

      // Mientras tanto, envía la solicitud al backend
      await auth.registerCliente(formData);

      this.clearForm();
    } catch (e) {
      console.error(e);
      this.error = e.response?.data?.message || "Ocurrió un error inesperado.";
    }
  },
  clearForm() {
    this.nombre = "";
    this.apellido = "";
    this.email = "";
    this.telefono = "";
    this.fechaNacimiento = { dia: "", mes: "", anio: "" };
    this.login = "";
    this.password = "";
    this.fotoPerfil = null;
  },
},

    clearForm() {
      this.nombre = "";
      this.apellido = "";
      this.email = "";
      this.telefono = "";
      this.fechaNacimiento = { dia: "", mes: "", anio: "" };
      this.login = "";
      this.password = "";
      this.fotoPerfil = null;
    },
  }
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

h2 {
  font-size: 1.8rem;
  font-weight: bold;
}

.form-label {
  font-size: 1rem;
  font-weight: 600;
  color: #6c757d;
}

.form-control {
  border-radius: 10px;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
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

.text-danger {
  font-size: 0.9rem;
}
</style>
