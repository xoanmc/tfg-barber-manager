<template>
  <div class="about-container">
    <h1>Acerca de nuestra Barbería</h1>

    <!-- Si el usuario es jefe, muestra el formulario para editar -->
    <div v-if="isJefe">
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="descripcion">Descripción</label>
          <textarea
            id="descripcion"
            v-model="descripcion"
            class="form-control"
            rows="5"
            placeholder="Ingrese la descripción de la barbería"
          ></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Guardar Cambios</button>
      </form>
    </div>

    <!-- Si no es jefe, muestra solo la descripción -->
    <div v-else>
      <p>{{ descripcion }}</p>
    </div>
  </div>
</template>

<script>
import AboutRepository from "@/repositories/AboutRepository"; // Repositorio para obtener el contenido
import auth from "@/common/auth"; // Importa autenticación

export default {
  data() {
    return {
      descripcion: "", // Descripción actual
    };
  },
  computed: {
    isJefe() {
      return auth.isJefe(); // Verifica si el usuario es jefe
    },
  },
  mounted() {
    this.loadContent(); // Carga el contenido al montar el componente
  },
  methods: {
    async loadContent() {
      try {
        const response = await AboutRepository.getContent(); // Obtiene el contenido
        this.descripcion = response.descripcion;
      } catch (error) {
        console.error("Error cargando el contenido", error);
      }
    },
    async handleSubmit() {
      try {
        await AboutRepository.updateContent({ descripcion: this.descripcion }); // Actualiza el contenido
        alert("Contenido actualizado con éxito.");
        this.loadContent(); // Vuelve a cargar el contenido actualizado
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
}

.form-group {
  margin-bottom: 15px;
}

.btn-primary {
  margin-top: 10px;
}
</style>
