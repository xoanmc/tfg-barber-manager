<template>
  <div class="about-container">
    <div class="banner">
      <h1 class="text-center text-primary mb-4">Acerca de Nuestra Barbería</h1>
    </div>

    <!-- Sección de Nuestra Historia -->
    <div class="history-section container mt-5">
      <div class="row align-items-center">
        <div class="col-md-6 history-text">
          <h2 class="text-primary mb-4">Nuestra Historia</h2>
          <p v-if="!isJefe" class="fs-5">{{ aboutInfo.descripcion || "Aquí irá la historia de la barbería." }}</p>
          <div v-if="isJefe">
            <textarea v-model="aboutInfo.descripcion" class="form-control mb-3" rows="5" placeholder="Editar la descripción de la barbería" required></textarea>
            <input type="file" @change="onFileChange" class="form-control mb-3" />
            <button class="btn btn-primary w-100" @click="guardarInfo">Guardar Cambios</button>
          </div>
        </div>
        <div class="col-md-6 history-image">
          <img :src="aboutInfo.imagen || defaultAboutImage" alt="Imagen de la barbería" class="shadow rounded img-fluid" />
        </div>
      </div>
    </div>

    <!-- Sección de Nuestros Profesionales -->
    <div class="professionals-section mt-5">
      <h2 class="text-center text-primary mb-4">Nuestros Profesionales</h2>
      <div class="cards-container d-flex flex-wrap justify-content-center gap-4">
        <div v-for="profesional in profesionales" :key="profesional.id" class="profile-card text-center shadow-sm">
          <img :src="profesional.imagen || defaultProfileFallback" alt="Imagen de perfil" class="profile-img" />
          <p class="mt-3 fw-bold">{{ profesional.nombre }} {{ profesional.apellido }}</p>
          <small class="text-muted">{{ profesional.login }}</small>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AboutRepository from "@/repositories/AboutRepository";
import ProfesionalRepository from "@/repositories/ProfesionalRepository";
import auth from "@/common/auth";

export default {
  data() {
    return {
      aboutInfo: {
        descripcion: "",
        imagen: "", // Imagen de la barbería
      },
      profesionales: [], // Lista de profesionales cargada del backend
      selectedFile: null, // Imagen seleccionada para subir
      defaultAboutImage: require('@/assets/defaultAboutImage.jpg'), // Imagen predeterminada de la barbería
      isJefe: auth.isJefe(),
    };
  },
  mounted() {
    this.loadContent(); // Cargar contenido de la barbería
    this.loadProfessionals(); // Cargar profesionales
  },
  methods: {
    async loadContent() {
      try {
        const response = await AboutRepository.getContent();
        this.aboutInfo = response;
      } catch (error) {
        console.error("Error al cargar el contenido de la barbería", error);
      }
    },
    async loadProfessionals() {
      try {
        const response = await ProfesionalRepository.getAll();
        this.profesionales = response; // Guardar lista de profesionales
      } catch (error) {
        console.error("Error al cargar los profesionales", error);
      }
    },
    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.selectedFile = file;
        const reader = new FileReader();
        reader.onload = (e) => {
          this.aboutInfo.imagen = e.target.result; // Mostrar vista previa de la imagen
        };
        reader.readAsDataURL(file);
      }
    },
    async guardarInfo() {
      try {
        const formData = new FormData();
        formData.append("descripcion", this.aboutInfo.descripcion);
        if (this.selectedFile) {
          formData.append("imagen", this.selectedFile);
        }
        await AboutRepository.updateContent(formData);
        alert("Información actualizada con éxito.");
        this.loadContent(); // Recargar datos actualizados
      } catch (error) {
        console.error("Error al actualizar la información", error);
        alert("Hubo un error al guardar los cambios.");
      }
    },
    verPerfil(profesional) {
      this.$router.push(`/barber-profile/${profesional.login}`);
    },
  },
};
</script>

<style scoped>
.about-container {
  padding: 60px 20px;
}

.banner {
  
  background-size: cover;
  background-position: center;
  padding: 50px 0;
}

.banner h1 {
  font-size: 3rem;
  text-shadow: 1px 1px 5px rgba(0, 0, 0, 0.8);
}

.history-section {
  margin-bottom: 60px;
}

.history-text {
  color: #333;
}

.history-image img {
  max-width: 100%;
  border-radius: 10px;
  object-fit: cover;
}

.professionals-section {
  background-color: transparent; /* Cambiar el fondo a transparente */
  padding: 60px 20px;
  margin-top: 40px;
}

.cards-container {
  background-color: rgba(255, 255, 255, 0.9); /* Fondo blanco con transparencia */
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.profile-card {
  background: white;
  border-radius: 15px;
  padding: 20px;
  width: 220px;
  text-align: center;
  cursor: pointer;
  transition: 0.3s ease;
}

.profile-card:hover {
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
  transform: translateY(-5px);
}

.profile-card img {
  border-radius: 50%;
  width: 120px;
  height: 120px;
  object-fit: cover;
}

textarea.form-control {
  border-radius: 10px;
}

button.btn-primary {
  background-color: #007BFF;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
}

button.btn-primary:hover {
  background-color: #0056b3;
}

.text-muted {
  font-size: 14px;
}

h1 {
  font-size: 2.5rem;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95); /* Sombreado para mejorar la legibilidad */
  color: #f8f9fa; /* Color claro para contraste */
}

h2 {
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95); /* Sombreado para mejorar la legibilidad */
  color: #f8f9fa; /* Color claro para contraste */
}
</style>
