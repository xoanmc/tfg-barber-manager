<template>
  <div class="container py-5">
    <h2 class="text-center text-primary mb-4">Peinados en Tendencia</h2>
    <div v-if="peinados.length > 0" class="grid-container">
      <div class="grid-item" v-for="peinado in peinados" :key="peinado.id">
        <img :src="require(`@/assets/${peinado.imagen}`)" :alt="peinado.nombre" />
        <div class="image-overlay">
          <h5>{{ peinado.nombre }}</h5>
          <p class="mb-0">{{ peinado.descripcionTendencias }}</p>
        </div>
      </div>
    </div>
    <div v-else class="text-center mt-5">
      <p class="text-muted">No hay peinados en tendencia actualmente.</p>
    </div>
  </div>
</template>

<script>
import PeinadoRepository from "@/repositories/PeinadoRepository";

export default {
  data() {
    return {
      peinados: [],
    };
  },
  async mounted() {
    try {
      const peinadosCargados = await PeinadoRepository.obtenerPeinadosEnTendencia();
      this.peinados = peinadosCargados.map((peinado) => {
        return {
          ...peinado,
          imagen: this.getImagenFileName(peinado.nombre),
        };
      });
    } catch (error) {
      console.error("Error al cargar los peinados en tendencia:", error);
    }
  },
  methods: {
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

      return mapping[nombre] || "defaultBanner.jpg"; // Imagen por defecto si no se encuentra
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
}

.grid-item:hover .image-overlay {
  transform: translateY(0);
}

.text-muted {
  font-size: 1.2rem;
  color: #777;
}
</style>
