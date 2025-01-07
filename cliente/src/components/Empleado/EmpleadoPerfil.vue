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
              <p><strong>Descripción:</strong> {{ myuser.descripcion }}</p>

              <div v-if="myuser.autoridad === 'EMPLEADO'" class="mt-4">
                <h4>Horario</h4>
                <ul class="list-group mt-3">
                  <li
                    v-for="(horario, index) in horariosFormateados"
                    :key="index"
                    class="list-group-item"
                  >
                    {{ horario.intervaloDias }}: {{ horario.horaInicio }} - {{ horario.horaFin }}
                  </li>
                </ul>
              </div>

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
import HorariosBarberosRepository from "@/repositories/HorariosBarberosRepository";
import ProfileImage from "@/components/ProfileImage";
import ImageRepository from "@/repositories/ImageRepository";
import eventBus from "@/eventBus";

export default {
  components: {
    ProfileImage,
  },
  data() {
    return {
      myuser: null,
      imageUrl: "",
      loading: true,
      horarios: [], // Horarios obtenidos del backend
      horariosFormateados: [], // Horarios agrupados y formateados
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
        const user = await UsuarioRepository.findOne(this.myuser.id);
        if (user?.profileImageUrl) this.imageUrl = user.profileImageUrl;

        
        if (this.myuser.autoridad === "EMPLEADO") {
          this.horarios = await HorariosBarberosRepository.obtenerHorariosPorBarbero(this.myuser.id);
          this.horariosFormateados = this.formatearHorarios(this.horarios);
        }
      } catch (err) {
        console.error("Error cargando el perfil del empleado:", err);
      } finally {
        this.loading = false;
      }
    },
    formatearHorarios(horarios) {
      const diasOrdenados = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"];
      const grupos = [];

      // Ordenar los horarios por día de la semana
      horarios.sort((a, b) => diasOrdenados.indexOf(a.diaSemana) - diasOrdenados.indexOf(b.diaSemana));

      let grupoActual = { dias: [], horaInicio: null, horaFin: null };

      for (const horario of horarios) {
        if (
          grupoActual.horaInicio === horario.horaInicio &&
          grupoActual.horaFin === horario.horaFin &&
          grupoActual.dias.length > 0 &&
          diasOrdenados.indexOf(horario.diaSemana) === diasOrdenados.indexOf(grupoActual.dias[grupoActual.dias.length - 1]) + 1
        ) {
          // Si es consecutivo y tiene el mismo horario, añadir el día al grupo
          grupoActual.dias.push(horario.diaSemana);
        } else {
          // Guardar el grupo actual y empezar uno nuevo
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

      // Guardar el último grupo
      if (grupoActual.dias.length > 0) {
        grupos.push(grupoActual);
      }

      // Formatear los grupos para la vista
      return grupos.map((grupo) => ({
        intervaloDias: grupo.dias.length > 1 ? `${grupo.dias[0]} - ${grupo.dias[grupo.dias.length - 1]}` : grupo.dias[0],
        horaInicio: grupo.horaInicio,
        horaFin: grupo.horaFin,
      }));
    },
    handleImageUpload(newImageUrl) {
      this.imageUrl = newImageUrl;
      eventBus.emit("profile-image-updated");
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

.list-group {
  margin-top: 10px;
}

.list-group-item {
  text-align: left;
}
</style>
