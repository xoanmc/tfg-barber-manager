<template>
    <div class="container py-5">
        <h2 class="text-center text-primary mb-4">Peinados Recomendados para {{ tipoRostro }}</h2>
        <div v-if="peinados.length > 0" class="grid-container">
            <div class="grid-item" v-for="peinado in peinados" :key="peinado.id">
                <img :src="getImagenSrc(peinado)" :alt="peinado.nombre" />
                <div class="image-overlay">
                    <h5>{{ peinado.nombre }}</h5>
                    <p class="mb-0">{{ peinado.descripcionRecomendador }}</p>
                </div>
            </div>
        </div>

        <div v-else class="text-center mt-5">
            <p class="text-muted">No hay peinados recomendados para esta estructura facial.</p>
        </div>
    </div>
</template>



<script>
import PeinadoRepository from "@/repositories/PeinadoRepository";

export default {
    data() {
        return {
            tipoRostro: this.$route.params.estructuraFacial,
            peinados: [],
        };
    },
    async mounted() {
        try {
            // Obtener peinados recomendados según la estructura facial
            const peinadosCargados = await PeinadoRepository.obtenerPeinadosPorEstructura(this.tipoRostro);

            // Mapear imágenes nuevas y precargadas
            this.peinados = peinadosCargados.map((peinado) => ({
                ...peinado,
                imagen: this.isImagenFija(peinado.nombre)
                    ? require(`@/assets/${this.getImagenFileName(peinado.nombre)}`)
                    : peinado.urlImagen,
            }));

            console.log("Peinados recomendados cargados:", this.peinados);
        } catch (error) {
            console.error("Error al cargar peinados recomendados:", error);
        }
    },
    methods: {
        getImagenSrc(peinado) {
            if (this.isImagenFija(peinado.nombre)) {
                return require(`@/assets/${this.getImagenFileName(peinado.nombre)}`);
            }
            if (peinado.urlImagen) {
                return peinado.urlImagen; // Usar URL dinámica del backend si está disponible
            }
            return require("@/assets/defaultBanner.jpg"); // Imagen predeterminada si no se encuentra
        },
        isImagenFija(nombre) {
            // Lista de nombres con imágenes fijas
            const nombresConImagenesFijas = [
                "The Modern Pompadour",
                "Textured Crop",
                "Messy Quiff",
                "Mid Length Waves",
                "Sleek Side Part",
                "Buzz Cut",
                "Tapered Fade",
                "Modern Mullet",
                "Natural Waves and Curls",
                "Textured Undercuts",
            ];
            return nombresConImagenesFijas.includes(nombre);
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
    transition: transform 0.3s ease, opacity 0.3s ease;
    opacity: 0;
}

.grid-item:hover .image-overlay {
    opacity: 1;
    transform: translateY(0);
}

.text-muted {
    font-size: 1.2rem;
    color: #777;
}
</style>