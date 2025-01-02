import HTTP from "@/common/http";

export default {
  async obtenerHorariosPorBarbero(barberoId) {
    const response = await HTTP.get(`/horarios/barbero/${barberoId}`);
    return response.data;
  },

  async actualizarHorario(barberoId, horarios) {
    await HTTP.post(`/horarios/barbero/${barberoId}`, horarios);
  },

  async obtenerTodosLosHorarios() {
    const response = await HTTP.get("/horarios/todos");
    return response.data;
  },

  async obtenerSlotsDisponibles(barberoId, fecha) {
    console.log("Parámetros enviados al backend: ", { barberoId, fecha });
    const response = await HTTP.get(`/horarios/barbero/${barberoId}/slots`, {
      params: { fecha },
    });
    console.log("Respuesta del backend: ", response.data);
    return response.data;
  },
};
