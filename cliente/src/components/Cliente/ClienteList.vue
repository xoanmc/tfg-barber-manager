<template>
  <div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1 class="text-primary">Lista de Clientes</h1>
    </div>

    <!-- Muestra la tabla solo si hay clientes disponibles -->
    <div class="table-responsive" v-if="clientes.length > 0">
      <table class="table table-striped table-hover align-middle">
        <thead class="table-dark">
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Nombre</th>
            <th scope="col">Apellido</th>
            <th scope="col">Login</th>
            <th scope="col">Email</th>
            <th scope="col">Teléfono</th>
            <th scope="col">Fecha de Nacimiento</th>
            <th scope="col">Citas</th>
            <th scope="col">Registro</th>
            <th scope="col">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="cliente in clientes"
            :key="cliente.id"
            :class="{ 'table-warning': !cliente.activo }"
          >
            <td>{{ cliente.id }}</td>
            <td>{{ cliente.nombre }}</td>
            <td>{{ cliente.apellido }}</td>
            <td>{{ cliente.login }}</td>
            <td>{{ cliente.email }}</td>
            <td>{{ cliente.telefono }}</td>
            <td>{{ cliente.fechaNacimiento ?? "N/A" }}</td>
            <td>{{ cliente.citas ?? "N/A" }}</td>
            <td>{{ cliente.primeraCita ?? "N/A" }}</td>
            <td>
              <button
                @click="toggleClienteEstado(cliente.id, cliente.activo)"
                class="btn btn-sm"
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
    <div v-else class="text-center py-5">
      <p class="text-muted">Cargando lista de clientes...</p>
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
      if (confirm(`¿Estás seguro de que deseas ${accion} a este cliente?`)) {
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

.table-dark th {
  color: #fff;
  background-color: #343a40;
}

.btn-warning {
  background-color: #ffc107 !important;
  border-color: #ffc107 !important;
}

.btn-success {
  background-color: #28a745 !important;
  border-color: #28a745 !important;
}

.btn-warning:hover {
  background-color: #e0a800 !important;
  border-color: #d39e00 !important;
}

.btn-success:hover {
  background-color: #218838 !important;
  border-color: #1e7e34 !important;
}

.text-muted {
  font-size: 1.2rem;
}
</style>
