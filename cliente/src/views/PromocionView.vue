<template>
  <div class="promociones-container">
    <h1 class="mb-4 text-primary">Promociones Activas</h1>

    <!-- Mostrar mensaje si no hay promociones -->
    <div v-if="promocionesFiltradas.length === 0" class="text-center text-muted">
      <p>No hay promociones activas en este momento.</p>
    </div>

    <!-- Lista de promociones -->
    <div v-else class="promociones-grid">
      <div class="promocion-card" v-for="(promocion, index) in promocionesFiltradas" :key="promocion.id"
        :class="{ inactiva: !promocion.activo }">
        <h2>{{ promocion.servicioNombre }}</h2>
        <p>
          <strong>Descuento:</strong> {{ promocion.porcentajeDescuento }}%
        </p>
        <p><strong>Inicio:</strong> {{ formatFecha(promocion.fechaInicio) }}</p>
        <p><strong>Fin:</strong> {{ formatFecha(promocion.fechaFin) }}</p>
        <p v-if="isJefe">
          <strong>Estado:</strong>
          <span :class="{ 'text-success': promocion.activo, 'text-danger': !promocion.activo }">
            {{ promocion.activo ? "Activa" : "Inactiva" }}
          </span>
        </p>

        <!-- Solo para el usuario jefe -->
        <div v-if="isJefe" class="promocion-actions">
          <button class="btn btn-primary btn-sm me-2" @click="editarPromocion(index)">
            Editar
          </button>
          <button class="btn btn-warning btn-sm me-2" @click="togglePromocion(promocion)">
            {{ promocion.activo ? "Desactivar" : "Activar" }}
          </button>
          <button class="btn btn-danger btn-sm" @click="eliminarPromocion(index)">
            Borrar
          </button>
        </div>
      </div>
    </div>

    <!-- Formulario para añadir/modificar promociones -->
    <div v-if="isJefe" class="form-container mt-5">
      <h3 class="text-primary text-center">
        {{ editIndex !== null ? "Modificar Promoción" : "Añadir Nueva Promoción" }}
      </h3>
      <form @submit.prevent="handleSubmit" class="p-4 shadow-sm bg-light rounded">
        <div class="form-group mb-3">
          <label for="servicio" class="form-label">Servicio</label>
          <select v-model="nuevaPromocion.servicioId" id="servicio" class="form-control" required>
            <option v-for="servicio in servicios" :key="servicio.id" :value="servicio.id">
              {{ servicio.nombre }}
            </option>
          </select>
        </div>
        <div class="form-group mb-3">
          <label for="descuento" class="form-label">Porcentaje de Descuento (%)</label>
          <input v-model="nuevaPromocion.porcentajeDescuento" type="number" id="descuento" class="form-control" min="0"
            max="100" placeholder="Porcentaje de descuento" required />
        </div>
        <div class="form-group mb-3">
          <label for="fechaInicio" class="form-label">Fecha de Inicio</label>
          <input v-model="nuevaPromocion.fechaInicio" type="date" id="fechaInicio" class="form-control" required />
        </div>
        <div class="form-group mb-3">
          <label for="fechaFin" class="form-label">Fecha de Fin</label>
          <input v-model="nuevaPromocion.fechaFin" type="date" id="fechaFin" class="form-control" required />
        </div>
        <div v-if="validacionError" class="text-danger mb-3">
          {{ validacionError }}
        </div>
        <div class="d-flex justify-content-center gap-3">
          <button type="submit" class="btn btn-primary">
            {{ editIndex !== null ? "Guardar Cambios" : "Añadir Promoción" }}
          </button>
          <button type="button" class="btn btn-secondary" v-if="editIndex !== null" @click="resetForm">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import PromocionRepository from "@/repositories/PromocionRepository";
import ServicesRepository from "@/repositories/ServicesRepository";
import auth from "@/common/auth";

