import HTTP from "@/common/http"; // Asegúrate de tener configurado un helper HTTP para manejar las peticiones

export default {
  // Obtener citas de un cliente por su ID
  async getCitasCliente(clienteId) {
    try {
      const response = await HTTP.get(`/citas/cliente/${clienteId}`);
      return response.data;
    } catch (error) {
      console.error("Error obteniendo citas del cliente", error);
      throw error;
    }
  },

  // Obtener citas de un barbero por su ID
  async getCitasBarbero(barberoId) {
    try {
      const response = await HTTP.get(`/citas/barbero/${barberoId}`);
      return response.data;
    } catch (error) {
      console.error("Error obteniendo citas del barbero", error);
      throw error;
    }
  },

  // Reservar una nueva cita
  async reservarCita(cita) {
    try {
      const response = await HTTP.post("/reservar", cita);
      return response.data;
    } catch (error) {
      console.error("Error reservando la cita", error);
      throw error;
    }
  },

  // Confirmar una cita por ID
  async confirmarCita(citaId) {
    try {
      await HTTP.post(`/citas/confirmar/${citaId}`);
    } catch (error) {
      console.error("Error confirmando la cita", error);
      throw error;
    }
  },

  // Cancelar una cita por ID
  async cancelarCita(citaId) {
    try {
      await HTTP.delete(`/citas/cancelar/${citaId}`);
    } catch (error) {
      console.error("Error cancelando la cita", error);
      throw error;
    }
  },
};
