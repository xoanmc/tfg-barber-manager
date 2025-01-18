<template>
    <div class="container py-5">
        <h2 class="text-center text-primary mb-4">Añadir Nuevo Peinado</h2>
        <form @submit.prevent="guardarPeinado">
            <div class="mb-3">
                <label for="nombre" class="form-label">Nombre del Peinado</label>
                <input type="text" id="nombre" class="form-control" v-model="nuevoPeinado.nombre" required />
            </div>
            <div class="mb-3">
                <label for="descripcion" class="form-label">Descripción</label>
                <textarea id="descripcion" class="form-control" v-model="nuevoPeinado.descripcionTendencias"
                    required></textarea>
            </div>
            <div class="mb-3">
                <label for="imagen" class="form-label">Imagen</label>
                <input type="file" id="imagen" class="form-control" @change="onFileChange" required />
            </div>
            <div class="mb-3">
                <label for="estructurasFaciales" class="form-label">Estructuras Faciales</label>
                <select id="estructurasFaciales" class="form-control" multiple
                    v-model="nuevoPeinado.estructurasFaciales">
                    <option v-for="estructura in estructurasFaciales" :key="estructura"
                        :value="estructura.toUpperCase()">
                        {{ estructura }}
                    </option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Guardar Peinado</button>
        </form>
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
        };
    },
    methods: {
        onFileChange(event) {
            this.nuevoPeinado.imagen = event.target.files[0];
        },
        async guardarPeinado() {
            try {
                const formData = new FormData();
                formData.append("nombre", this.nuevoPeinado.nombre);
                formData.append("descripcionTendencias", this.nuevoPeinado.descripcionTendencias);
                formData.append("estructurasFaciales", this.nuevoPeinado.estructurasFaciales.join(","));
                formData.append("imagen", this.nuevoPeinado.imagen);

                await PeinadoRepository.guardarPeinado(formData);

                alert("Peinado guardado con éxito");

                // Redirige a la lista y recarga los peinados
                this.$router.push("/trending");
            } catch (error) {
                console.error("Error guardando el peinado:", error);
                alert("Hubo un error al guardar el peinado");
            }
        }
        ,
    },
};
</script>

<style scoped>
h2 {
    font-size: 2rem;
    font-weight: bold;
    margin-bottom: 20px;
}

.form-control {
    margin-bottom: 15px;
}

.btn-primary {
    background-color: #007bff;
    border: none;
    color: white;
    font-size: 1.2rem;
}

.btn-primary:hover {
    background-color: #0056b3;
}
</style>