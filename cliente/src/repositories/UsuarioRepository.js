import HTTP from "@/common/http";

const resource = "users";

export default {
    async findAllClientes() {
        const response = await HTTP.get(`${resource}/clientes`);
        return response.data;
    },

    async findAllEmpleados() {
        const response = await HTTP.get(`${resource}/empleados`);
        return response.data;
    },

    async findAllBarberos() {
        const response = await HTTP.get(`${resource}/barberos`);
        return response.data;
    },

    async findOne(id) {
        const user = await HTTP.get(`${resource}/${id}`);
        return user.data;
    },

    async delete(id) {
        return await HTTP.delete(`${resource}/${id}`);
    },

    async despedir(id) {
        return await HTTP.patch(`${resource}/${id}/despedir`);
    },

    async bloquearCliente(id) {
        return await HTTP.patch(`${resource}/${id}/bloquear`);
    },

    async activarCliente(id) {
        return await HTTP.patch(`${resource}/${id}/activar`);
    },

};