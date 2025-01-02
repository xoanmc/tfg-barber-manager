<template>
    <div class="modal-container" v-if="visible">
      <div class="modal">
        <p>¿Deseas pagar ahora con PayPal?</p>
        <div class="button-group">
          <button @click="$emit('pagar')">Sí</button>
          <button @click="$emit('continuar-sin-pago')">No</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
    },
  };
  </script>
  
  <style scoped>
  .modal-container {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal {
    background: white;
    padding: 20px;
    border-radius: 10px;
    width: 300px;
    text-align: center;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .button-group {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
  }
  
  button {
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 1rem;
  }
  
  button:first-child {
    background-color: #007bff;
    color: white;
  }
  
  button:first-child:hover {
    background-color: #0056b3;
  }
  
  button:last-child {
    background-color: #ccc;
    color: black;
  }
  
  button:last-child:hover {
    background-color: #999;
  }
  </style>
  