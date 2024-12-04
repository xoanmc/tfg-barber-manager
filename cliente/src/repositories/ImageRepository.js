import HTTP from "@/common/http";

export default {
    async uploadProfileImage(userId, file) {
        if (!file || file.size === 0) {
            throw new Error("El archivo está vacío o no es válido.");
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await HTTP.post(`/users/upload/${userId}`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            return response.data.imageUrl;
        } catch (error) {
            console.error("Error al subir la imagen de perfil:", error);
            throw error;
        }
    },

    async getProfileImage(userId) {
        try {
            const response = await HTTP.get(`/users/profile-image/${userId}`);
            return response.data.imageUrl;
        } catch (error) {
            console.error("Error al obtener la imagen de perfil:", error);
            throw error;
        }
    },

    async deleteProfileImage(userId) {
        try {
            const response = await HTTP.delete(`/users/profile-image/${userId}`);
            return response.data;
        } catch (error) {
            console.error("Error al eliminar la imagen de perfil:", error);
            throw error;
        }
    },
};