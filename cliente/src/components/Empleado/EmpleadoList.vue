<template>
  <div class="container">
    <h1>Lista de Empleados</h1>

    <!-- Botón para redirigir a la página de registro de empleados -->
    <div class="float-end">
      <router-link class="btn btn-success" :to="{ name: 'EmpleadoCreate' }">
        Contratar
      </router-link>
    </div>

    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Teléfono</th>
            <th>Puesto</th>
            <th>Edad</th>
            <th>Salario</th>
            <th>Contrato</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr 
            v-for="empleado in empleados" 
            :key="empleado.id" 
            :class="{ 'table-secondary': empleado.despedido }"
          >
            <td>{{ empleado.id }}</td>
            <td>{{ empleado.nombre }}</td>
            <td>{{ empleado.apellido }}</td>
            <td>{{ empleado.telefono }}</td>
            <td>{{ empleado.puesto }}</td>
            <td>{{ empleado.edad }}</td>
            <td>{{ empleado.salario }}</td>
            <td>{{ empleado.contrato }}</td>
            <td>
              <button
                :disabled="empleado.despedido" 
                @click="despedirEmpleado(empleado.id)"
                class="btn"
                :class="empleado.despedido ? 'btn-secondary' : 'btn-warning'"
              >
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
      empleados: [], // Array para almacenar los empleados
    };
  },
  mounted() {
    this.obtenerEmpleados(); // Llamada al servicio para obtener la lista de empleados al montar el componente
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
    async despedirEmpleado(id) {
      if (confirm("¿Estás seguro de que deseas despedir a este empleado?")) {
        try {
          await UsuarioRepository.despedir(id);
          alert("Empleado despedido con éxito.");
          // Actualiza el estado de 'despedido' directamente en el array de empleados
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
  width: 100%;
  margin-top: 20px;
}

.float-end {
  margin-bottom: 10px;
}

.table-secondary {
  background-color: #f8d7da;
  opacity: 0.8;
}

.btn-secondary {
  cursor: not-allowed;
  background-color: #6c757d !important;
  border-color: #6c757d !important;
}
</style>
