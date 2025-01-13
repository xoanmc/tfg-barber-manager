<template>
    <div class="container">
        <div class="text-center">
            <h2 class="title">Opiniones de Clientes</h2>
            <!-- Botón de escribir reseña solo visible para clientes -->
            <button v-if="isCliente" class="btn btn-primary mt-4" @click="showModal = true">Escribir una
                opinión</button>
        </div>

        <div class="row mt-4" v-if="reviews.length">
            <div class="col-md-6 col-lg-4 mb-4" v-for="review in reviews" :key="review.id">
                <div class="card review-card shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">{{ review.cliente.nombre }}</h5>
                        <p class="card-text">"{{ review.comentario }}"</p>
                        <div class="stars-container text-warning">
                            <i class="bi bi-star-fill" v-for="index in review.calificacion"
                                :key="`filled-${review.id}-${index}`"></i>
                            <i class="bi bi-star" v-for="index in 5 - review.calificacion"
                                :key="`empty-${review.id}-${index}`"></i>
                        </div>
                        <!-- Botón eliminar solo visible para jefes -->
                        <button v-if="isJefe" class="btn btn-danger mt-2 w-100" @click="deleteReview(review.id)">
                            Eliminar
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <p v-else class="text-center">No hay opiniones aún.</p>

        <!-- Modal de escribir reseña -->
        <div v-if="showModal" class="modal-overlay">
            <div class="modal-content">
                <h3 class="text-center">Deja tu Opinión</h3>
                <div class="rating-container mb-3">
                    <span v-for="index in 5" :key="index" class="star"
                        :class="{ active: index <= newReview.calificacion }" @click="setRating(index)">
                        ★
                    </span>
                </div>
                <textarea v-model="newReview.comentario" placeholder="Escribe tu opinión..."
                    class="form-control mt-3"></textarea>
                <div class="d-flex justify-content-end mt-3">
                    <button class="btn btn-secondary me-2" @click="closeModal">Cerrar</button>
                    <button class="btn btn-primary" @click="submitReview">Publicar Reseña</button>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
import auth from "@/common/auth";
import { getStore } from "@/common/store";
import BarberReviewRepository from "@/repositories/BarberReviewRepository";

export default {
    props: {
        barberoLogin: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            reviews: [],
            showModal: false,
            isCliente: false, // Indica si el usuario es cliente autenticado
            newReview: {
                comentario: "",
                calificacion: 5,
                barberoLogin: "",
                cliente: { id: null, nombre: "" },
            },
            isJefe: false,
        };
    },
    methods: {
        async fetchReviews() {
            try {
                this.reviews = await BarberReviewRepository.getReviews(this.barberoLogin);
            } catch (error) {
                console.error("Error al obtener las reseñas:", error);
            }
        },
        async submitReview() {
            const token = auth.getToken();
            if (!token) {
                alert("Debes iniciar sesión como cliente para dejar una reseña.");
                this.$router.push("/login");
                return;
            }

            this.newReview.barberoLogin = this.barberoLogin;
            this.newReview.cliente = {
                id: localStorage.getItem("userId"),
                nombre: localStorage.getItem("username"),
                autoridad: "CLIENTE",
            };

            try {
                await BarberReviewRepository.addReview(this.newReview);
                this.fetchReviews();
                this.resetReviewForm();
                alert("¡Gracias por tu reseña!");
            } catch (error) {
                console.error("Error al publicar la reseña:", error);
                alert("Hubo un error al publicar la reseña.");
            }
        },
        async deleteReview(reviewId) {
            if (!confirm("¿Estás seguro de que quieres eliminar esta reseña?")) {
                return;
            }

            try {
                await BarberReviewRepository.deleteReview(reviewId);
                this.reviews = this.reviews.filter(review => review.id !== reviewId);
                alert("Reseña eliminada con éxito.");
            } catch (error) {
                console.error("Error al eliminar la reseña:", error);
                alert("No se pudo eliminar la reseña. Por favor, inténtalo de nuevo.");
            }
        },
        setRating(rating) {
            this.newReview.calificacion = rating;
        },
        resetReviewForm() {
            this.newReview.comentario = "";
            this.newReview.calificacion = 5;
            this.showModal = false;
        },
        closeModal() {
            this.showModal = false;
        },
        async checkAuth() {
            try {
                await auth.isAuthenticationChecked;  // Espera a que se autentique
                this.isCliente = auth.isCliente();  // Actualiza el estado de cliente
                this.isJefe = auth.isJefe();  // Actualiza el estado de jefe
                console.log("Rol del usuario:", getStore().state.user.autoridad);  // Muestra en consola
            } catch (error) {
                console.error("Error verificando autenticación:", error);
                this.isCliente = false;
                this.isJefe = false;
            }
        },
    },
    mounted() {
        this.fetchReviews();
        this.checkAuth();
    },
};
</script>

<style scoped>
.container {
    margin-top: 30px;
}

.title {
    font-size: 2rem;
    font-weight: bold;
}

.review-card {
    border-radius: 10px;
    background-color: #fff;
    transition: transform 0.2s ease-in-out;
}

.review-card:hover {
    transform: scale(1.05);
}

.card-body {
    padding: 20px;
}

.card-title {
    font-size: 1.2rem;
    font-weight: bold;
    text-align: center;
    margin-bottom: 10px;
}

.card-text {
    font-size: 1rem;
    margin-bottom: 10px;
    text-align: center;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-content {
    background: white;
    padding: 30px;
    border-radius: 12px;
    width: 400px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.rating-container {
    text-align: center;
    font-size: 2rem;
}

.star {
    font-size: 2rem;
    cursor: pointer;
    color: #ccc;
    transition: color 0.3s;
}

.star.active {
    color: gold;
}

textarea {
    width: 100%;
    height: 100px;
    border-radius: 8px;
    padding: 10px;
    border: 1px solid #ccc;
    resize: none;
}

.stars-container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 10px;
}

.stars-container i {
    font-size: 24px;
    margin: 0 2px;
}
</style>
