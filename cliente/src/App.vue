<template>
  <div class="global-background">
    <nav class="navbar navbar-expand-lg navbar-transparent" :style="{ color: textColor }">
      <div class="container-fluid">
        <router-link class="navbar-brand" to="/home">
          <img src="@/assets/logoBarber.png" alt="Logo" class="logo-circle" />
        </router-link>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
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
                Nuestros Servicios
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/trending" class="nav-link" active-class="active">
                Tendencias
              </router-link>
            </li>
            <li class="nav-item" v-if="isCliente">
              <router-link to="/misCitas" class="nav-link" active-class="active">
                Mis Citas
              </router-link>
            </li>
            <li class="nav-item" v-if="isEmpleado">
              <router-link to="/empleadoCitas" class="nav-link" active-class="active">
                Mis Citas
              </router-link>
            </li>
            <li class="nav-item" v-if="isJefe">
              <router-link to="/users/empleados" class="nav-link" active-class="active">
                Empleados
              </router-link>
            </li>
            <li class="nav-item" v-if="isJefe">
              <router-link to="/users/clientes" class="nav-link" active-class="active">
                Clientes
              </router-link>
            </li>
            <li class="nav-item" v-if="isJefe">
              <router-link to="/promociones" class="nav-link" active-class="active">
                Promociones
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
  </div>
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
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeUnmount() {
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    handleScroll() {
      const navbar = document.querySelector(".navbar");
      if (window.scrollY > 50) {
        navbar.classList.add("scrolled");
      } else {
        navbar.classList.remove("scrolled");
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



<style>
/* Fondo global */
.global-background {
  /*position: relative;*/
  background-image: url('~@/assets/background.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
  width: 100%;
  position: relative;
}


.global-background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 1;
}

.global-background>* {
  position: relative;
  z-index: 2;
}
</style>

<style scoped>
.router-view {
  margin-top: 80px;
}

.navbar {
  background-color: transparent;
  /* Inicialmente transparente */
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
  transition: background-color 0.4s ease, box-shadow 0.4s ease;
}

.navbar.scrolled {
  background-color: rgba(0, 0, 0, 0.8);
  /* Color al hacer scroll */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  /* Sombra para efecto */
}

.navbar .nav-link {
  color: white !important;
}

.navbar .nav-link:hover {
  color: rgba(15, 105, 230, 0.8) !important;
}

.navbar-brand {
  color: white !important;
  /* Logo en blanco */
}

.logo-circle {
  height: 40px;
  width: 40px;
  border-radius: 50%;
  /* hace que la imagen sea circular */
  object-fit: cover;
  /* para que la imagen se ajuste dentro del círculo */
}
</style>