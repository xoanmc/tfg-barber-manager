<template>
    <div class="confirmation-container">
      <div class="card shadow-lg p-4" style="border-radius: 15px; background-color: #f9f9f9;">
        <div class="card-body text-center">
          <h1 v-if="message" class="success">{{ message }}</h1>
          <p v-if="error" class="error">{{ error }}</p>
          <router-link to="/home" class="btn btn-primary mt-4">Volver a Inicio</router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "ConfirmRegistration",
    data() {
      return {
        message: "",
        error: "",
      };
    },
    created() {
      const query = this.$route.query;
  
      if (query.success === "true") {
        // Mensaje de éxito
        this.message = "Registro confirmado exitosamente. Ahora puedes iniciar sesión.";
      } else {
        // Mensaje de error basado en el código de error
        this.error = this.getErrorMessage(query.error);
      }
    },
    methods: {
      getErrorMessage(errorCode) {
        switch (errorCode) {
          case "invalid_token":
            return "El enlace de confirmación es inválido o ha expirado.";
          case "user_not_found":
            return "Usuario no encontrado. Por favor, verifica tu token.";
          default:
            return "Ha ocurrido un error inesperado. Por favor, inténtalo más tarde.";
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .confirmation-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 80vh;
    background-color: #f4f4f4;
    padding: 20px;
  }
  
  .card {
    max-width: 600px;
    width: 100%;
    border: none;
    border-radius: 15px;
  }
  
  .card-body {
    padding: 2rem;
  }
  
  .success {
    color: #28a745;
    font-size: 2rem;
    font-weight: bold;
    margin-bottom: 1.5rem;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
  }
  
  .error {
    color: #dc3545;
    font-size: 1.2rem;
    margin-bottom: 1.5rem;
  }
  
  .btn-primary {
    background-color: #007bff;
    border-color: #007bff;
    padding: 10px 20px;
    font-size: 1rem;
    font-weight: bold;
    border-radius: 30px;
    text-transform: uppercase;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .btn-primary:hover {
    background-color: #0056b3;
    border-color: #004085;
  }
  </style>
  