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
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="cliente in clientes"
            :key="cliente.id"
            :class="{ 'table-secondary': !cliente.activo }"
          >
            <td>{{ cliente.id }}</td>
            <td>{{ cliente.nombre }}</td>
            <td>{{ cliente.apellido }}</td>
            <td>{{ cliente.telefono }}</td>
            <td>{{ cliente.edad ?? "N/A" }}</td>
            <!-- Edad -->
            <td>{{ cliente.citas ?? "N/A" }}</td>
            <!-- Citas -->
            <td>{{ cliente.primeraCita ?? "N/A" }}</td>
            <!-- Primera cita -->
            <td>
              <button
                @click="toggleClienteEstado(cliente.id, cliente.activo)"
                class="btn"
                :class="cliente.activo ? 'btn-warning' : 'btn-success'"
              >
                {{ cliente.activo ? "Bloquear" : "Activar" }}
              </button>
            </td>
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
import UsuarioRepository from "@/repositories/UsuarioRepository";

export default {
  data() {
    return {
      clientes: [], // Inicializamos el array de clientes
    };
  },
  mounted() {
    this.obtenerClientes(); // Carga la lista de clientes al montar el componente
  },
  methods: {
    async obtenerClientes() {
      try {
        const response = await UsuarioRepository.findAllClientes();
        this.clientes = response;
      } catch (e) {
        console.error("Error obteniendo la lista de clientes", e);
        this.clientes = []; // Asegura que el array esté vacío en caso de error
      }
    },
    async toggleClienteEstado(id, estadoActual) {
      const accion = estadoActual ? "bloquear" : "activar";
      if (
        confirm(
          `¿Estás seguro de que deseas ${accion} a este cliente?`
        )
      ) {
        try {
          if (estadoActual) {
            await UsuarioRepository.bloquearCliente(id);
            alert("Cliente bloqueado con éxito.");
          } else {
            await UsuarioRepository.activarCliente(id);
            alert("Cliente activado con éxito.");
          }
          // Actualiza directamente el estado del cliente
          const cliente = this.clientes.find((c) => c.id === id);
          if (cliente) {
            cliente.activo = !estadoActual;
          }
        } catch (e) {
          console.error(`Error al ${accion} al cliente`, e);
          alert(`Hubo un error al ${accion} al cliente.`);
        }
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

.table-secondary {
  background-color: #f8d7da;
  opacity: 0.8;
}

.btn-warning {
  background-color: #ffc107 !important;
  border-color: #ffc107 !important;
}

.btn-success {
  background-color: #28a745 !important;
  border-color: #28a745 !important;
}
</style>
