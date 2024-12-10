<template>
  <div class="about-container">
    <h1 class="text-center text-primary mb-4">Acerca de Nuestra Barbería</h1>

    <!-- si es jefe, formulario para editar -->
    <div v-if="isJefe" class="card mx-auto shadow-lg p-4" style="max-width: 600px;">
      <form @submit.prevent="handleSubmit">
        <div class="form-group mb-3">
          <label for="descripcion" class="form-label">Descripción</label>
          <textarea
            id="descripcion"
            v-model="descripcion"
            class="form-control"
            rows="5"
            placeholder="Ingrese la descripción de la barbería"
            required
          ></textarea>
        </div>
        <button type="submit" class="btn btn-primary w-100">Guardar Cambios</button>
      </form>
    </div>

    <!-- si no es jefe solo la descripción -->
    <div v-else class="text-center mt-4">
      <p class="text-muted fs-5">{{ descripcion }}</p>
    </div>
  </div>
</template>


<script>
import AboutRepository from "@/repositories/AboutRepository";
import auth from "@/common/auth";

export default {
  data() {
    return {
      descripcion: "",
    };
  },
  computed: {
    isJefe() {
      return auth.isJefe();
    },
  },
  mounted() {
    this.loadContent();
  },
  methods: {
    async loadContent() {
      try {
        const response = await AboutRepository.getContent();
        this.descripcion = response.descripcion;
      } catch (error) {
        console.error("Error cargando el contenido", error);
      }
    },
    async handleSubmit() {
      try {
        await AboutRepository.updateContent({ descripcion: this.descripcion });
        alert("Contenido actualizado con éxito.");
        this.loadContent();
      } catch (error) {
        console.error("Error actualizando el contenido", error);
        alert("Hubo un error al actualizar el contenido.");
      }
    },
  },
};
</script>


<style scoped>
.about-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  margin-top: 80px;
}

h1 {
  font-size: 2.5rem;
  font-weight: bold;
}

.card {
  border-radius: 12px;
}

.form-label {
  font-weight: bold;
  color: #333;
}

.form-control {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.btn-primary {
  background-color: #007BFF;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  font-weight: bold;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.text-muted {
  line-height: 1.6;
}
</style>