export default {
  data() {
    return {
      promociones: [],
      servicios: [],
      nuevaPromocion: {
        servicioId: null,
        porcentajeDescuento: null,
        fechaInicio: "",
        fechaFin: "",
      },
      editIndex: null,
      validacionError: "",
    };
  },
  computed: {
    isJefe() {
      return auth.isJefe();
    },
    promocionesFiltradas() {
      if (this.isJefe) {
        return this.promociones;
      } else {
        return this.promociones.filter((promocion) => promocion.activo);
      }
    },
  },
  async mounted() {
    await this.cargarPromociones();
    await this.cargarServicios();
  },
  methods: {
    async cargarPromociones() {
      try {
        this.promociones = await PromocionRepository.getPromociones();
      } catch (error) {
        console.error("Error cargando promociones:", error);
      }
    },
    async cargarServicios() {
      try {
        this.servicios = await ServicesRepository.getServicios();
      } catch (error) {
        console.error("Error cargando servicios:", error);
      }
    },
    formatFecha(fecha) {
      return new Date(fecha).toLocaleDateString("es-ES");
    },
    validarPromocion() {
      if (!this.nuevaPromocion.fechaInicio || !this.nuevaPromocion.fechaFin) {
        this.validacionError = "Las fechas de inicio y fin son obligatorias.";
        return false;
      }
      if (new Date(this.nuevaPromocion.fechaInicio) > new Date(this.nuevaPromocion.fechaFin)) {
        this.validacionError = "La fecha de inicio no puede ser posterior a la fecha de fin.";
        return false;
      }
      if (
        this.nuevaPromocion.porcentajeDescuento < 0 ||
        this.nuevaPromocion.porcentajeDescuento > 100
      ) {
        this.validacionError = "El porcentaje de descuento debe estar entre 0 y 100.";
        return false;
      }
      this.validacionError = "";
      return true;
    },

    async handleSubmit() {
      try {
        if (!this.validarPromocion()) return;

        if (!this.nuevaPromocion.servicioId) {
          this.validacionError = "Debe seleccionar un servicio.";
          return;
        }

        let nuevaPromocion;
        if (this.editIndex !== null) {
          nuevaPromocion = await PromocionRepository.updatePromocion(this.nuevaPromocion);
        } else {
          nuevaPromocion = await PromocionRepository.addPromocion(this.nuevaPromocion);
        }

        const servicio = this.servicios.find(
          (servicio) => servicio.id === nuevaPromocion.servicioId
        );
        nuevaPromocion.servicioNombre = servicio ? servicio.nombre : "Sin servicio asociado";

        if (this.editIndex !== null) {
          this.promociones[this.editIndex] = nuevaPromocion;
        } else {
          this.promociones.push(nuevaPromocion);
        }

        this.resetForm();
      } catch (error) {
        console.error("Error guardando la promoción:", error);
      }
    },
    async togglePromocion(promocion) {
      try {
        promocion.activo = !promocion.activo;
        await PromocionRepository.updatePromocion(promocion);
      } catch (error) {
        console.error("Error actualizando el estado de la promoción:", error);
      }
    },
    editarPromocion(index) {
      const promocion = this.promociones[index];
      if (promocion.servicio) {
        this.nuevaPromocion = { ...promocion, servicioId: promocion.servicio.id };
      } else {
        this.nuevaPromocion = { ...promocion, servicioId: null };
      }
      this.editIndex = index;
    },
    async eliminarPromocion(index) {
      try {
        const promocionId = this.promociones[index].id;
        await PromocionRepository.deletePromocion(promocionId);
        this.promociones.splice(index, 1);
      } catch (error) {
        console.error("Error eliminando la promoción:", error);
      }
    },
    resetForm() {
      this.nuevaPromocion = {
        servicioId: null,
        porcentajeDescuento: null,
        fechaInicio: "",
        fechaFin: "",
      };
      this.editIndex = null;
    },
  },
};
</script>

<style scoped>
h1 {
  margin-top: 50px;
  font-size: 2.5rem;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95);
  color: #0d6efd;
}

.promociones-container {
  text-align: center;
  padding: 50px 20px;
}

.promociones-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.promocion-card {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 250px;
  text-align: left;
  transition: transform 0.3s, box-shadow 0.3s;
}

.promocion-card.inactiva {
  background-color: #f5f5f5;
  opacity: 0.6;
}

.promocion-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}

.promocion-card h2 {
  font-size: 1.4rem;
  margin-bottom: 10px;
  color: #007bff;
}

.promocion-card p {
  font-size: 0.95rem;
  margin-bottom: 8px;
  color: #555;
}

.promocion-actions {
  display: flex;
  justify-content: space-between;
  margin-top: auto;
}

.text-success {
  color: green;
}

.text-danger {
  color: red;
}

.text-muted {
  text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.7);
  font-size: 1.5rem;
  font-weight: bold;
  color: #f8f9fa;
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
</style>