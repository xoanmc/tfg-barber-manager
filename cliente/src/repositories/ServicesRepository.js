import HTTP from "@/common/http";

export default {
  async getServicios() {
    return (await HTTP.get("/servicios")).data; // Cargar todos los servicios
  },

  async updateServicios(servicios) {
    // Actualizar la lista completa de servicios
    return await HTTP.post("/servicios/update", servicios);
  },

  async deleteServicio(id) {
    // Eliminar un servicio por ID
    return await HTTP.delete(`/servicios/${id}`);
  },
};
