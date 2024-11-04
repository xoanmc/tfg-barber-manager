import HTTP from "@/common/http"; // Asegúrate de tener el helper HTTP para manejar peticiones

export default {
  async getContent() {
    // GET para obtener el contenido actual
    return (await HTTP.get("/about/1")).data; // Suponemos que solo hay un "Acerca de"
  },

  async updateContent(payload) {
    // PUT para actualizar contenido
    return await HTTP.post("/about/update", payload); // POST o PUT con solo la descripción
  },
};
