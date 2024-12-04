<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
    <div v-if="showBanner" class="alert alert-success text-center fade-in" role="alert">
      <p class="mb-2 fw-bold">¡Empleado registrado exitosamente!</p>
      <p>El empleado ha sido añadido correctamente al sistema.</p>
      <button class="btn btn-primary mt-3" @click="redirectToEmployeeList">Aceptar</button>
    </div>

    <div v-else class="card shadow-lg w-100 mx-3" style="max-width: 400px; border-radius: 15px;">
      <div class="card-body p-4">
        <h2 class="text-center text-primary fw-bold mb-4">Registrar Empleado</h2>
        <form @submit.prevent="handleRegister">
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

          <div class="mb-3">
            <label for="puesto" class="form-label">Puesto</label>
            <input
              type="text"
              id="puesto"
              v-model="puesto"
              class="form-control"
              required
            />
          </div>

          <div class="mb-3">
            <label for="salario" class="form-label">Salario</label>
            <input
              type="number"
              id="salario"
              v-model.number="salario"
              class="form-control"
              step="50"
              min="0"
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

          <div class="d-grid">
            <button class="btn btn-primary btn-lg rounded-pill" type="submit">
              Registrar Empleado
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
import AccountRepository from "@/repositories/AccountRepository";

export default {
  data() {
    return {
      nombre: "",
      apellido: "",
      email: "",
      login: "",
      password: "",
      puesto: "",
      salario: 0,
      telefono: "",
      error: "",
      showBanner: false,
    };
  },
  methods: {
    async handleRegister() {
      try {
        await AccountRepository.registerEmpleado({
          nombre: this.nombre,
          apellido: this.apellido,
          email: this.email,
          login: this.login,
          password: this.password,
          puesto: this.puesto,
          salario: this.salario,
          telefono: this.telefono,
        });

        this.showBanner = true;

        this.clearForm();

        this.error = "";
      } catch (e) {
        console.error(e);
        this.error = e.response?.data?.message || "Ocurrió un error inesperado.";
      }
    },
    clearForm() {
      this.nombre = "";
      this.apellido = "";
      this.email = "";
      this.login = "";
      this.password = "";
      this.puesto = "";
      this.salario = 0;
      this.telefono = "";
    },
    redirectToEmployeeList() {
      this.$router.push("/users/empleados");
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

.alert {
  max-width: 400px;
  margin: 0 auto;
  border-radius: 15px;
  animation: fadeIn 0.5s ease-in-out;
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

/* animación del banner */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .container {
    margin-top: 10vh;
    margin-bottom: 10vh;
  }

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
