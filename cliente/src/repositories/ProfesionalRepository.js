import HTTP from "@/common/http";

export default {
  async getAll() {
    try {
      const response = await HTTP.get("/profesionales");
      return response.data;
    } catch (error) {
      console.error("Error al obtener los profesionales:", error);
      throw error;
    }
  },
};
