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

        <!-- solo para usuario jefe -->
        <div v-if="isJefe" class="service-actions">
          <button class="btn btn-primary btn-sm me-2" @click="editarServicio(index)">
            Editar
          </button>
          <button class="btn btn-danger btn-sm" @click="eliminarServicio(index)">
            Eliminar
          </button>
        </div>
      </div>
    </div>

    <!--añadir/modificar servicios (solo para jefes) -->
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
      servicios: [],
      nuevoServicio: {
        nombre: "",
        descripcion: "",
        precio: null,
      },
      editIndex: null,
      precioInvalido: false,
    };
  },
  computed: {
    isJefe() {
      return auth.isJefe();
    },
  },
  mounted() {
    this.cargarServicios();
  },
  methods: {
    async cargarServicios() {
      try {
        const response = await ServicesRepository.getServicios();
        this.servicios = response;
      } catch (error) {
        console.error("Error cargando los servicios", error);
      }
    },
    validatePrecio() {
      const precio = this.nuevoServicio.precio;
      this.precioInvalido =
        isNaN(precio) || precio < 0 || !/^\d+(\.\d{1,2})?$/.test(precio);
    },
    async handleSubmit() {
      if (this.editIndex !== null) {
        this.servicios[this.editIndex] = { ...this.nuevoServicio };
      } else {
        this.servicios.unshift({ ...this.nuevoServicio });
      }

      await this.guardarServicios();
      this.resetForm();
    },
    editarServicio(index) {
      this.nuevoServicio = { ...this.servicios[index] };
      this.editIndex = index;
    },
    async eliminarServicio(index) {
      const servicioId = this.servicios[index].id;
      try {
        await ServicesRepository.deleteServicio(servicioId);
        this.servicios.splice(index, 1);
        alert("Servicio eliminado correctamente");
      } catch (error) {
        console.error("Error eliminando el servicio", error);
        alert("Hubo un error al eliminar el servicio");
      }
    },
    resetForm() {
      this.nuevoServicio = { nombre: "", descripcion: "", precio: null };
      this.editIndex = null;
      this.precioInvalido = false;
    },
    async guardarServicios() {
      try {
        await ServicesRepository.updateServicios(this.servicios);
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
