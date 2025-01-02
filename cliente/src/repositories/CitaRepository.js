import HTTP from "@/common/http";

export default {
    async getMisCitas() {
        try {
            const response = await HTTP.get("/citas/cliente/mis-citas");
            return response.data;
        } catch (error) {
            console.error("Error obteniendo las citas del cliente", error);
            throw error;
        }
    },

    async getCitasCliente(clienteId) {
        try {
            const response = await HTTP.get(`/citas/cliente/${clienteId}`);
            return response.data;
        } catch (error) {
            console.error("Error obteniendo citas del cliente", error);
            throw error;
        }
    },

    async getCitasBarbero() {
        try {
            const response = await HTTP.get("/citas/barbero/mis-citas");
            return response.data;
        } catch (error) {
            console.error("Error obteniendo citas del barbero", error);
            throw error;
        }
    },

    async reservarCita(cita) {
        try {
            const response = await HTTP.post("/citas/reservar", cita);
            return response.data;
        } catch (error) {
            console.error("Error reservando la cita", error);
            throw error;
        }
    },

    async confirmarCita(citaId) {
        try {
            await HTTP.post(`/citas/confirmar/${citaId}`);
        } catch (error) {
            console.error("Error confirmando la cita", error);
            throw error;
        }
    },

    async cancelarCita(citaId) {
        try {
            await HTTP.delete(`/citas/cancelar/${citaId}`);
        } catch (error) {
            console.error("Error cancelando la cita", error);
            throw error;
        }
    },

    async rechazarCita(citaId) {
        try {
            await HTTP.post(`/citas/rechazar/${citaId}`);
        } catch (error) {
            console.error("Error rechazando la cita", error);
            throw error;
        }
    },
};