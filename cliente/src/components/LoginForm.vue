<template>
  <div class="form-container">
    <form class="form" @submit.prevent="autenticarme">
      <span class="input-span">
        <label for="username" class="label">Login</label>
        <input type="text" name="username" id="username" v-model="auxLogin" />
      </span>
      <span class="input-span">
        <label for="password" class="label">Password</label>
        <input
          type="password"
          name="password"
          id="password"
          v-model="auxPass"
        />
      </span>
      <span class="span"><a href="#">¿Olvidaste tu contraseña?</a></span>
      <input class="submit" type="submit" value="Log in" />
      <span class="span"
        >¿Todavía no eres miembro?
        <router-link to="/register">Regístrate</router-link></span
      >
    </form>
  </div>
</template>

<script>
import auth from "../common/auth.js";
export default {
  data() {
    return {
      auxLogin: "",
      auxPass: "",
    };
  },
  methods: {
    async autenticarme() {
      try {
        await auth.login({
          login: this.auxLogin,
          password: this.auxPass,
        });
        this.$router.push("/");
      } catch (e) {
        console.error(e);
        if (e.response?.data?.message) {
          alert(e.response.data.message);
        } else {
          alert(e.message);
        }
      }
    },
  },
};
</script>

<style scoped>
.form-container {
  display: flex;
  justify-content: center; /* Centra horizontalmente */
  align-items: center; /* Centra verticalmente */
  height: 100vh; /* Altura completa de la ventana del navegador */
}
.form {
  --bg-light: #efefef;
  --bg-dark: #4d4c4c;
  --clr: #3dad6c;
  --clr-alpha: #9c9c9c60;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  width: 100%;
  max-width: 300px;
}
.form .input-span {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.form input[type="text"],
.form input[type="password"] {
  border-radius: 0.5rem;
  padding: 1rem 0.75rem;
  width: 100%;
  border: none;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background-color: var(--clr-alpha);
  outline: 2px solid var(--bg-dark);
}
.form input[type="text"]:focus,
.form input[type="password"]:focus {
  outline: 2px solid var(--clr);
}
.label {
  align-self: flex-start;
  color: var(--clr); /* Color aplicado a las etiquetas */
  font-weight: 600;
}
.form .submit {
  padding: 1rem 0.75rem;
  width: 100%;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  border-radius: 3rem;
  background-color: var(--bg-dark); /* Color de fondo del botón */
  color: var(--bg-light); /* Color del texto del botón */
  border: none;
  cursor: pointer;
  transition: all 300ms;
  font-weight: 600;
  font-size: 0.9rem;
}
.form .submit:hover {
  background-color: var(--clr); /* Cambia el fondo al color verde en hover */
  color: var(--bg-dark); /* Cambia el texto al color oscuro en hover */
}
.span {
  text-decoration: none;
  color: var(--bg-dark); /* Color aplicado a los textos en .span */
}
.span a {
  color: var(--clr); /* Color aplicado a los enlaces */
}
</style>
