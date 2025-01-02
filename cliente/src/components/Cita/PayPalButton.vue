<template>
    <div id="paypal-button-container"></div>
  </template>
  
  <script>
  export default {
    props: {
      amount: {
        type: Number,
        required: true,
      },
    },
    mounted() {
      if (!window.paypal) {
        console.error("PayPal SDK no está cargado.");
        return;
      }
  
      // Renderizar el botón de PayPal
      window.paypal.Buttons({
        createOrder: (data, actions) => {
          // Crear el pedido de PayPal con los detalles
          return actions.order.create({
            purchase_units: [
              {
                description: "Pago de Reserva",
                amount: {
                  value: this.amount.toFixed(2),
                  currency_code: "EUR",
                },
              },
            ],
          });
        },
        onApprove: async (data, actions) => {
          // Capturar el pago
          const order = await actions.order.capture();
          console.log("Pago realizado con éxito:", order);
          this.$emit("pago-completado", order);
        },
        onError: (err) => {
          console.error("Error con PayPal:", err);
          this.$emit("pago-error", err);
        },
      }).render("#paypal-button-container");
    },
  };
  </script>
  
  <style scoped>
  #paypal-button-container {
    margin-top: 20px;
  }
  </style>
  