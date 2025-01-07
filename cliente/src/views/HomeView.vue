<template>
  <div class="home-container">
    <!-- Imagen de fondo -->
    <div class="background-banner" :style="{ backgroundImage: `url(${homePageInfo.imagen || defaultBanner})` }">
      <div class="overlay">
        <div class="content text-center">
          <img alt="Logo de la Barbería" src="../assets/logoBarber.png" class="logo-img mb-3" />
          <h1 class="text-primary display-4">
            {{ homePageInfo.nombre || "Nombre Barbería" }}
          </h1>
          <p class="text-white fs-5 mb-4">
            {{ homePageInfo.descripcion || "Breve descripción sobre la barbería en concreto." }}
          </p>
          <button class="btn btn-primary btn-lg mt-2" @click="reservar">
            Reserva Ya!
          </button>
        </div>

        <div v-if="isJefe" class="form-container bg-white p-4 shadow rounded mx-auto">
          <h2 class="text-center text-primary mb-4">¿Quieres editar los datos?</h2>
          <form @submit.prevent="guardarInformacion">
            <div class="mb-3">
              <label for="nombre" class="form-label">Nombre de la Barbería</label>
              <input v-model="homePageInfo.nombre" type="text" id="nombre" class="form-control"
                placeholder="Nombre de la barbería" required />
            </div>
            <div class="mb-3">
              <label for="descripcion" class="form-label">Descripción de la Barbería</label>
              <textarea v-model="homePageInfo.descripcion" id="descripcion" class="form-control"
                placeholder="Descripción de la barbería" rows="3" required></textarea>
            </div>
            <div class="mb-3">
              <label for="imagen" class="form-label">Subir Imagen de Portada</label>
              <input type="file" id="imagen" class="form-control" accept="image/*" @change="onFileChange" />
            </div>
            <button type="submit" class="btn btn-primary w-100">Guardar</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import auth from "@/common/auth";
import HomePageInfoRepository from "@/repositories/HomePageInfoRepository";

export default {
  name: "HomeView",
  data() {
    return {
      homePageInfo: {
        nombre: "",
        descripcion: "",
        imagen: "", // URL temporal de la imagen seleccionada
      },
      selectedFile: null, // Archivo seleccionado
      defaultBanner: require('@/assets/defaultBanner.jpg'),
      isJefe: auth.isJefe(),
    };
  },
  mounted() {
    this.cargarInformacion();
  },
  methods: {
    async cargarInformacion() {
      try {
        const response = await HomePageInfoRepository.getHomePageInfo();
        console.log("Respuesta del servidor:", response);
        this.homePageInfo = response;
      } catch (error) {
        console.error("Error cargando la información de la página de inicio", error);
      }
    },
    async guardarInformacion() {
      try {
        const formData = new FormData();
        formData.append("nombre", this.homePageInfo.nombre);
        formData.append("descripcion", this.homePageInfo.descripcion);

        if (this.selectedFile) {
          formData.append("imagen", this.selectedFile); // Añadir archivo si fue seleccionado
        } else {
          console.warn("No se seleccionó ninguna imagen para subir.");
        }

        await HomePageInfoRepository.updateHomePageInfo(formData);
        alert("Información actualizada con éxito");
        this.cargarInformacion();
      } catch (error) {
        console.error("Error guardando la información de la página de inicio", error);
        alert("Hubo un error al guardar la información");
      }
    },
    reservar() {
      if (auth.isLogged()) {
        this.$router.push("/reserve");
      } else {
        this.$router.push("/login");
      }
    },
    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.selectedFile = file; // signa archivo seleccionado
        const reader = new FileReader();
        reader.onload = (e) => {
          this.homePageInfo.imagen = e.target.result; // URL temporal para previsualización
        };
        reader.readAsDataURL(file);
      }
    },

  },
};
</script>

<style scoped>
/* General estilos */
.home-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.background-banner {
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  width: 100%;
  height: 100vh;
  position: relative;
}

.overlay {
  background-color: rgba(0, 0, 0, 0.6); /* Oscurece la imagen de fondo */
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding-top: 5%;
}

.content {
  color: white;
  text-align: center;
  max-width: 800px;
  margin-bottom: 50px;
  /* Espacio adicional entre el contenido y el formulario */
}

.logo-img {
  max-width: 120px;
  border-radius: 50%;
  margin-bottom: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

h1 {
  font-weight: bold;
  font-size: 2.5rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95);

}

p {
  font-size: 1.2rem;
}

.btn-primary {
  background-color: #007BFF;
  border: none;
}

.btn-primary:hover {
  background-color: #0056b3;
}

/* Formulario */
.form-container {
  max-width: 500px;
  background: white;
  position: absolute;
  bottom: 10%;
  padding: 20px;
  border-radius: 8px;
}

.form-container h2 {
  font-size: 1.5rem;
  margin-bottom: 20px;
}

.form-container .form-label {
  font-weight: bold;
  color: #333;
}

.form-container .btn-primary {
  background-color: #007BFF;
  border: none;
}

.form-container .btn-primary:hover {
  background-color: #0056b3;
}
</style>
