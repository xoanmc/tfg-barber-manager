import HTTP from "@/common/http";

export default {
  async solicitarRecuperacion(email) {
    return await HTTP.post("/password-recovery", { email });
  },

  async restablecerContraseña(token, password) {
    return await HTTP.post("/reset-password", { token, password });
  },
};
