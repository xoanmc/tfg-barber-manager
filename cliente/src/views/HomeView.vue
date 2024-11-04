<template>
  <div class="home-container">
    <!-- Logo de la Barbería -->
    <div class="logo-container">
      <img alt="Vue logo" src="../assets/logoBarber.png" />
    </div>

    <!-- Mostrar el nombre de la barbería -->
    <h1>{{ homePageInfo.nombre || "Nombre barbería" }}</h1>

    <!-- Mostrar la descripción de la barbería -->
    <p>
      {{
        homePageInfo.descripcion ||
        "Breve descripción sobre la barbería en concreto"
      }}
    </p>

    <!-- Formulario para modificar el nombre y descripción (solo visible para el jefe) -->
    <div v-if="isJefe" class="form-container">
      <h2>Modificar Información de la Barbería</h2>
      <form @submit.prevent="guardarInformacion">
        <div class="form-group">
          <label for="nombre">Nombre de la Barbería</label>
          <input
            v-model="homePageInfo.nombre"
            type="text"
            id="nombre"
            placeholder="Nombre de la barbería"
          />
        </div>
        <div class="form-group">
          <label for="descripcion">Descripción de la Barbería</label>
          <textarea
            v-model="homePageInfo.descripcion"
            id="descripcion"
            placeholder="Descripción de la barbería"
          ></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Guardar</button>
      </form>
    </div>

    <!-- Botón para reservar citas -->
    <button class="btn-reservar" @click="reservar">Reserva Ya!</button>
  </div>
</template>

<script>
import auth from "@/common/auth";
import HomePageInfoRepository from "@/repositories/HomePageInfoRepository";

export default {
  name: "HomeView",
  data() {
    return {
      homePageInfo: {
        nombre: "",
        descripcion: "",
      },
      isJefe: auth.isJefe(),
    };
  },
  mounted() {
    this.cargarInformacion();
  },
  methods: {
    // Cargar la información de la página de inicio
    async cargarInformacion() {
      try {
        const response = await HomePageInfoRepository.getHomePageInfo();
        this.homePageInfo = response;
      } catch (error) {
        console.error(
          "Error cargando la información de la página de inicio",
          error
        );
      }
    },
    // Guardar la información modificada
    async guardarInformacion() {
      try {
        await HomePageInfoRepository.updateHomePageInfo(this.homePageInfo);
        alert("Información actualizada con éxito");
      } catch (error) {
        console.error(
          "Error guardando la información de la página de inicio",
          error
        );
        alert("Hubo un error al guardar la información");
      }
    },
    reservar() {
      // Aquí verificamos si el usuario está autenticado correctamente
      if (auth.isLogged()) {
        this.$router.push("/reserve"); // Redirigir al formulario de reservas
      } else {
        this.$router.push("/login"); // Si no está logueado, redirige al login
      }
    },
  },
};
</script>

<style scoped>
.home-container {
  text-align: center;
  padding: 20px;
}

.logo-container img {
  max-width: 200px;
}

.form-container {
  margin-top: 20px;
}

.btn-reservar {
  background-color: yellow;
  padding: 15px;
  font-size: 20px;
  font-weight: bold;
  border: none;
  cursor: pointer;
  margin-top: 20px;
}

.btn-reservar:hover {
  background-color: darkorange;
}

.form-group {
  margin-bottom: 15px;
}

.btn {
  margin-top: 10px;
}
</style>
