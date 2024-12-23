import HTTP from "../common/http";

export default {
    async authenticate(credentials) {
        return (await HTTP.post(`authenticate`, credentials)).data;
    },

    async getAccount() {
        return (await HTTP.get(`account`)).data;
    },

    async registerCliente(user) {
        return (await HTTP.post(`register`, user)).data;
    },

    async registerEmpleado(user) {
        return (await HTTP.post(`contratar`, user)).data;
    },

    async updateAccount(profile) {
        return (await HTTP.put("/users/me", profile)).data;
    },
};