<template>
  <div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Plantilla de Empleados</h1>
      <router-link class="btn btn-success btn-lg" :to="{ name: 'EmpleadoCreate' }">
        Contratar
      </router-link>
    </div>

    <div class="table-responsive">
      <table class="table table-striped table-hover align-middle">
        <thead class="table-dark">
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Nombre</th>
            <th scope="col">Apellido</th>
            <th scope="col">Login</th>
            <th scope="col">Email</th>
            <th scope="col">Teléfono</th>
            <th scope="col">Puesto</th>
            <th scope="col">Salario</th>
            <th scope="col">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="empleado in empleados" :key="empleado.id"
            :class="{ 'table-warning': empleado.despedido, 'clickable-row': !empleado.despedido }"
            @click="verPerfilBarbero(empleado.login)">
            <td>{{ empleado.id }}</td>
            <td>{{ empleado.nombre }}</td>
            <td>{{ empleado.apellido }}</td>
            <td>{{ empleado.login }}</td>
            <td>{{ empleado.email }}</td>
            <td>{{ empleado.telefono }}</td>
            <td>{{ empleado.puesto }}</td>
            <td>{{ empleado.salario }}</td>
            <td>
              <button :disabled="empleado.despedido" @click.stop="despedirEmpleado(empleado.id)" class="btn btn-sm"
                :class="empleado.despedido ? 'btn-secondary' : 'btn-danger'">
                {{ empleado.despedido ? "Despedido" : "Despedir" }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import UsuarioRepository from "@/repositories/UsuarioRepository";

export default {
  data() {
    return {
      empleados: [],
    };
  },
  mounted() {
    this.obtenerEmpleados();
  },
  methods: {
    async obtenerEmpleados() {
      try {
        const response = await UsuarioRepository.findAllEmpleados();
        this.empleados = response;
      } catch (error) {
        console.error("Error obteniendo la lista de empleados", error);
      }
    },
    verPerfilBarbero(login) {
      if (login) {
        this.$router.push({ name: "BarberProfileView", params: { login } });
      }
    },
    async despedirEmpleado(id) {
      if (confirm("¿Estás seguro de que deseas despedir a este empleado?")) {
        try {
          await UsuarioRepository.despedir(id);
          alert("Empleado despedido con éxito.");
          const empleado = this.empleados.find((e) => e.id === id);
          if (empleado) {
            empleado.despedido = true;
          }
        } catch (error) {
          console.error("Error al despedir al empleado", error);
          alert("Hubo un error al despedir al empleado.");
        }
      }
    },
  },
};
</script>

<style scoped>
.container {
  padding: 20px;
}

.table {
  margin-top: 20px;
  border-radius: 8px;
  overflow: hidden;
}

.table-warning {
  background-color: #fff3cd !important;
}

.btn-danger {
  background-color: #dc3545 !important;
  border-color: #dc3545 !important;
}

.btn-danger:hover {
  background-color: #c82333 !important;
  border-color: #bd2130 !important;
}

.btn-secondary {
  cursor: not-allowed;
  background-color: #6c757d !important;
  border-color: #6c757d !important;
}

h1 {
  color: #f8f9fa;
  margin-bottom: 0;
  padding-top: 100px;
  font-size: 2rem;
  font-weight: bold;
}

.table-dark th {
  color: #fff;
  background-color: #343a40;
}

.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
}

.clickable-row {
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}

.clickable-row:hover {
  background-color: #f1f1f1; /* Color más claro al pasar el cursor */
}

</style>
