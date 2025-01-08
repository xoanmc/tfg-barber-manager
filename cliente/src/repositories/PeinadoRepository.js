import HTTP from "@/common/http";

export default {
  async obtenerPeinadosEnTendencia() {
    const response = await HTTP.get(`/peinados/tendencias`);
    return response.data;
  },
  async guardarPeinado(peinado) {
    const response = await HTTP.post(`/peinados/guardar`, peinado);
    return response.data;
  },
};
