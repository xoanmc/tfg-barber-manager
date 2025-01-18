import HTTP from "@/common/http";

export default {

  async obtenerPeinadosEnTendencia() {
    const response = await HTTP.get(`/peinados/tendencias`);
    return response.data;
  },

  async obtenerPeinadosPorEstructura(estructuraFacial) {
    try {
      const response = await HTTP.get(`/peinados/estructuras/${estructuraFacial}`);
      return response.data;
    } catch (error) {
      console.error("Error al obtener peinados por estructura facial:", error);
      throw error;
    }
  },

  async guardarPeinado(peinado) {
    return await HTTP.post(`/peinados/guardar`, peinado, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  async obtenerPeinados() {
    const response = await HTTP.get(`/peinados`);
    return response.data;
  },

  async toggleTendencia(peinadoId) {
    await HTTP.put(`/peinados/${peinadoId}/toggle-tendencia`);
  },

  async obtenerUrlImagenPeinado(fileName) {
    return `/peinados/images/${fileName}`;
  },
};
