import HTTP from "@/common/http";

export default {
  async getBarberProfile(login) {
    try {
      const response = await HTTP.get(`/barber-profile/${login}`);
      return response.data;
    } catch (error) {
      throw new Error("Error al obtener el perfil del barbero.");
    }
  },
};
