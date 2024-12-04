<template>
  <div class="text-center position-relative">
    <div class="profile-picture position-relative d-inline-block">
      <img
        :src="imageUrl || defaultImageUrl"
        alt="Imagen de Perfil"
        class="rounded-circle shadow img-fluid"
      />
      <button
        type="button"
        class="btn btn-sm btn-primary position-absolute btn-edit-profile"
        @click="triggerFileInput"
        title="Editar imagen"
      >
        <i class="bi bi-pencil"></i>
      </button>
    </div>

    <input
      type="file"
      ref="fileInput"
      class="d-none"
      @change="onFileSelected"
    />
  </div>
</template>

<script>
import ImageRepository from "@/repositories/ImageRepository";

export default {
  props: {
    userId: {
      type: Number,
      required: true,
    },
    currentImageUrl: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      selectedImage: null,
      imageUrl: this.currentImageUrl,
      defaultImageUrl: "https://via.placeholder.com/150",
    };
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    onFileSelected(event) {
      this.selectedImage = event.target.files[0];
      this.uploadImage();
    },
    async uploadImage() {
      if (!this.selectedImage) {
        alert("Por favor selecciona una imagen.");
        return;
      }

      try {
        const imageUrl = await ImageRepository.uploadProfileImage(this.userId, this.selectedImage);
        this.imageUrl = imageUrl;
        this.$emit("imageUploaded", imageUrl);
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
.profile-picture {
  width: 150px;
  height: 150px;
  margin: 0 auto;
  position: relative;
}

.profile-picture img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.btn-edit-profile {
  width: 40px;
  height: 40px;
  bottom: 5px;
  right: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  font-size: 1.2rem;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.btn-edit-profile:hover {
  background-color: #0056b3;
  color: #fff;
  opacity: 0.9;
}
</style>
