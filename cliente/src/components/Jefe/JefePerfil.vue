<template>
  <div class="container">
    <div v-if="user">
      <!-- Usar el componente ProfileImage para la imagen de perfil -->
      <ProfileImage
        :userId="myuser.id"
        :currentImageUrl="imageUrl"
        @imageUploaded="handleImageUpload"
      />

      <!-- Mostrar Detalles del Jefe -->
      <h1>{{ myuser.nombre + " " + myuser.apellido }}</h1>
      <h4>{{ "login: " + myuser.login }}</h4>
      <h4>{{ "telefono: " + myuser.telefono }}</h4>

      <!-- Botón para acceder a la Gestión de Empleados -->
      <div class="button-container">
        <button @click="irAGestionEmpleados">Gestión de Empleados</button>
      </div>

      <!-- Botón para acceder a la Gestión de Clientes -->
      <div class="button-container">
        <button @click="irAGestionClientes">Gestión de Clientes</button>
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
      } catch (err) {
        console.error(err);
      }
    },
    handleImageUpload(newImageUrl) {
      this.imageUrl = newImageUrl;
    },
    // Método para redirigir a la gestión de empleados
    irAGestionEmpleados() {
      this.$router.push("/users/empleados");
    },
    // Método para redirigir a la gestión de clientes
    irAGestionClientes() {
      this.$router.push("/users/clientes");
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

.button-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

button {
  background-color: #4d4c4c;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #3dad6c;
}
</style>
