<template>
  <div class="container py-5">
    <!-- Mostrar un spinner mientras se cargan los datos -->
    <div v-if="loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
    </div>

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
      myuser: null,
      imageUrl: "",
      loading: true,
    };
  },
  async mounted() {
    await this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        this.myuser = await AccountRepository.getAccount();

        this.imageUrl = await ImageRepository.getProfileImage(this.myuser.id);
      } catch (err) {
        console.error("Error cargando el perfil del cliente:", err);
      } finally {
        this.loading = false;
      }
    },
    handleImageUpload(newImageUrl) {
      this.imageUrl = newImageUrl;
    },
    goToEditProfile() {
      this.$router.push("/editProfile");
    },
  },
};
</script>

<style scoped>
.card {
  border-radius: 15px;
  background-color: #f8f9fa;
  margin-top: 80px;
}

.card-body {
  padding: 2rem;
}

.text-primary {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
