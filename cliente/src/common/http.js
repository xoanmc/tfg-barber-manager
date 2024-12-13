import axios from "axios";
import { BACKEND_URL } from "@/constants";
import auth from "./auth"; // Importa el módulo auth para el manejo de autenticación

const HTTP = axios.create({
  baseURL: BACKEND_URL,
});

// Manejo de errores 401 y 403 (Acceso no autorizado)
const onUnauthorized = () => {
  console.error("Access denied!");
  auth.logout(); // Cierra sesión si hay un error de autenticación
};

// Respuesta exitosa
const onResponseSuccess = (response) => response;

// Manejo de errores en la respuesta (401, 403)
const onResponseFailure = (err) => {
  if (err.response) {
    const status = err.response.status;
    if (!err.config.url.includes("authenticate")) {
      if (status === 401 || status === 403) {
        onUnauthorized(); // Si hay 401/403, cerrar sesión
      }
    }
  }
  return Promise.reject(err); // Rechaza la promesa para manejar el error en los componentes
};

// Antes de cada request, añade el token solo si es necesario
const onRequest = (config) => {
  // Excluir rutas públicas del envío del token
  if (
    !config.url.includes("/password-recovery") &&
    !config.url.includes("/reset-password")
  ) {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
};



// Configura los interceptores para añadir el token solo si es necesario
HTTP.interceptors.request.use(onRequest);
HTTP.interceptors.response.use(onResponseSuccess, onResponseFailure);

export default HTTP;
