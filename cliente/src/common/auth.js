import { getStore } from "./store";
import AccountRepository from "../repositories/AccountRepository";

export default {
  login,
  logout,
  getToken,
  isCliente,
  isEmpleado,
  isJefe,
  isLogged, // Añadir la función isLogged
  registerCliente,
  registerEmpleado,
  isAuthenticationChecked: isAuthenticationChecked(),
};

async function registerCliente(credentials) {
  await AccountRepository.registerCliente(credentials);
  return login(credentials);
}

async function registerEmpleado(credentials) {
  await AccountRepository.registerEmpleado(credentials);
}

async function login(credentials) {
  const response = await AccountRepository.authenticate(credentials);
  _saveToken(response.token);
  return _authenticate();
}

function logout() {
  _removeToken();
  getStore().state.user.login = "";
  getStore().state.user.autoridad = "";
  getStore().state.user.logged = false;
}

function isCliente() {
  return getStore().state.user.autoridad === "CLIENTE";
}

function isEmpleado() {
  return getStore().state.user.autoridad === "EMPLEADO";
}

function isJefe() {
  return getStore().state.user.autoridad === "JEFE";
}

function isLogged() {
  // Nueva función para verificar si el usuario está logueado
  return getStore().state.user.logged;
}

function getToken() {
  return localStorage.getItem("token");
}

function _saveToken(token) {
  localStorage.setItem("token", token);
}

function _removeToken() {
  localStorage.removeItem("token");
}

async function _authenticate() {
  const response = await AccountRepository.getAccount();
  const store = getStore();
  store.state.user.login = response.login;
  store.state.user.autoridad = response.autoridad;
  store.state.user.logged = true;
  return store.state.user;
}

function isAuthenticationChecked() {
  return new Promise((res) => {
    if (getToken()) {
      _authenticate()
        .catch(() => logout())
        .finally(() => res(true));
    } else {
      res(true);
    }
  });
}
