import { createRouter, createWebHistory } from "vue-router";

import ErrorNotFoundView from "../views/ErrorNotFoundView.vue";
import AboutView from "../views/AboutView.vue";
import BarberProfileView from "@/views/BarberProfileView.vue";
import HomeView from "../views/HomeView.vue";
import ServicesView from "@/views/ServicesView.vue";
import PromocionView from "@/views/PromocionView.vue";
import TrendingView from "@/views/TrendingView.vue";
import RecomendadorView from "@/views/RecomendadorView.vue";
import PeinadosRecomendadosView from "@/views/PeinadosRecomendadosView.vue";
import NewTrendingHairstyle from "@/components/NewTrendingHairstyle.vue";
import ReserveForm from "@/components/Cita/ReserveForm.vue";
import PaymentScreen from "@/components/Cita/PaymentScreen.vue";

import LoginForm from "../components/LoginForm.vue";
import ForgotPassword from "@/components/ForgotPassword.vue";
import ResetPassword from "@/components/ResetPassword.vue";
import ClienteRegisterForm from "@/components/Cliente/ClienteRegisterForm.vue";
import ClienteList from "@/components/Cliente/ClienteList.vue";
import EmailConfirmation from "@/components/EmailConfirmation.vue";
import ConfirmRegistration from "@/components/ConfirmRegistration.vue";

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
    component: EmailConfirmation,
    meta: { public: true },
},
{
    path: "/confirmRegistration",
    name: "ConfirmRegistration",
    component: ConfirmRegistration,
    meta: { public: true },
},
{
    path: "/payment",
    name: "PaymentScreen",
    component: PaymentScreen,
    meta: { authority: "CLIENTE" },
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
    path: "/barber-profile/:login",
    name: "BarberProfileView",
    component: BarberProfileView,
    meta: { public: true },
},
{
    path: "/services",
    name: "services",
    component: ServicesView,
    meta: { public: true },
},
{
    path: "/trending",
    name: "trending",
    component: TrendingView,
    meta: { public: true },
},
{
    path: "/trending/nuevo-peinado",
    name: "NewTrendingHairstyle",
    component: NewTrendingHairstyle,
    meta: { authority: "JEFE" },
},
{
    path: "/trending/recomendador/:estructuraFacial",
    name: "PeinadosRecomendadosView",
    component: PeinadosRecomendadosView,
    meta: { public: true },
},
{
    path: "/trending/recomendador",
    name: "RecomendadorView",
    component: RecomendadorView,
    meta: { public: true },
},
{
    path: "/reserve",
    name: "reserve",
    component: ReserveForm,
    meta: { public: true },
},
{
    path: "/promociones",
    name: "PromocionView",
    component: PromocionView,
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

    auth.isAuthenticationChecked.finally(() => {
        const requiresAuth = !to.meta.public;

        const requiredAuthority = to.meta.authority;
        const userIsLogged = getStore().state.user.logged;
        const loggedUserAuthority = getStore().state.user.autoridad;
        console.log("Autoridad del usuario logueado:", loggedUserAuthority);

        if (requiresAuth) {
            if (userIsLogged) {
                if (requiredAuthority && requiredAuthority != loggedUserAuthority) {
                    alert(
                        "Acceso prohibido para el usuario actual; intenta autenticarte de nuevo"
                    );
                    auth.logout();
                    next("/login");
                } else {
                    next();
                }
            } else {
                alert("Esta página requiere autenticación");
                next("/login");
            }
        } else {
            if (userIsLogged && to.meta.isLoginPage) {
                next({ name: "Home", replace: true });
            } else {
                next();
            }
        }
    });
});

export default router;