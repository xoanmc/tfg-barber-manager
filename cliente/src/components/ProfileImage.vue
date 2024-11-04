<template>
  <div>
    <!-- Mostrar Imagen de Perfil -->
    <div class="profile-picture">
      <img :src="imageUrl" alt="Profile Image" v-if="imageUrl" />
    </div>

    <!-- Formulario para subir la imagen -->
    <form @submit.prevent="uploadImage">
      <input type="file" @change="onFileSelected" />
      <button type="submit">Subir Imagen</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: {
    userId: {
      type: Number,
      required: true,
    },
    currentImageUrl: {
      type: String,
      default: "", // Imagen por defecto si no hay imagen
    },
  },
  data() {
    return {
      selectedImage: null, // Imagen seleccionada
      imageUrl: this.currentImageUrl || "", // URL de la imagen
    };
  },
  methods: {
    // Manejador para seleccionar la imagen
    onFileSelected(event) {
      this.selectedImage = event.target.files[0];
    },

    // Subir la imagen al servidor
    async uploadImage() {
      if (!this.selectedImage) {
        alert("Por favor selecciona una imagen.");
        return;
      }

      const formData = new FormData();
      formData.append("file", this.selectedImage);

      try {
        const token = localStorage.getItem("token"); // Obtener el token JWT
        const response = await axios.post(
          `/api/users/upload/${this.userId}`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: `Bearer ${token}`, // Incluye el token JWT
            },
          }
        );

        this.imageUrl = response.data.imageUrl;
        this.$emit("imageUploaded", this.imageUrl); // Emitir evento de éxito para que el componente padre sepa que se subió la imagen
        alert("Imagen subida con éxito.");
      } catch (error) {
        console.error("Error al subir la imagen:", error);
        alert("Error al subir la imagen.");
      }
    },
  },
  watch: {
    currentImageUrl(newVal) {
      this.imageUrl = newVal;
    },
  },
};
</script>

<style scoped>
.profile-picture img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
}

form {
  margin-top: 20px;
}
</style>
