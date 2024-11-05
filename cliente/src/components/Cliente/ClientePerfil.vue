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

      <!-- Mostrar Citas Reservadas -->
      <div class="citas-container" v-if="citas.length">
        <h2>Mis Citas</h2>
        <ul>
          <li v-for="cita in citas" :key="cita.id">
            <p><strong>Servicio:</strong> {{ cita.servicio.nombre }}</p>
            <p><strong>Fecha y Hora:</strong> {{ new Date(cita.fechaHora).toLocaleString() }}</p>
            <p><strong>Estado:</strong> {{ cita.estado }}</p>
            <p><strong>Barbero:</strong> {{ cita.barbero.nombre }} {{ cita.barbero.apellido }}</p>
          </li>
        </ul>
      </div>
      <div v-else>
        <p>No tienes citas reservadas.</p>
      </div>
    </div>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import CitaRepository from "@/repositories/CitaRepository";
import ProfileImage from "@/components/ProfileImage"; // Importa el componente

export default {
  components: {
    ProfileImage, // Registra el componente
  },
  data() {
    return {
      myuser: "",
      user: "",
      citas: [], // Lista para almacenar las citas del cliente
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

        // Llamar a la API para obtener las citas del cliente autenticado
        this.citas = await CitaRepository.getCitasCliente(this.myuser.id);
      } catch (err) {
        console.error("Error al obtener los datos del perfil o las citas:", err);
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

.citas-container {
  margin-top: 20px;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 15px;
  background-color: #f9f9f9;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 15px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
}

li:last-child {
  border-bottom: none;
}

p {
  margin: 5px 0;
}
</style>
