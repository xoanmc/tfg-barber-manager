<template>
    <div class="container py-5 d-flex justify-content-center align-items-center" style="min-height: calc(100vh - 56px);">
      <div class="card shadow-lg w-100" style="max-width: 500px; border-radius: 15px;">
        <div class="card-body p-4">
          <h2 class="text-center text-primary fw-bold mb-4">Añadir Nuevo Peinado</h2>
          <form @submit.prevent="guardarPeinado">
            <!-- Nombre del Peinado -->
            <div class="mb-3">
              <label for="nombre" class="form-label">Nombre del Peinado*</label>
              <input
                type="text"
                id="nombre"
                class="form-control"
                v-model="nuevoPeinado.nombre"
                required
              />
            </div>
  
            <!-- Descripción -->
            <div class="mb-3">
              <label for="descripcion" class="form-label">Descripción*</label>
              <textarea
                id="descripcion"
                class="form-control"
                v-model="nuevoPeinado.descripcionTendencias"
                rows="3"
                required
              ></textarea>
            </div>
  
            <!-- Imagen -->
            <div class="mb-3">
              <label for="imagen" class="form-label">Imagen*</label>
              <input
                type="file"
                id="imagen"
                class="form-control"
                @change="onFileChange"
                required
              />
            </div>
  
            <!-- Estructuras Faciales -->
            <div class="mb-3">
              <label for="estructurasFaciales" class="form-label">Estructuras Faciales*</label>
              <div class="form-check" v-for="estructura in estructurasFaciales" :key="estructura">
                <input
                  class="form-check-input"
                  type="checkbox"
                  :id="estructura"
                  :value="estructura.toUpperCase()"
                  v-model="nuevoPeinado.estructurasFaciales"
                />
                <label class="form-check-label" :for="estructura">
                  {{ estructura }}
                </label>
              </div>
            </div>
  
            <!-- Botón Guardar -->
            <div class="d-grid">
              <button class="btn btn-primary btn-lg rounded-pill" type="submit">
                Guardar Peinado
              </button>
            </div>
          </form>
  
          <!-- Mensaje de error -->
          <div v-if="error" class="text-danger mt-3 text-center">
            {{ error }}
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import PeinadoRepository from "@/repositories/PeinadoRepository";
  
  export default {
    data() {
      return {
        nuevoPeinado: {
          nombre: "",
          descripcionTendencias: "",
          estructurasFaciales: [],
          imagen: null,
        },
        estructurasFaciales: [
                "OVALADA",
                "REDONDA",
                "RECTANGULAR",
                "TRIANGULO_INVERTIDO",
                "CUADRADO",
                "TRIANGULO",
                "DIAMANTE",
                "CORAZON",
                "OVALADO_LARGO"
            ],
        error: "",
      };
    },
    methods: {
      onFileChange(event) {
        this.nuevoPeinado.imagen = event.target.files[0];
      },
      async guardarPeinado() {
        try {
          // Preparar datos para el envío
          const formData = new FormData();
          formData.append("nombre", this.nuevoPeinado.nombre);
          formData.append("descripcionTendencias", this.nuevoPeinado.descripcionTendencias);
          formData.append("estructurasFaciales", this.nuevoPeinado.estructurasFaciales.join(","));
          formData.append("imagen", this.nuevoPeinado.imagen);
  
          // Guardar el peinado
          await PeinadoRepository.guardarPeinado(formData);
  
          // Redirigir al listado de peinados
          this.$router.push("/trending");
        } catch (error) {
          console.error("Error guardando el peinado:", error);
          this.error = "Hubo un error al guardar el peinado. Por favor, inténtalo de nuevo.";
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .card {
    border: none;
    border-radius: 15px;
    background-color: #f8f9fa;
  }
  
  .card-body {
    padding: 2rem;
  }
  
  h2 {
    font-size: 1.8rem;
    font-weight: bold;
  }
  
  .form-label {
    font-size: 1rem;
    font-weight: 600;
    color: #6c757d;
  }
  
  .form-control {
    border-radius: 10px;
    box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
  }
  
  .form-control:focus {
    box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
    border-color: #007bff;
  }
  
  .form-check-label {
    font-size: 1rem;
    color: #495057;
  }
  
  .form-check-input:checked {
    background-color: #007bff;
    border-color: #007bff;
  }
  
  .btn-primary {
    background-color: #007bff;
    border-color: #007bff;
  }
  
  .btn-primary:hover {
    background-color: #0056b3;
    border-color: #004085;
  }
  
  .text-danger {
    font-size: 0.9rem;
  }
  </style>
  