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
};
