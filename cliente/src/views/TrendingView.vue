<template>
  <div class="container py-5">
    <h2 class="text-center text-primary mb-4">Peinados en Tendencia</h2>

    <!-- Grid de peinados -->
    <div v-if="peinados.length > 0" class="grid-container">
      <div class="grid-item" v-for="peinado in peinados" :key="peinado.id">
        <img :src="require(`@/assets/${peinado.imagen}`)" :alt="peinado.nombre" />
        
        <!-- Botón de check siempre visible -->
        <div v-if="isJefe" class="toggle-container">
          <button
            class="toggle-button"
            :class="peinado.tendencia ? 'btn-success' : 'btn-secondary'"
            @click="toggleTendencia(peinado.id)"
          >
            {{ peinado.tendencia ? "✓" : "✕" }}
          </button>
        </div>
        
        <!-- Descripción que aparece al pasar el cursor -->
        <div class="image-overlay">
          <h5>{{ peinado.nombre }}</h5>
          <p class="mb-0">{{ peinado.descripcionTendencias }}</p>
        </div>
      </div>
    </div>

    <!-- Mensaje cuando no hay peinados -->
    <div v-else class="text-center mt-5">
      <p class="text-muted">No hay peinados disponibles actualmente.</p>
    </div>
  </div>
</template>

<script>
import auth from "@/common/auth";
import PeinadoRepository from "@/repositories/PeinadoRepository";

export default {
  data() {
    return {
      peinados: [], // Lista de peinados
      isJefe: false, // Indica si el usuario es jefe
    };
  },
  async mounted() {
    try {
      // Determinar si el usuario es jefe
      this.isJefe = auth.isJefe();

      // Cargar peinados según el rol del usuario
      const peinadosCargados = this.isJefe
        ? await PeinadoRepository.obtenerPeinados() // Cargar todos los peinados si es jefe
        : await PeinadoRepository.obtenerPeinadosEnTendencia(); // Solo peinados en tendencia para otros usuarios

      // Mapear peinados con las imágenes correspondientes
      this.peinados = peinadosCargados.map((peinado) => ({
        ...peinado,
        imagen: this.getImagenFileName(peinado.nombre),
      }));
    } catch (error) {
      console.error("Error al cargar los peinados:", error);
    }
  },
  methods: {
    async toggleTendencia(peinadoId) {
      try {
        await PeinadoRepository.toggleTendencia(peinadoId);
        const peinado = this.peinados.find((p) => p.id === peinadoId);
        peinado.tendencia = !peinado.tendencia; // Alternar el estado de tendencia
      } catch (error) {
        console.error("Error al actualizar la tendencia:", error);
      }
    },
    getImagenFileName(nombre) {
      const mapping = {
        "The Modern Pompadour": "The Modern Pompadour.png",
        "Textured Crop": "Textured Crop.png",
        "Messy Quiff": "Messy Quiff.png",
        "Mid Length Waves": "Mid Length Waves.png",
        "Sleek Side Part": "Sleek Side Part.png",
        "Buzz Cut": "Buzz Cut.png",
        "Tapered Fade": "Tapered Fade.png",
        "Modern Mullet": "Modern Mullet.png",
        "Natural Waves and Curls": "Natural Waves and Curls.png",
        "Textured Undercuts": "Textured Undercuts.png",
      };

      return mapping[nombre] || "defaultBanner.jpg"; // Imagen por defecto
    },
  },
};
</script>

<style scoped>
h2 {
  font-size: 2.5rem;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95);
  color: #0d6efd;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.grid-item {
  border-radius: 12px;
  overflow: hidden;
  position: relative;
  aspect-ratio: 1/1;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.grid-item:hover {
  transform: translateY(-5px);
}

.grid-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.grid-item:hover img {
  transform: scale(1.1);
}

.image-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  color: white;
  padding: 1.5rem;
  transform: translateY(100%);
  transition: transform 0.3s ease;
  opacity: 0;
  /* Ocultar inicialmente */
}

.grid-item:hover .image-overlay {
  opacity: 1;
  /* Mostrar al pasar el cursor */
  transform: translateY(0);
}

.toggle-container {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 2;
  /* Asegura que esté encima de la imagen */
}

.toggle-button {
  margin-top: 10px;
  padding: 5px 10px;
  font-size: 1rem;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.btn-success {
  background-color: #28a745;
  color: white;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.toggle-button:hover {
  opacity: 0.8;
}

.text-muted {
  font-size: 1.2rem;
  color: #777;
}
</style>
