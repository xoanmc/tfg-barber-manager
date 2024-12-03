<template>
  <div class="container py-5">
    <div v-if="loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
    </div>

    <div v-else-if="myuser">
      <div class="row justify-content-center">
        <div class="col-lg-8">
          <div class="card shadow-lg">
            <div class="card-body text-center">
              <ProfileImage :userId="myuser.id" :currentImageUrl="imageUrl" @imageUploaded="handleImageUpload" />
              <h3 class="text-primary mt-3">{{ myuser.nombre + " " + myuser.apellido }}</h3>
              <p class="text-muted">{{ "@" + myuser.login }}</p>
              <p><strong>Teléfono:</strong> {{ myuser.telefono }}</p>
              <p><strong>Puesto:</strong> {{ myuser.puesto }}</p>
              <p><strong>Horario:</strong> {{ myuser.horario }}</p>
              <p><strong>Descripción:</strong> {{ myuser.descripcion }}</p>
              <button class="btn btn-primary mt-4" @click="goToEditProfile">Editar Perfil</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center text-danger">
      <p>Error al cargar los datos del perfil del empleado.</p>
    </div>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ProfileImage from "@/components/ProfileImage";
import ImageRepository from "@/repositories/ImageRepository";

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
        // Intentar cargar la URL de la imagen de perfil
        this.imageUrl = await ImageRepository.getProfileImage(this.myuser.id);
        const user = await UsuarioRepository.findOne(this.myuser.id);
        if (user?.profileImageUrl) this.imageUrl = user.profileImageUrl;
      } catch (err) {
        console.error("Error cargando el perfil del empleado:", err);
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
}

.card-body {
  padding: 2rem;
}

.text-primary {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
