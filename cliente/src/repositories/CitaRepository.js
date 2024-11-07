import HTTP from "@/common/http"; // Asegúrate de tener configurado un helper HTTP para manejar las peticiones

export default {
  // Obtener citas del cliente autenticado
  async getMisCitas() {
    try {
      const response = await HTTP.get("/citas/cliente/mis-citas");
      return response.data;
    } catch (error) {
      console.error("Error obteniendo las citas del cliente", error);
      throw error;
    }
  },
  

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

  // Obtener citas de un barbero
  async getCitasBarbero() {
    try {
      const response = await HTTP.get("/citas/barbero/mis-citas");
      return response.data;
    } catch (error) {
      console.error("Error obteniendo citas del barbero", error);
      throw error;
    }
  },
  

  // Reservar una nueva cita
  async reservarCita(cita) {
    try {
      const response = await HTTP.post("/citas/reservar", cita);
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

  async rechazarCita(citaId) {
    try {
      await HTTP.post(`/citas/rechazar/${citaId}`);
    } catch (error) {
      console.error("Error rechazando la cita", error);
      throw error;
    }
  },

  async modificarCita(citaId, citaModificada) {
    try {
      const response = await HTTP.put(`/citas/modificar/${citaId}`, citaModificada);
      return response.data;
    } catch (error) {
      console.error("Error modificando la cita", error);
      throw error;
    }
  },
};
