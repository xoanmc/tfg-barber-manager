import HTTP from "@/common/http";

export default {
  async getReviews(barberoLogin) {
    try {
      const response = await HTTP.get(`/reviews/${barberoLogin}`);
      return response.data;
    } catch (error) {
      console.error("Error al obtener las reseñas:", error);
      throw new Error("Error al obtener las reseñas.");
    }
  },

  async addReview(reviewData) {
    try {
      const response = await HTTP.post("/reviews/add", reviewData);
      return response.data;
    } catch (error) {
      console.error("Error al crear la reseña:", error);
      throw new Error("Error al crear la reseña.");
    }
  },

  async deleteReview(reviewId) {
    try {
      await HTTP.delete(`/reviews/delete/${reviewId}`);
      return "Reseña eliminada con éxito.";
    } catch (error) {
      console.error("Error al eliminar la reseña:", error);
      throw new Error("Error al eliminar la reseña.");
    }
  }
};
