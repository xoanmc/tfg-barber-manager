<template>
  <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow-lg w-100" style="max-width: 500px; border-radius: 15px;">
      <div class="card-body p-4">
        <h2 class="text-center text-primary fw-bold mb-4">
          Edita tu perfil de: {{ profile.autoridad }}
        </h2>

        <form @submit.prevent="handleEditProfile">
          <div class="mb-3">
            <label for="nombre" class="form-label">Nombre</label>
            <input
              type="text"
              id="nombre"
              v-model="profile.nombre"
              class="form-control"
              placeholder="Ingresa tu nombre"
              required
            />
          </div>

          <div class="mb-3">
            <label for="apellido" class="form-label">Apellido</label>
            <input
              type="text"
              id="apellido"
              v-model="profile.apellido"
              class="form-control"
              placeholder="Ingresa tu apellido"
              required
            />
          </div>

          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input
              type="email"
              id="email"
              v-model="profile.email"
              class="form-control"
              placeholder="Ingresa tu email"
              required
            />
          </div>

          <div class="mb-3">
            <label for="telefono" class="form-label">Teléfono</label>
            <input
              type="text"
              id="telefono"
              v-model="profile.telefono"
              class="form-control"
              placeholder="Ingresa tu teléfono"
              required
            />
          </div>

          <div v-if="profile.autoridad === 'EMPLEADO'" class="mb-3">
            <label for="descripcion" class="form-label">Descripción</label>
            <textarea
              id="descripcion"
              v-model="profile.descripcion"
              class="form-control"
              placeholder="Describe tu rol o especialidad"
              rows="3"
            ></textarea>
          </div>

          <div v-if="profile.autoridad === 'EMPLEADO'" class="mb-4">
            <h4>Horario de Trabajo</h4>

            <div class="mb-3">
              <label for="rangoDias" class="form-label">Rango de días:</label>
              <div class="d-flex">
                <select v-model="nuevoHorario.diaInicio" id="rangoDiasInicio" class="form-select me-2">
                  <option value="Lunes">Lunes</option>
                  <option value="Martes">Martes</option>
                  <option value="Miércoles">Miércoles</option>
                  <option value="Jueves">Jueves</option>
                  <option value="Viernes">Viernes</option>
                  <option value="Sábado">Sábado</option>
                  <option value="Domingo">Domingo</option>
                </select>
                <select v-model="nuevoHorario.diaFin" id="rangoDiasFin" class="form-select">
                  <option value="Lunes">Lunes</option>
                  <option value="Martes">Martes</option>
                  <option value="Miércoles">Miércoles</option>
                  <option value="Jueves">Jueves</option>
                  <option value="Viernes">Viernes</option>
                  <option value="Sábado">Sábado</option>
                  <option value="Domingo">Domingo</option>
                </select>
              </div>
            </div>

            <div class="mb-3">
              <label for="horaInicio" class="form-label">Hora Inicio:</label>
              <input type="time" id="horaInicio" v-model="nuevoHorario.horaInicio" class="form-control" />
            </div>
            <div class="mb-3">
              <label for="horaFin" class="form-label">Hora Fin:</label>
              <input type="time" id="horaFin" v-model="nuevoHorario.horaFin" class="form-control" />
            </div>
          </div>

          <div class="d-grid mt-4">
            <button type="submit" class="btn btn-success btn-lg rounded-pill">
              Guardar
            </button>
          </div>
        </form>

        <div v-if="error" class="text-danger mt-3 text-center">{{ error }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import HorariosBarberosRepository from "@/repositories/HorariosBarberosRepository";

export default {
  data() {
    return {
      profile: {
        autoridad: "",
        nombre: "",
        apellido: "",
        email: "",
        telefono: "",
        descripcion: "",
      },
      nuevoHorario: {
        diaInicio: "Lunes",
        diaFin: "Lunes",
        horaInicio: "",
        horaFin: "",
      },
      error: "",
    };
  },
  async mounted() {
    try {
      const user = await AccountRepository.getAccount();
      this.profile = { ...this.profile, ...user };
    } catch (e) {
      console.error(e);
      this.error = "No se pudo cargar la información del perfil.";
    }
  },
  methods: {
    generarRangoDias(diaInicio, diaFin) {
      const dias = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"];
      const inicioIndex = dias.indexOf(diaInicio);
      const finIndex = dias.indexOf(diaFin);

      if (inicioIndex === -1 || finIndex === -1) {
        alert("Días seleccionados no válidos.");
        return [];
      }

      if (inicioIndex <= finIndex) {
        return dias.slice(inicioIndex, finIndex + 1);
      } else {
        return [...dias.slice(inicioIndex), ...dias.slice(0, finIndex + 1)];
      }
    },
    async handleEditProfile() {
      try {
        const rangoDias = this.generarRangoDias(this.nuevoHorario.diaInicio, this.nuevoHorario.diaFin);

        if (this.profile.autoridad === "EMPLEADO" && rangoDias.length > 0) {
          const horariosTransformados = rangoDias.map((dia) => ({
            diaSemana: dia,
            horaInicio: this.nuevoHorario.horaInicio,
            horaFin: this.nuevoHorario.horaFin,
          }));

          await HorariosBarberosRepository.actualizarHorario(this.profile.id, horariosTransformados);
        }

        await AccountRepository.updateAccount(this.profile);
        alert("Perfil actualizado con éxito.");
        const redirectPath =
          this.profile.autoridad === "CLIENTE"
            ? "/perfilCliente"
            : this.profile.autoridad === "EMPLEADO"
            ? "/perfilEmpleado"
            : "/perfilJefe";
        this.$router.push(redirectPath);
      } catch (e) {
        console.error(e);
        this.error = e.response?.data?.message || "Error al guardar los cambios.";
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

.btn-success {
  background-color: #28a745;
  border-color: #28a745;
}

.btn-success:hover {
  background-color: #218838;
  border-color: #1e7e34;
}

.text-danger {
  font-size: 0.9rem;
}
</style>
