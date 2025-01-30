<template>
  <div class="spacer"></div>
  <div class="barber-profile-container">
    <div class="profile-header">
      <h2 class="barber-name">{{ barberProfile.nombre }} {{ barberProfile.apellido }}</h2>
      <p class="login">@{{ barberProfile.login || "Sin login disponible" }}</p>
    </div>

    <div class="profile-main-section">
      <div class="profile-image-container">
        <img :src="barberProfile.imagenPerfil || defaultImage" alt="Foto del barbero" class="profile-image">
      </div>
      <div class="profile-description">
        <p>{{ barberProfile.descripcion || "Este barbero aún no ha proporcionado su biografía." }}</p>
      </div>
    </div>

    <div class="horarios-section">
      <h2 class="text-center text-primary">Horario</h2>
      <ul class="horarios-list">
        <li v-for="(horario, index) in horariosFormateados" :key="index">
          <strong>{{ horario.intervaloDias }}</strong>: {{ horario.horaInicio }} - {{ horario.horaFin }}
        </li>
        <li v-if="!horariosFormateados.length">Este barbero aún no ha proporcionado horarios.</li>
      </ul>
    </div>

    <!-- Sección de reseñas -->
    <div class="reviews-section mt-5" v-if="barberProfile.login">
      <barber-review :barbero-login="barberProfile.login" :is-cliente="isCliente"></barber-review>
    </div>
    <p v-else>Cargando reseñas...</p>
  </div>
</template>

<script>
import BarberProfileRepository from "@/repositories/BarberProfileRepository";
import BarberReview from "@/views/BarberReviewView.vue";

export default {
  components: {
    BarberReview,
  },
  data() {
    return {
      barberProfile: {},
      defaultImage: require('@/assets/defaultProfileImage.jpg'),
      horariosFormateados: [],
      isCliente: true, 
    };
  },
  methods: {
    formatearHorarios(horarios) {
      const diasOrdenados = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"];
      const grupos = [];
      horarios.sort((a, b) => diasOrdenados.indexOf(a.diaSemana) - diasOrdenados.indexOf(b.diaSemana));

      let grupoActual = { dias: [], horaInicio: null, horaFin: null };

      for (const horario of horarios) {
        if (
          grupoActual.horaInicio === horario.horaInicio &&
          grupoActual.horaFin === horario.horaFin &&
          grupoActual.dias.length > 0 &&
          diasOrdenados.indexOf(horario.diaSemana) === diasOrdenados.indexOf(grupoActual.dias[grupoActual.dias.length - 1]) + 1
        ) {
          grupoActual.dias.push(horario.diaSemana);
        } else {
          if (grupoActual.dias.length > 0) {
            grupos.push({ ...grupoActual });
          }
          grupoActual = {
            dias: [horario.diaSemana],
            horaInicio: horario.horaInicio,
            horaFin: horario.horaFin,
          };
        }
      }
      if (grupoActual.dias.length > 0) {
        grupos.push(grupoActual);
      }

      return grupos.map((grupo) => ({
        intervaloDias: grupo.dias.length > 1 ? `${grupo.dias[0]} - ${grupo.dias[grupo.dias.length - 1]}` : grupo.dias[0],
        horaInicio: grupo.horaInicio,
        horaFin: grupo.horaFin,
      }));
    },
  },
  async created() {
    const login = this.$route.params.login;
    try {
      const response = await BarberProfileRepository.getBarberProfile(login);
      this.barberProfile = response;
      console.log("Perfil cargado:", this.barberProfile);
      if (response.horarios) {
        this.horariosFormateados = this.formatearHorarios(response.horarios);
      }
    } catch (error) {
      console.error("Error al cargar el perfil del barbero:", error);
    }
  },
};
</script>

<style scoped>
.spacer {
  height: 100px;
}

.barber-profile-container {
  padding: 50px 20px 40px;
  max-width: 900px;
  margin: 0 auto;
  background-color: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.profile-header {
  text-align: center;
  margin-bottom: 30px;
}

.profile-main-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.profile-image-container {
  margin-bottom: 20px;
}

.profile-image {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  border: 4px solid #007bff;
  object-fit: cover;
}

.info-section {
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.profile-info,
.profile-description {
  width: 45%;
  text-align: center;
}

.barber-name {
  font-size: 1.8rem;
  font-weight: bold;
}

.login {
  font-size: 1.2rem;
  color: #6c757d;
}

.profile-description h3 {
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.profile-description p {
  font-size: 1.2rem;
  line-height: 1.6;
}

.horarios-section {
  margin-top: 40px;
}

.horarios-list {
  list-style: none;
  padding: 0;
  font-size: 1.2rem;
}

.horarios-list li {
  padding: 8px 0;
  border-bottom: 1px solid #ddd;
  text-align: center;
}

.horarios-list li:last-child {
  border-bottom: none;
}

.text-primary {
  color: #007bff;
}
</style>
