<template>
<nav
    class="navbar navbar-expand-lg navbar-transparent"
    :style="{ color: textColor }"
  >
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/home">TFG Barber</router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link to="/home" class="nav-link" active-class="active">
              Inicio
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/about" class="nav-link" active-class="active">
              Acerca de
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/services" class="nav-link" active-class="active">
              Servicios
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/gallery" class="nav-link" active-class="active">
              Galería
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/trending" class="nav-link" active-class="active">
              Tendencias
            </router-link>
          </li>
        </ul>
        <ul class="navbar-nav">
          <li
            class="nav-item dropdown"
            v-if="store.state.user.logged"
          >
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="userDropdown"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              Hola, {{ store.state.user.login }}
            </a>
            <ul
              class="dropdown-menu dropdown-menu-end"
              aria-labelledby="userDropdown"
            >
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
          <li
            class="nav-item"
            v-if="!store.state.user.logged"
          >
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
      textColor: "#ffffff",
    };
  },
  computed: {
    isHome() {
      return this.$route.path === "/home";
    },
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
  watch: {
    $route: "updateNavbarStyle",
  },
  mounted() {
    this.updateNavbarStyle();
  },
  methods: {
    updateNavbarStyle() {
      // Define los colores en función de la ruta
      if (this.isHome) {
        this.textColor = "#ffffff"; // Blanco para fondo oscuro
      } else {
        this.textColor = "#2c3e50"; // Oscuro para fondo claro
      }
    },
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

<style scoped>
.router-view {
  margin-top: 80px;
}

.navbar {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
  transition: background-color 0.3s ease, color 0.3s ease; /* Transiciones suaves */
}


.navbar-transparent {
  background: transparent !important;
}


.navbar-brand,
.nav-link,
.navbar-toggler-icon {
  transition: color 0.3s ease;
}


.navbar-transparent .nav-link {
  color: inherit !important;
}

.navbar-transparent .nav-link:hover {
  color: rgba(15, 105, 230, 0.8) !important; /* Color al pasar el mouse */
}
</style>
