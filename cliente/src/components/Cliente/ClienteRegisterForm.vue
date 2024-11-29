<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow-lg w-100" style="max-width: 400px; border-radius: 15px;">
      <div class="card-body p-4">
        <h2 class="text-center text-primary fw-bold mb-4">¡Hazte Miembro!</h2>
        <form @submit.prevent="handleRegister">
          <!-- Nombre -->
          <div class="mb-3">
            <label for="name" class="form-label">Nombre</label>
            <input
              type="text"
              id="name"
              v-model="nombre"
              class="form-control"
              required
            />
          </div>

          <!-- Apellido -->
          <div class="mb-3">
            <label for="apellido" class="form-label">Apellido</label>
            <input
              type="text"
              id="apellido"
              v-model="apellido"
              class="form-control"
              required
            />
          </div>

          <!-- Email -->
          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input
              type="email"
              id="email"
              v-model="email"
              class="form-control"
              required
            />
          </div>

          <!-- Login -->
          <div class="mb-3">
            <label for="login" class="form-label">Login</label>
            <input
              type="text"
              id="login"
              v-model="login"
              class="form-control"
              minlength="4"
              required
            />
          </div>

          <!-- Contraseña -->
          <div class="mb-3">
            <label for="password" class="form-label">Contraseña</label>
            <input
              type="password"
              id="password"
              v-model="password"
              class="form-control"
              minlength="4"
              required
            />
          </div>

          <!-- Teléfono -->
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

          <!-- Fecha de Nacimiento -->
          <div class="mb-3">
            <label class="form-label">Fecha de Nacimiento</label>
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

          <!-- Botón Registrarse -->
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
        const dia = String(this.fechaNacimiento.dia).padStart(2, "0").trim();
        const mes = String(this.fechaNacimiento.mes).padStart(2, "0").trim();
        const anio = String(this.fechaNacimiento.anio).trim();
        const fechaNacimiento = `${anio}-${mes}-${dia}`;

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
          this.error = e.response.data.message;
        } else {
          this.error = "Ocurrió un error inesperado.";
        }
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
  margin-top: 70px; /* Asegura que no se superponga al menú superior */
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

/* Ajustes responsivos */
@media (max-width: 768px) {
  .card-body {
    padding: 1.5rem;
  }

  h2 {
    font-size: 1.5rem;
  }

  .btn-primary {
    font-size: 0.9rem;
  }
}
</style>
