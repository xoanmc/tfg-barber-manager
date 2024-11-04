<template>
  <div class="services-container">
    <h1>Nuestros servicios</h1>

    <!-- Formulario para añadir o modificar servicios (solo visible para jefes) -->
    <div v-if="isJefe" class="form-container">
      <form @submit.prevent="handleSubmit">
        <h3>
          {{ editIndex !== null ? "Modificar Servicio" : "Añadir Servicio" }}
        </h3>
        <div class="form-group">
          <label for="nombre">Nombre del servicio</label>
          <input
            v-model="nuevoServicio.nombre"
            type="text"
            id="nombre"
            placeholder="Nombre del servicio"
            required
          />
        </div>
        <div class="form-group">
          <label for="descripcion">Descripción del servicio</label>
          <textarea
            v-model="nuevoServicio.descripcion"
            id="descripcion"
            placeholder="Descripción del servicio"
            required
          ></textarea>
        </div>
        <div class="form-group">
          <label for="precio">Precio del servicio</label>
          <input
            v-model="nuevoServicio.precio"
            type="number"
            id="precio"
            step="0.01"
            min="0"
            placeholder="Precio del servicio"
            required
            @input="validatePrecio"
          />
          <span v-if="precioInvalido" class="error-text"
            >El precio no es válido</span
          >
        </div>
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
          Cancelar Edición
        </button>
      </form>
    </div>

    <!-- Lista de servicios -->
    <div class="services-grid">
      <div
        class="service-card"
        v-for="(servicio, index) in servicios"
        :key="servicio.id"
      >
        <h2>{{ servicio.nombre }}</h2>
        <p>{{ servicio.descripcion }}</p>
        <p>Precio: {{ servicio.precio.toFixed(2) }}€</p>
        <!-- Mostrar precio -->

        <!-- Botones de edición y eliminación visibles solo para jefes -->
        <div v-if="isJefe" class="service-actions">
          <button @click="editarServicio(index)">Editar</button>
          <button @click="eliminarServicio(index)">Eliminar</button>
        </div>
      </div>
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
        this.servicios.push({ ...this.nuevoServicio });
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
  padding: 20px;
}

.form-container {
  margin-bottom: 20px;
}

.services-grid {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin-top: 20px;
}

.service-card {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 200px;
  text-align: center;
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.service-card:hover {
  transform: scale(1.05);
}

.service-card h2 {
  font-size: 18px;
  margin-bottom: 10px;
}

.service-card p {
  font-size: 14px;
  color: #555;
}

.service-actions {
  margin-top: 10px;
}

.form-group {
  margin-bottom: 15px;
}

.btn {
  margin-top: 10px;
}

.error-text {
  color: red;
  font-size: 12px;
}
</style>
