<template>
  <div class="about-container">
    <!-- Sección de Nuestra Historia -->
    <div class="history-section container mt-5">
      <div class="row align-items-center">
        <div class="col-md-6 history-text">
          <h2 class="text-primary mb-4">Nuestra Historia</h2>
          <p v-if="!isJefe" class="history-text">{{ aboutInfo.descripcion || "Aquí irá la historia de la barbería." }}
          </p>
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
        <div v-for="profesional in profesionales" :key="profesional.id" class="profile-card text-center shadow-sm"
          @click="verPerfil(profesional)">
          <div class="profile-img-container">
            <img v-if="profesional.imagen" :src="profesional.imagen" alt="Imagen de perfil" class="profile-img" />
            <div v-else class="profile-placeholder"></div>
          </div>
          <p class="mt-3 fw-bold">{{ profesional.nombre }} {{ profesional.apellido }}</p>
          <small class="text-muted">@{{ profesional.login }}</small>
        </div>
      </div>
    </div>

    <div v-if="mostrarAlerta" class="modal d-flex align-items-center justify-content-center"
      style="background-color: rgba(0,0,0,0.5); position: fixed; top: 0; left: 0; width: 100%; height: 100%; z-index: 1050;">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ alertaTitulo }}</h5>
            <button type="button" class="btn-close" @click="cerrarAlerta"></button>
          </div>
          <div class="modal-body">
            <p>{{ alertaMensaje }}</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="cerrarAlerta">Aceptar</button>
          </div>
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
      mostrarAlerta: false,
      alertaTitulo: "",
      alertaMensaje: "",
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
        this.mostrarAlertaPersonalizada("¡Éxito!", "Información actualizada.");
      } catch (error) {
        console.error("Error al actualizar la información", error);
        this.mostrarAlertaPersonalizada("Error", "Hubo un error al guardar los cambios.");
      }
    },
    mostrarAlertaPersonalizada(titulo, mensaje) {
      this.alertaTitulo = titulo;
      this.alertaMensaje = mensaje;
      this.mostrarAlerta = true;
    },
    cerrarAlerta() {
      this.mostrarAlerta = false;
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
  font-size: 1rem;
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
  font-size: 1.1rem;
  line-height: 1.5;
  text-align: justify;
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

.modal {
  z-index: 1050 !important;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close {
  border: none;
  background-color: transparent;
}
</style>
