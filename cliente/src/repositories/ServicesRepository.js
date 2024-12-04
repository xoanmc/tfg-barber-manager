import HTTP from "@/common/http";

export default {

    async getServicios() {
        try {
            const response = await HTTP.get("/servicios");
            return response.data;
        } catch (error) {
            console.error("Error obteniendo servicios:", error);
            throw error;
        }
    },

    async findAllServicios() {
        try {
            const response = await HTTP.get("/servicios");
            return response.data;
        } catch (error) {
            console.error("Error obteniendo servicios:", error);
            throw error;
        }
    },

    async updateServicios(servicios) {
        try {
            return await HTTP.post("/servicios/update", servicios);
        } catch (error) {
            console.error("Error actualizando servicios:", error);
            throw error;
        }
    },

    async deleteServicio(id) {
        try {
            return await HTTP.delete(`/servicios/${id}`);
        } catch (error) {
            console.error("Error eliminando servicio:", error);
            throw error;
        }
    },
};