<template>
    <div class="barber-profile-container">
      <div class="profile-header">
        <img :src="barber.imagenPerfil || defaultProfileImage" alt="Imagen del barbero" class="profile-img" />
        <h1>{{ barber.nombre }} {{ barber.apellido }}</h1>
        <p>{{ barber.descripcion }}</p>
      </div>
  
      <div v-if="barber.horarios.length" class="schedule-section">
        <h2>Horarios de Atención</h2>
        <ul class="schedule-list">
          <li v-for="horario in barber.horarios" :key="horario.id">
            {{ horario.diaSemana }}: {{ horario.horaInicio }} - {{ horario.horaFin }}
          </li>
        </ul>
      </div>
    </div>
  </template>
  
  <script>
  import BarberProfileRepository from "@/repositories/BarberProfileRepository";
  
  export default {
    data() {
      return {
        barber: {
          nombre: "",
          apellido: "",
          descripcion: "",
          imagenPerfil: "",
          horarios: [],
        },
        defaultProfileImage: require("@/assets/defaultProfileImage.jpg"),
      };
    },
    async mounted() {
      const login = this.$route.params.login;
      try {
        this.barber = await BarberProfileRepository.getBarberProfileByLogin(login);
      } catch (error) {
        console.error("Error al cargar el perfil:", error);
      }
    },
  };
  </script>
  
  <style scoped>
  .barber-profile-container {
    padding: 40px;
    max-width: 800px;
    margin: 0 auto;
  }
  
  .profile-header {
    text-align: center;
    margin-bottom: 30px;
  }
  
  .profile-img {
    width: 150px;
    height: 150px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .schedule-section {
    margin-top: 30px;
  }
  
  .schedule-list {
    list-style: none;
    padding: 0;
  }
  
  .schedule-list li {
    background: #f1f1f1;
    padding: 10px;
    margin-bottom: 5px;
    border-radius: 8px;
  }
  </style>