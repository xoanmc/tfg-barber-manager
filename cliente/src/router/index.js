import { createRouter, createWebHistory } from "vue-router";

import ErrorNotFoundView from "../views/ErrorNotFoundView.vue";
import AboutView from "../views/AboutView.vue";
import HomeView from "../views/HomeView.vue";
import ServicesView from "@/views/ServicesView.vue";
import GalleryView from "@/views/GalleryView.vue";
import TrendingView from "@/views/TrendingView.vue";
import ReserveForm from "@/components/Cita/ReserveForm.vue";

import LoginForm from "../components/LoginForm.vue";
import ForgotPassword from "@/components/ForgotPassword.vue";
import ResetPassword from "@/components/ResetPassword.vue";
import ClienteRegisterForm from "@/components/Cliente/ClienteRegisterForm.vue";
import ClienteList from "@/components/Cliente/ClienteList.vue";
import EmailConfirmation from "@/components/EmailConfirmation.vue";

import PerfilCliente from "@/components/Cliente/ClientePerfil.vue";

import auth from "@/common/auth";
import { getStore } from "@/common/store";
import JefePerfil from "@/components/Jefe/JefePerfil.vue";
import EditProfile from "@/components/EditProfile.vue";

import EmpleadoPerfil from "@/components/Empleado/EmpleadoPerfil.vue";
import EmpleadoRegisterForm from "@/components/Empleado/EmpleadoRegisterForm.vue";
import EmpleadoList from "@/components/Empleado/EmpleadoList.vue";
import ClienteCitas from "@/components/Cliente/ClienteCitas.vue";
import EmpleadoCitas from "@/components/Empleado/EmpleadoCitas.vue";

const routes = [{
        path: "/",
        redirect: "/home",
    },
    {
        path: "/login",
        name: "Login",
        component: LoginForm,
        meta: { public: true, isLoginPage: true },
    },
    {
        path: "/forgotPassword",
        name: "ForgotPassword",
        component: ForgotPassword,
        meta: { public: true },
    },
    {
        path: "/resetPassword",
        name: "ResetPassword",
        component: ResetPassword,
        meta: { public: true },
    },
    //REGISTER
    {
        path: "/register",
        name: "ClienteRegisterForm",
        component: ClienteRegisterForm,
        meta: { public: true },
    },
    {
        path: "/empleados/new",
        name: "EmpleadoCreate",
        component: EmpleadoRegisterForm,
        meta: { authority: "JEFE" },
    },
    {
        path: "/emailConfirmation",
        name: "EmailConfirmation",
        component:EmailConfirmation,
        meta: { public: true },
    },
    
    //PERFILES
    {
        path: "/perfilCliente",
        name: "PerfilCliente",
        component: PerfilCliente,
        meta: { authority: "CLIENTE" },
    },
    {
        path: "/misCitas",
        name: "MisCitas",
        component: ClienteCitas,
        meta: { authority: "CLIENTE" },
    },
    {
        path: "/empleadoCitas",
        name: "EmpleadoCitas",
        component: EmpleadoCitas,
        meta: { authority: "EMPLEADO" },
    },
    {
        path: "/perfilEmpleado",
        name: "PerfilEmpleado",
        component: EmpleadoPerfil,
        meta: { authority: "EMPLEADO" },
    },
    {
        path: "/perfilJefe",
        name: "PerfilJefe",
        component: JefePerfil,
        meta: { authority: "JEFE" },
    },
    {
        path: "/editProfile",
        name: "EditProfile",
        component: EditProfile,
        meta: { requiresAuth: true },
    },
    {
        path: "/home",
        name: "Home",
        component: HomeView,
        meta: { public: true },
    },
    {
        path: "/about",
        name: "about",
        component: AboutView,
        meta: { public: true },
    },
    {
        path: "/services",
        name: "services",
        component: ServicesView,
        meta: { public: true },
    },
    {
        path: "/gallery",
        name: "gallery",
        component: GalleryView,
        meta: { public: true },
    },
    {
        path: "/trending",
        name: "trending",
        component: TrendingView,
        meta: { public: true },
    },
    {
        path: "/reserve",
        name: "reserve",
        component: ReserveForm,
        meta: { public: true },
    },
    //EMPLEADO
    {
        path: "/users/empleados",
        name: "EmpleadoList",
        component: EmpleadoList,
        meta: { authority: "JEFE" },
    },
    //CLIENTE
    {
        path: "/users/clientes",
        name: "ClienteList",
        component: ClienteList,
        meta: { authority: "JEFE" },
    },
    // /posts/new debe colocarse antes de /posts/:id porque si no vue-router
    // interpreta "new" como si fuera el id.
    //
    // Una forma de evitar este problema es usar una expresión regular para
    // limitar los valores que son interpretados. Por ejemplo, usando el path
    // /posts/:id(\\d+), vue-router espera que :id sea numérico.
    {
        path: "/:catchAll(.*)*",
        component: ErrorNotFoundView,
        meta: { public: true },
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

router.beforeEach((to, from, next) => {
    // Lo primero que hacemos antes de cargar ninguna ruta es comprobar si
    // el usuario está autenticado (revisando el token)
    auth.isAuthenticationChecked.finally(() => {
        // por defecto, el usuario debe estar autenticado para acceder a las rutas
        const requiresAuth = !to.meta.public;

        const requiredAuthority = to.meta.authority;
        const userIsLogged = getStore().state.user.logged;
        const loggedUserAuthority = getStore().state.user.autoridad;
        console.log("Autoridad del usuario logueado:", loggedUserAuthority);

        if (requiresAuth) {
            // página privada
            if (userIsLogged) {
                if (requiredAuthority && requiredAuthority != loggedUserAuthority) {
                    // usuario logueado pero sin permisos suficientes, le redirigimos a la página de login
                    alert(
                        "Acceso prohibido para el usuario actual; intenta autenticarte de nuevo"
                    );
                    auth.logout();
                    next("/login");
                } else {
                    // usuario logueado y con permisos adecuados
                    next();
                }
            } else {
                // usuario no está logueado, no puede acceder a la página
                alert("Esta página requiere autenticación");
                next("/login");
            }
        } else {
            // página pública
            if (userIsLogged && to.meta.isLoginPage) {
                // si estamos logueados no hace falta volver a mostrar el login
                next({ name: "Home", replace: true });
            } else {
                next();
            }
        }
    });
});

export default router;