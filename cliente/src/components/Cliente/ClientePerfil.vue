<template>
  <div v-if="user">
    <div class="container">
      <!-- Usar el componente ProfileImage para la imagen de perfil -->
      <ProfileImage
        :userId="myuser.id"
        :currentImageUrl="imageUrl"
        @imageUploaded="handleImageUpload"
      />

      <!-- Mostrar Detalles del Usuario -->
      <h1>{{ myuser.nombre + " " + myuser.apellido }}</h1>
      <h4>{{ "login: " + myuser.login }}</h4>
      <h4>{{ "telefono: " + myuser.telefono }}</h4>
    </div>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ProfileImage from "@/components/ProfileImage"; // Importa el componente

export default {
  components: {
    ProfileImage, // Registra el componente
  },
  data() {
    return {
      myuser: "",
      user: "",
      imageUrl: "", // URL de la imagen de perfil
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        this.myuser = await AccountRepository.getAccount();
        this.user = await UsuarioRepository.findOne(this.myuser.id);

        // Obtener la imagen de perfil desde la base de datos si existe
        if (this.user && this.user.profileImageUrl) {
          this.imageUrl = this.user.profileImageUrl;
        }
      } catch (err) {
        console.error(err);
      }
    },
    handleImageUpload(newImageUrl) {
      this.imageUrl = newImageUrl; // Actualiza la imagen tras la subida
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
