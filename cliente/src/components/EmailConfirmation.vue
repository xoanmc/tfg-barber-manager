<template>
    <div class="container py-5 d-flex justify-content-center align-items-center vh-100">
      <div class="card shadow-lg text-center" style="max-width: 400px; width: 100%; border-radius: 15px;">
        <div class="card-body p-4">
          <h2 class="text-primary fw-bold mb-3">¡Registro Exitoso!</h2>
          <p>Hemos enviado un correo de confirmación a tu dirección. Por favor, revisa tu bandeja de entrada para activar tu cuenta.</p>
          <div class="d-grid mt-4">
            <router-link to="/login" class="btn btn-primary btn-lg rounded-pill">
              Ir a Inicio de Sesión
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "EmailConfirmation",
  };
  </script>
  
  <style scoped>
  .card {
    border: none;
    border-radius: 15px;
    background-color: #f8f9fa;
  }
  
  .card-body {
    padding: 2rem;
  }
  
  .text-primary {
    font-weight: bold;
    font-size: 1.8rem;
  }
  
  .btn-primary {
    background-color: #007bff;
    border-color: #007bff;
  }
  
  .btn-primary:hover {
    background-color: #0056b3;
    border-color: #004085;
  }

  .card {
  margin-top: 20px;
}

</style>
  