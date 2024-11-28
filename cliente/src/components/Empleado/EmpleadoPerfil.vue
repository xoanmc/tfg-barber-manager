<template>
  <div class="container py-5">
    <!-- Mostrar Spinner mientras carga -->
    <div v-if="loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
    </div>

    <!-- Mostrar información del empleado -->
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
              <p><strong>Horario:</strong> {{ myuser.horario }}</p>
              <p><strong>Descripción:</strong> {{ myuser.descripcion }}</p>
              <button class="btn btn-primary mt-4">Editar Perfil</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Mostrar mensaje de error si no se cargan los datos -->
    <div v-else class="text-center text-danger">
      <p>Error al cargar los datos del perfil. Por favor, intenta de nuevo más tarde.</p>
    </div>
  </div>
</template>

### Script

```javascript
<script>
import AccountRepository from "@/repositories/AccountRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ProfileImage from "@/components/ProfileImage";

export default {
  components: {
    ProfileImage,
  },
  data() {
    return {
      myuser: null,
      imageUrl: "",
      loading: true,
      error: false, // Bandera para detectar errores
    };
  },
  async mounted() {
    try {
      await this.fetchData(); // Carga los datos al montar el componente
    } catch (err) {
      console.error("Error en la carga inicial:", err);
      this.error = true; // Establece un error si ocurre un fallo
    } finally {
      this.loading = false; // Siempre desactiva el spinner al finalizar
    }
  },
  methods: {
    async fetchData() {
      try {
        this.myuser = await AccountRepository.getAccount(); // Obtener los datos del usuario autenticado
        if (this.myuser?.id) {
          const user = await UsuarioRepository.findOne(this.myuser.id); // Consultar los datos adicionales
          if (user?.profileImageUrl) this.imageUrl = user.profileImageUrl;
        } else {
          throw new Error("El ID del usuario no es válido."); // Lanzar un error si falta el ID
        }
      } catch (err) {
        console.error("Error al cargar los datos del perfil:", err);
        this.error = true;
      }
    },
    handleImageUpload(newImageUrl) {
      this.imageUrl = newImageUrl; // Actualiza la imagen al subir una nueva
    },
  },
};
</script>
