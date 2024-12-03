<template>
  <div class="services-container">
    <h1 class="mb-4 text-primary">Nuestros Servicios</h1>

    <!-- Lista de servicios -->
    <div class="services-grid">
      <div
        class="service-card"
        v-for="(servicio, index) in servicios"
        :key="servicio.id"
      >
        <h2>{{ servicio.nombre }}</h2>
        <p>{{ servicio.descripcion }}</p>
        <p><strong>Precio:</strong> {{ servicio.precio.toFixed(2) }} €</p>

        <!-- Botones de edición y eliminación visibles solo para jefes -->
        <div v-if="isJefe" class="service-actions">
          <button class="btn btn-warning btn-sm me-2" @click="editarServicio(index)">
            Editar
          </button>
          <button class="btn btn-danger btn-sm" @click="eliminarServicio(index)">
            Eliminar
          </button>
        </div>
      </div>
    </div>

    <!-- Formulario para añadir o modificar servicios (solo visible para jefes) -->
    <div v-if="isJefe" class="form-container mt-5">
      <h3 class="text-primary text-center">
        {{ editIndex !== null ? "Modificar Servicio" : "Añadir Nuevo Servicio" }}
      </h3>
      <form @submit.prevent="handleSubmit" class="p-4 shadow-sm bg-light rounded">
        <div class="form-group mb-3">
          <label for="nombre" class="form-label">Nombre del Servicio</label>
          <input
            v-model="nuevoServicio.nombre"
            type="text"
            id="nombre"
            class="form-control"
            placeholder="Nombre del servicio"
            required
          />
        </div>
        <div class="form-group mb-3">
          <label for="descripcion" class="form-label">Descripción del Servicio</label>
          <textarea
            v-model="nuevoServicio.descripcion"
            id="descripcion"
            class="form-control"
            placeholder="Descripción del servicio"
            rows="3"
            required
          ></textarea>
        </div>
        <div class="form-group mb-3">
          <label for="precio" class="form-label">Precio del Servicio (€)</label>
          <input
            v-model="nuevoServicio.precio"
            type="number"
            id="precio"
            class="form-control"
            step="0.01"
            min="0"
            placeholder="Precio del servicio"
            required
            @input="validatePrecio"
          />
          <span v-if="precioInvalido" class="error-text">
            El precio debe ser válido (máximo 2 decimales).
          </span>
        </div>
        <div class="d-flex justify-content-center gap-3">
          <button
            type="submit"
            class="btn btn-primary"
            :disabled="precioInvalido"
          >
            {{ editIndex !== null ? "Guardar Cambios" : "Añadir Servicio" }}
          </button>
          <button
            type="button"
            class="btn btn-secondary"
            v-if="editIndex !== null"
            @click="resetForm"
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import auth from "@/common/auth";
import ServicesRepository from "@/repositories/ServicesRepository";

export default {
  data() {
    return {
      servicios: [], // Lista de servicios obtenidos desde el backend
      nuevoServicio: {
        nombre: "",
        descripcion: "",
        precio: null,
      },
      editIndex: null, // Índice del servicio en edición (null si no se edita)
      precioInvalido: false, // Validación de precio
    };
  },
  computed: {
    isJefe() {
      return auth.isJefe(); // Solo los jefes pueden añadir/editar servicios
    },
  },
  mounted() {
    this.cargarServicios(); // Cargar los servicios desde el backend cuando se monta el componente
  },
  methods: {
    // Cargar servicios desde el backend
    async cargarServicios() {
      try {
        const response = await ServicesRepository.getServicios();
        this.servicios = response;
      } catch (error) {
        console.error("Error cargando los servicios", error);
      }
    },
    // Validar el campo precio
    validatePrecio() {
      const precio = this.nuevoServicio.precio;
      this.precioInvalido =
        isNaN(precio) || precio < 0 || !/^\d+(\.\d{1,2})?$/.test(precio);
    },
    // Guardar cambios (añadir o editar servicio)
    async handleSubmit() {
      if (this.editIndex !== null) {
        // Si estamos en modo edición, actualiza el servicio
        this.servicios[this.editIndex] = { ...this.nuevoServicio };
      } else {
        // Si estamos añadiendo un nuevo servicio
        this.servicios.unshift({ ...this.nuevoServicio }); // Añadir al inicio
      }

      // Guardar los cambios en el backend
      await this.guardarServicios();
      this.resetForm(); // Reiniciar el formulario
    },
    // Editar un servicio existente
    editarServicio(index) {
      this.nuevoServicio = { ...this.servicios[index] }; // Cargar los datos del servicio en el formulario
      this.editIndex = index;
    },
    // Eliminar un servicio
    async eliminarServicio(index) {
      const servicioId = this.servicios[index].id; // Obtener el ID del servicio a eliminar
      try {
        await ServicesRepository.deleteServicio(servicioId); // Llamar a la API para eliminar el servicio en el backend
        this.servicios.splice(index, 1); // Eliminar el servicio de la lista en el frontend
        alert("Servicio eliminado correctamente");
      } catch (error) {
        console.error("Error eliminando el servicio", error);
        alert("Hubo un error al eliminar el servicio");
      }
    },
    // Resetear formulario
    resetForm() {
      this.nuevoServicio = { nombre: "", descripcion: "", precio: null }; // Limpiar campos del formulario
      this.editIndex = null; // Salir del modo edición
      this.precioInvalido = false; // Reiniciar la validación de precio
    },
    // Guardar la lista completa de servicios en el backend
    async guardarServicios() {
      try {
        await ServicesRepository.updateServicios(this.servicios); // Persiste la lista de servicios en el backend
        alert("Servicios actualizados con éxito");
      } catch (error) {
        console.error("Error guardando los servicios", error);
        alert("Hubo un error al guardar los servicios");
      }
    },
  },
};
</script>

<style scoped>
.services-container {
  text-align: center;
  padding: 30px 20px;
}

.services-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.service-card {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 250px;
  text-align: left;
  transition: transform 0.3s, box-shadow 0.3s;
}

.service-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}

.service-card h2 {
  font-size: 1.4rem;
  margin-bottom: 10px;
  color: #007bff;
}

.service-card p {
  font-size: 0.95rem;
  margin-bottom: 8px;
  color: #555;
}

.service-actions {
  display: flex;
  justify-content: space-between;
}

.form-container {
  max-width: 600px;
  margin: 0 auto;
}

.form-group label {
  font-weight: bold;
}

.form-control {
  border-radius: 8px;
}

.error-text {
  color: red;
  font-size: 0.85rem;
}

.d-flex.justify-content-center {
  justify-content: center !important;
}
</style>
