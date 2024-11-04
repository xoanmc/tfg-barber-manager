<template>
  <div class="container">
    <div v-if="user">
      <!-- Usar el componente ProfileImage para la imagen de perfil -->
      <ProfileImage
        :userId="myuser.id"
        :currentImageUrl="imageUrl"
        @imageUploaded="handleImageUpload"
      />

      <!-- Mostrar Detalles del Empleado -->
      <h1>{{ myuser.nombre + " " + myuser.apellido }}</h1>
      <h4>{{ "login: " + myuser.login }}</h4>
      <h4>{{ "telefono: " + myuser.telefono }}</h4>
      <h2>{{ "puesto: " + myuser.puesto }}</h2>

      <!-- Nuevo Bloque: Información del Empleado -->
      <div class="employee-info">
        <h4>{{ "Horario: " + myuser.horario }}</h4>
        <h4>{{ "Descripción: " + myuser.descripcion }}</h4>
      </div>
    </div>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ProfileImage from "@/components/ProfileImage";

export default {
  components: {
    ProfileImage,
  },
  data() {
    return {
      myuser: null,
      user: null,
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

        // Obtener los nuevos datos (horario y descripción)
        if (this.user && this.user.horario) {
          this.myuser.horario = this.user.horario;
        }
        if (this.user && this.user.descripcion) {
          this.myuser.descripcion = this.user.descripcion;
        }
      } catch (err) {
        console.error(err);
      }
    },
    handleImageUpload(newImageUrl) {
      this.imageUrl = newImageUrl;
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

.employee-info {
  margin-top: 20px;
  text-align: left;
}

.employee-info h4 {
  margin: 5px 0;
}
</style>
