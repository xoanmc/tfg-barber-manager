<template>
  <div class="container">
    <h1>Lista de Clientes</h1>

    <!-- Muestra la tabla solo si hay clientes disponibles -->
    <div class="table-responsive" v-if="clientes.length > 0">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Teléfono</th>
            <th>Edad</th>
            <th>Citas</th>
            <th>Primera Cita</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cliente in clientes" :key="cliente.id">
            <td>{{ cliente.id }}</td>
            <td>{{ cliente.nombre }}</td>
            <td>{{ cliente.apellido }}</td>
            <td>{{ cliente.telefono }}</td>
            <td>{{ cliente.edad ?? "N/A" }}</td>
            <!-- Edad -->
            <td>{{ cliente.citas ?? "N/A" }}</td>
            <!-- Citas (por ahora null) -->
            <td>{{ cliente.primeraCita ?? "N/A" }}</td>
            <!-- Primera cita (por ahora null) -->
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Mensaje mostrado mientras se cargan los clientes -->
    <div v-else>
      <p>Cargando lista de clientes...</p>
    </div>
  </div>
</template>

<script>
// Importamos el repositorio para obtener la lista de clientes
import UsuarioRepository from "@/repositories/UsuarioRepository";

export default {
  data() {
    return {
      clientes: [], // Inicializamos el array de clientes
    };
  },
  mounted() {
    // Al montar el componente, cargamos la lista de clientes
    this.obtenerClientes();
  },
  methods: {
    async obtenerClientes() {
      try {
        // Llamamos al método findAllClientes del repositorio
        const response = await UsuarioRepository.findAllClientes();
        this.clientes = response; // Asignamos la respuesta a la variable clientes
      } catch (e) {
        console.error("Error obteniendo la lista de clientes", e);
        this.clientes = []; // En caso de error, aseguramos que el array esté vacío
      }
    },
  },
};
</script>

<style scoped>
.table-responsive {
  margin-top: 20px;
}

.table th,
.table td {
  text-align: center;
}
</style>
