<template>
  <div class="container py-5">
    <h2 class="text-center text-primary mb-4">Elige tu tipo de rostro</h2>
    <div class="grid-container">
      <div 
        class="grid-item" 
        v-for="estructura in estructurasFaciales" 
        :key="estructura"
        @click="irAPeinadosRecomendados(estructura)"
      >
        <img :src="getImagenEstructura(estructura)" :alt="estructuraFacialMap[estructura]" />
        <div class="image-overlay">
          <h5>{{ estructuraFacialMap[estructura] }}</h5>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { estructuraFacialMap } from "@/estructuraFacialMap";

export default {
  data() {
    return {
      estructuraFacialMap, 
      estructurasFaciales: Object.keys(estructuraFacialMap),
    };
  },
  methods: {
    getImagenEstructura(estructura) {
      if (!estructuraFacialMap[estructura]) {
        console.warn(`No se encontró estructura para: ${estructura}`);
        return require("@/assets/defaultBanner.jpg");
      }

      const nombreImagen = estructura
        .normalize("NFD") // elimina acentos
        .replace(/[\u0300-\u036f]/g, "")
        //.toLowerCase()
        .replace(/\s+/g, "_"); // sustituye espacios por "_"

      try {
        return require(`@/assets/${nombreImagen}.jpeg`);
      } catch (e) {
        console.warn(`Imagen no encontrada para ${nombreImagen}, usando imagen por defecto.`);
        return require("@/assets/defaultBanner.jpg");
      }
    },
    irAPeinadosRecomendados(estructura) {
      this.$router.push(`/trending/recomendador/${estructura}`);
    },
  },
};
</script>

<style scoped>
h2 {
  margin-top: 50px;
  font-size: 2.5rem;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.95);
  color: #0d6efd;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.grid-container {
  margin-top: 50px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  padding: 0 20px;
}

.grid-item {
  position: relative;
  cursor: pointer;
  overflow: hidden;
  border-radius: 10px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.grid-item:hover {
  transform: translateY(-10px) scale(1.05);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.grid-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.grid-item:hover img {
  transform: scale(1.2);
}

.image-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 10px;
  text-align: center;
  opacity: 1;
  transition: opacity 0.3s ease;
}

.grid-item:hover .image-overlay {
  opacity: 1;
}

h5 {
  margin: 0;
  font-size: 1.2rem;
  font-weight: bold;
  text-transform: capitalize;
}
</style>
