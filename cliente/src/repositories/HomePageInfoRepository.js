import HTTP from "@/common/http";

export default {
  // Obtener la información de la pantalla de inicio
  async getHomePageInfo() {
    const response = await HTTP.get("/home"); // /api/home
    return response.data;
  },

  // Actualizar la información de la pantalla de inicio
  async updateHomePageInfo(info) {
    return await HTTP.post("/update", info); // /api/update
  },
};
