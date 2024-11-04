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
          <tr v-for="empleado in empleados" :key="empleado.id">
            <td>{{ empleado.id }}</td>
            <td>{{ empleado.nombre }}</td>
            <td>{{ empleado.apellido }}</td>
            <td>{{ empleado.telefono }}</td>
            <td>{{ empleado.puesto }}</td>
            <td>{{ empleado.edad }}</td>
            <td>{{ empleado.salario }}</td>
            <td>{{ empleado.contrato }}</td>
            <td>
              <!-- Botón para eliminar el empleado -->
              <button
                @click="eliminarEmpleado(empleado.id)"
                class="btn btn-danger"
              >
                Despedir
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
    // Llamada al servicio para obtener la lista de empleados al montar el componente
    this.obtenerEmpleados();
  },
  methods: {
    // Método para obtener la lista de empleados
    async obtenerEmpleados() {
      try {
        const response = await UsuarioRepository.findAllEmpleados();
        this.empleados = response;
      } catch (error) {
        console.error("Error obteniendo la lista de empleados", error);
      }
    },
    // Método para eliminar un empleado
    async eliminarEmpleado(id) {
      if (confirm("¿Estás seguro de que deseas eliminar este empleado?")) {
        try {
          await UsuarioRepository.delete(id);
          alert("Empleado eliminado con éxito.");
          this.obtenerEmpleados(); // Recargar la lista de empleados después de la eliminación
        } catch (error) {
          console.error("Error eliminando el empleado", error);
          alert("Hubo un error al eliminar el empleado.");
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
</style>
