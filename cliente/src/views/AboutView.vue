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
            <textarea v-model="aboutInfo.descripcion" class="form-control mb-3" rows="5"
              placeholder="Editar la descripción de la barbería" required></textarea>
            <input type="file" @change="onFileChange" class="form-control mb-3" />
            <button class="btn btn-primary w-100" @click="guardarInfo">Guardar Cambios</button>
          </div>
        </div>
        <div class="col-md-6 history-image">
          <img :src="aboutInfo.imagen || defaultAboutImage" alt="Imagen de la barbería"
            class="shadow rounded img-fluid" />
        </div>
      </div>
    </div>

    <!-- Sección de Nuestros Profesionales -->
    <div class="professionals-section mt-5">
      <h2 class="text-center text-primary mb-4">Nuestros Profesionales</h2>
      <div class="cards-container d-flex flex-wrap justify-content-center gap-4">
        <div v-for="profesional in profesionales" :key="profesional.id" class="profile-card text-center shadow-sm">
          <div class="profile-img-container">
            <img v-if="profesional.imagen" :src="profesional.imagen" alt="Imagen de perfil" class="profile-img" />
            <div v-else class="profile-placeholder"></div>
          </div>
          <p class="mt-3 fw-bold">{{ profesional.nombre }} {{ profesional.apellido }}</p>
          <small class="text-muted">@{{ profesional.login }}</small>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AboutRepository from "@/repositories/AboutRepository";
import auth from "@/common/auth";
import eventBus from "@/eventBus";

export default {
  data() {
    return {
      aboutInfo: {
        descripcion: "",
        imagen: "",
      },
      profesionales: [],
      selectedFile: null,
      defaultAboutImage: require('@/assets/defaultAboutImage.jpg'),
      isJefe: auth.isJefe(),
    };
  },
  mounted() {
    this.loadContent();
    this.loadProfessionals();

    // Escuchar el evento de actualización de imagen de perfil
    eventBus.on("profile-image-updated", this.loadProfessionals);
  },
  beforeUnmount() {

    // Eliminar la suscripción al desmontar el componente
    eventBus.off("profile-image-updated", this.loadProfessionals);
  },
  methods: {
    async loadContent() {
      try {
        const response = await AboutRepository.getContent();
        console.log("Datos recibidos:", response);
        this.aboutInfo.descripcion = response.descripcion; // Asignar descripción correctamente
        this.aboutInfo.imagen = response.imagen || this.defaultAboutImage; // Asignar imagen o por defecto
      } catch (error) {
        console.error("Error al cargar el contenido de la barbería", error);
      }
    },
    async loadProfessionals() {
      try {
        const response = await AboutRepository.getProfessionals();
        this.profesionales = response.map((profesional) => ({
          ...profesional,
          imagen: profesional.imagen || profesional.profileImageUrl,
        }));

      } catch (error) {
        console.error("Error al cargar los barberos", error);
      }
    },
    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.selectedFile = file;
        const reader = new FileReader();
        reader.onload = (e) => {
          this.aboutInfo.imagen = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    async guardarInfo() {
      try {
        const formData = new FormData();
        console.log("Descripción enviada:", this.aboutInfo.descripcion);
        formData.append("descripcion", this.aboutInfo.descripcion);
        if (this.selectedFile) {
          formData.append("imagen", this.selectedFile);
        }
        const response = await AboutRepository.updateContent(formData);
        console.log("Respuesta del servidor al guardar:", response);
        alert("Información actualizada con éxito.");
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
  background-color: transparent;
  padding: 60px 20px;
  margin-top: 40px;
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
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95);
  color: #f8f9fa;
}

h2 {
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95);
  color: #f8f9fa;
}

.history-text {
  color: #fff;
  font-size: 1.2rem;
  line-height: 1.6;
}

.profile-img-container {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
}

.profile-img {
  border-radius: 50%;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #e0e0e0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.profile-placeholder::before {
  content: "\f007";
  font-family: "Font Awesome 5 Free";
  font-weight: 900;
  font-size: 50px;
  color: #9e9e9e;
}
</style>
