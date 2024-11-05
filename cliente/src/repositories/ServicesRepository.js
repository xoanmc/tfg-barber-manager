import HTTP from "@/common/http";

export default {
  // Método para obtener todos los servicios disponibles
  async findAllServicios() {
    try {
      const response = await HTTP.get("/servicios");  // Solicitud GET para obtener los servicios
      return response.data;  // Devuelve la lista de servicios obtenida
    } catch (error) {
      console.error("Error obteniendo servicios:", error);  // Manejo de errores
      throw error;  // Propaga el error
    }
  },

  // Método para actualizar la lista de servicios
  async updateServicios(servicios) {
    try {
      return await HTTP.post("/servicios/update", servicios);  // Solicitud POST para actualizar servicios
    } catch (error) {
      console.error("Error actualizando servicios:", error);  // Manejo de errores
      throw error;  // Propaga el error
    }
  },

  // Método para eliminar un servicio por ID
  async deleteServicio(id) {
    try {
      return await HTTP.delete(`/servicios/${id}`);  // Solicitud DELETE para eliminar un servicio por su ID
    } catch (error) {
      console.error("Error eliminando servicio:", error);  // Manejo de errores
      throw error;  // Propaga el error
    }
  },
};
