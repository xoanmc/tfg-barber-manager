import HTTP from "@/common/http";

export default {
    // Subir imagen de perfil
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
            return response.data.imageUrl; // Devuelve la URL de la imagen subida
        } catch (error) {
            console.error("Error al subir la imagen de perfil:", error);
            throw error;
        }
    },

    // Obtener imagen de perfil
    async getProfileImage(userId) {
        try {
            const response = await HTTP.get(`/users/profile-image/${userId}`);
            return response.data.imageUrl; // Devuelve la URL de la imagen
        } catch (error) {
            console.error("Error al obtener la imagen de perfil:", error);
            throw error;
        }
    },

    // Eliminar imagen de perfil (si se implementa en el backend)
    async deleteProfileImage(userId) {
        try {
            const response = await HTTP.delete(`/users/profile-image/${userId}`);
            return response.data; // Devuelve una confirmación de eliminación
        } catch (error) {
            console.error("Error al eliminar la imagen de perfil:", error);
            throw error;
        }
    },
};