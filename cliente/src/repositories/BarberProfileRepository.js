import HTTP from "@/common/http";

export default {
  async getBarberProfileByLogin(login) {
    try {
      const response = await HTTP.get(`/barber-profile/${login}`);
      return response.data;
    } catch (error) {
      console.error("Error al obtener el perfil del barbero:", error);
      throw error;
    }
  },
};
