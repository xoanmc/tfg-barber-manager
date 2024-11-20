<template>
  <nav class="navbar navbar-expand-lg bg-light">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/home"> TFG Barber </router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link to="/home" class="nav-link" aria-current="page" active-class="active">
              Inicio
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/about" active-class="active">
              Acerca de
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/services" active-class="active">
              Servicios
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/gallery" active-class="active">
              Galería
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/trending" active-class="active">
              Tendencias
            </router-link>
          </li>
          <!-- Botón para reservar con verificación -->
          <li class="nav-item" v-if="isCliente">
            <a class="nav-link" @click="irAReservar">Reservar</a>
          </li>
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item" v-if="!store.state.user.logged">
            <router-link class="nav-link" to="/login" active-class="active">
              Login
            </router-link>
          </li>
          <li class="nav-item" v-if="!store.state.user.logged">
            <router-link class="nav-link" to="/register" active-class="active">
              Registrarse
            </router-link>
          </li>
          <li class="nav-item" v-if="store.state.user.logged">
            <a class="nav-link" @click="desautenticarme()"> Logout </a>
          </li>
          <li class="nav-item" v-if="isCliente">
            <router-link class="nav-link" to="/perfilCliente" active-class="active">
              Perfil
            </router-link>
          </li>
          <li class="nav-item" v-if="isEmpleado">
            <router-link class="nav-link" to="/perfilEmpleado" active-class="active">
              Perfil
            </router-link>
          </li>
          <li class="nav-item" v-if="isJefe">
            <router-link class="nav-link" to="/perfilJefe" active-class="active">
              Perfil
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <router-view />
</template>

<script>
import { getStore } from "./common/store";
import auth from "./common/auth";

export default {
  data() {
    return {
      store: getStore(),
    };
  },
  computed: {
    isLogged() {
      return this.store.state.user.logged;
    },
    isCliente() {
      return auth.isCliente();
    },
    isEmpleado() {
      return auth.isEmpleado();
    },
    isJefe() {
      return auth.isJefe();
    },
  },
  methods: {
    desautenticarme() {
      auth.logout();
      this.$router.push("/login");
    },
    irAReservar() {
      // Verificación de autenticación antes de redirigir
      if (this.isLogged) {
        this.$router.push("/reserve");
      } else {
        this.$router.push("/login");
      }
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  cursor: pointer;
}

/* fixing popper warning in bootstrap 5.2: */
.dropdown-menu {
  margin-top: 0px !important;
}
</style>
