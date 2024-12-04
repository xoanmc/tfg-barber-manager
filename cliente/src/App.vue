<template>
  <nav class="navbar navbar-expand-lg bg-light">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/home">TFG Barber</router-link>
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
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item dropdown" v-if="store.state.user.logged">
            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown"
              aria-expanded="false">
              Hola, {{ store.state.user.login }}
            </a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
              <template v-if="isCliente">
                <li>
                  <a class="dropdown-item" @click="irAPerfil">Perfil</a>
                </li>
                <li>
                  <a class="dropdown-item" @click="verMisCitas">Mis citas</a>
                </li>
              </template>
              <template v-if="isEmpleado">
                <li>
                  <a class="dropdown-item" @click="irAPerfil">Perfil</a>
                </li>
                <li>
                  <a class="dropdown-item" @click="verMisCitas">Mis citas</a>
                </li>
              </template>
              <template v-if="isJefe">
                <li>
                  <a class="dropdown-item" @click="irAPerfil">Perfil</a>
                </li>
                <li>
                  <a class="dropdown-item" @click="irAGestionEmpleados">Gestión Empleados</a>
                </li>
                <li>
                  <a class="dropdown-item" @click="irAGestionClientes">Gestión Clientes</a>
                </li>
                <li>
                  <a class="dropdown-item" @click="irAGestionPromociones">Gestión Promociones</a>
                </li>
              </template>
              <li>
                <hr class="dropdown-divider" />
              </li>
              <li>
                <a class="dropdown-item text-danger" @click="desautenticarme">Cerrar Sesión</a>
              </li>
            </ul>
          </li>
          <li class="nav-item" v-if="!store.state.user.logged">
            <router-link class="nav-link" to="/login" active-class="active">
              Iniciar Sesión
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
    irAPerfil() {
      if (this.isCliente) {
        this.$router.push("/perfilCliente");
      } else if (this.isEmpleado) {
        this.$router.push("/perfilEmpleado");
      } else if (this.isJefe) {
        this.$router.push("/perfilJefe");
      }
    },
    verMisCitas() {
      const autoridad = this.store.state.user.autoridad;
      if (autoridad === "CLIENTE") {
        this.$router.push({ name: "MisCitas" });
      } else if (autoridad === "EMPLEADO") {
        this.$router.push({ name: "EmpleadoCitas" });
      } else {
        alert("Esta funcionalidad no está disponible para tu rol.");
      }
    },
    irAGestionEmpleados() {
      this.$router.push("/users/empleados");
    },
    irAGestionClientes() {
      this.$router.push("/users/clientes");
    },
    irAGestionPromociones() {
      this.$router.push("/promotions"); // definir esta ruta en el router
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

.dropdown-menu {
  right: 0;
  left: auto;
}

.dropdown-item.text-danger:hover {
  background-color: #f8d7da;
  color: #721c24;
}
</style>
