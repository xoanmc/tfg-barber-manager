import HTTP from "@/common/http";

export default {
  async getContent() {
    try {
      const response = await HTTP.get("/about");
      return response.data;
    } catch (error) {
      console.error("Error al obtener la información de la página 'About':", error);
      throw error;
    }
  },

  async updateContent(payload) {
    const token = localStorage.getItem("token");
    try {
      return await HTTP.post("/about/update", payload, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
    } catch (error) {
      console.error("Error al actualizar la información:", error);
      throw error;
    }
  },

  async getProfessionals() {
    try {
      const response = await HTTP.get("/about/barberos"); // Llamada al nuevo endpoint
      return response.data;
    } catch (error) {
      console.error("Error al obtener los profesionales:", error);
      throw error;
    }
  },
};
