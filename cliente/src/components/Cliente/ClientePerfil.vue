<template>
  <div class="container py-5">
    <!-- Mostrar un spinner mientras se cargan los datos -->
    <div v-if="loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
    </div>

    <!-- Mostrar los datos del cliente -->
    <div v-else-if="myuser">
      <div class="row justify-content-center">
        <div class="col-lg-6">
          <div class="card shadow-lg">
            <div class="card-body text-center">
              <!-- Componente para manejar la imagen de perfil -->
              <ProfileImage
                :userId="myuser.id"
                :currentImageUrl="imageUrl"
                @imageUploaded="handleImageUpload"
              />
              <h3 class="text-primary mt-3">{{ myuser.nombre + " " + myuser.apellido }}</h3>
              <p class="text-muted">{{ "@" + myuser.login }}</p>
              <p><strong>Teléfono:</strong> {{ myuser.telefono }}</p>
              <button class="btn btn-primary mt-4" @click="goToEditProfile">Editar Perfil</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Mostrar mensaje de error si no se pudieron cargar los datos -->
    <div v-else class="text-center text-danger">
      <p>Error al cargar los datos del perfil.</p>
    </div>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import ImageRepository from "@/repositories/ImageRepository";
import ProfileImage from "@/components/ProfileImage";

export default {
  components: {
    ProfileImage,
  },
  data() {
    return {
      myuser: null, // Datos del usuario autenticado
      imageUrl: "", // URL de la imagen de perfil
      loading: true, // Controla si los datos están cargándose
    };
  },
  async mounted() {
    await this.fetchData(); // Cargar los datos al montar el componente
  },
  methods: {
    async fetchData() {
      try {
        // Obtener los datos del usuario autenticado
        this.myuser = await AccountRepository.getAccount();

        // Intentar cargar la URL de la imagen de perfil
        this.imageUrl = await ImageRepository.getProfileImage(this.myuser.id);
      } catch (err) {
        console.error("Error cargando el perfil del cliente:", err);
      } finally {
        this.loading = false; // Desactivar el estado de carga
      }
    },
    handleImageUpload(newImageUrl) {
      this.imageUrl = newImageUrl; // Actualizar la imagen de perfil
    },
    goToEditProfile() {
      this.$router.push("/editProfile"); // Redirigir a la página de edición de perfil
    },
  },
};
</script>

<style scoped>
.card {
  border-radius: 15px;
  background-color: #f8f9fa;
}

.card-body {
  padding: 2rem;
}

.text-primary {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
