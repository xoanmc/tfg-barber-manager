<template>
  <div v-if="user">
    <div class="container">
      <ProfileImage
        :userId="myuser.id"
        :currentImageUrl="imageUrl"
        @imageUploaded="handleImageUpload"
      />
      <h1>{{ myuser.nombre + " " + myuser.apellido }}</h1>
      <h4>{{ "login: " + myuser.login }}</h4>
      <h4>{{ "telefono: " + myuser.telefono }}</h4>
      <h2>{{ "puesto: " + myuser.puesto }}</h2>
      
      <div class="employee-info">
        <h4>{{ "Horario: " + myuser.horario }}</h4>
        <h4>{{ "Descripción: " + myuser.descripcion }}</h4>
      </div>

      <h2>Citas Programadas</h2>
      <div v-if="citas.length > 0">
        <ul>
          <li v-for="cita in citas" :key="cita.id">
            {{ cita.fechaHora }} - {{ cita.servicio.nombre }} - Estado: {{ cita.estado }}
            <div v-if="cita.estado !== 'Confirmada'">
              <button @click="confirmarCita(cita.id)">Confirmar</button>
              <button @click="rechazarCita(cita.id)">Rechazar</button>
            </div>
            <button @click="modificarCita(cita)">Modificar</button>
          </li>
        </ul>
      </div>
      <div v-else>
        No tienes citas programadas.
      </div>
    </div>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import CitaRepository from "@/repositories/CitaRepository";
import ProfileImage from "@/components/ProfileImage";

export default {
  components: {
    ProfileImage,
  },
  data() {
    return {
      myuser: null,
      user: null,
      citas: [],
      imageUrl: "",
    };
  },
  async mounted() {
    await this.fetchData();
    if (this.myuser && this.myuser.id) {
      await this.fetchCitas();
    }
  },
  methods: {
    async fetchData() {
      try {
        this.myuser = await AccountRepository.getAccount();
        this.user = await UsuarioRepository.findOne(this.myuser.id);

        if (this.user && this.user.profileImageUrl) {
          this.imageUrl = this.user.profileImageUrl;
        }
      } catch (err) {
        console.error("Error cargando el perfil del usuario", err);
      }
    },
    async fetchCitas() {
      try {
        this.citas = await CitaRepository.getCitasBarbero();
      } catch (error) {
        console.error("Error obteniendo las citas del barbero", error);
      }
    },
    async confirmarCita(citaId) {
      try {
        await CitaRepository.confirmarCita(citaId);
        await this.fetchCitas();
        alert("Cita confirmada exitosamente");
      } catch (error) {
        console.error("Error confirmando cita", error);
        alert("Error al confirmar la cita");
      }
    },
    async rechazarCita(citaId) {
      try {
        await CitaRepository.rechazarCita(citaId);
        await this.fetchCitas();
        alert("Cita rechazada exitosamente");
      } catch (error) {
        console.error("Error rechazando cita", error);
        alert("Error al rechazar la cita");
      }
    },
    handleImageUpload(newImageUrl) {
      this.imageUrl = newImageUrl;
    },
  },
};
</script>
