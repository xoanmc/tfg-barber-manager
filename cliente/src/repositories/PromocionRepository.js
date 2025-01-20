import HTTP from "@/common/http";

export default {
    async getPromociones(servicioId = null, fecha = null) {
        try {
          const queryParams = [];
          if (servicioId) queryParams.push(`servicioId=${servicioId}`);
          if (fecha) queryParams.push(`fecha=${fecha}`);
          const queryString = queryParams.length ? `?${queryParams.join("&")}` : "";
          const response = await HTTP.get(`/promociones${queryString}`);
          return response.data;
        } catch (error) {
          console.error("Error obteniendo promociones:", error);
          throw error;
        }
      },

    async addPromocion(promocion) {
        try {
            const response = await HTTP.post(`/promociones`, {
                servicioId: promocion.servicioId, // Usamos servicioId como lo define el DTO
                porcentajeDescuento: promocion.porcentajeDescuento,
                fechaInicio: promocion.fechaInicio,
                fechaFin: promocion.fechaFin,
                activo: promocion.activo || true,
            });
            return response.data;
        } catch (error) {
            console.error("Error añadiendo promoción:", error);
            throw error;
        }
    },

    async updatePromocion(promocion) {
        try {
            const response = await HTTP.put(`/promociones/${promocion.id}`, {
                id: promocion.id,
                servicioId: promocion.servicioId,
                porcentajeDescuento: promocion.porcentajeDescuento,
                fechaInicio: promocion.fechaInicio,
                fechaFin: promocion.fechaFin,
                activo: promocion.activo,
            });
            return response.data;
        } catch (error) {
            console.error("Error actualizando promoción:", error);
            throw error;
        }
    },

    async deletePromocion(id) {
        try {
            await HTTP.delete(`/promociones/${id}`);
        } catch (error) {
            console.error("Error eliminando promoción:", error);
            throw error;
        }
    },
};
