<template>
  <div>
    <h2>Reserva tu Cita</h2>

    <!-- Lista de barberos disponibles -->
    <div>
      <label for="barbero">Selecciona Barbero:</label>
      <select v-model="cita.barberoId" id="barbero">
        <option
          v-for="barbero in barberos"
          :key="barbero.id"
          :value="barbero.id"
        >
          {{ barbero.nombre }}
        </option>
      </select>
    </div>

    <!-- Fecha y Hora de la cita con botones "Aceptar" y "Cancelar" directamente en el calendario -->
    <div>
      <label for="fecha">Fecha y Hora:</label>
      <input
        ref="inputFecha"
        type="text"
        v-model="fechaHoraTemporal"
        readonly
        id="fecha"
        placeholder="Selecciona una fecha"
        @focus="abrirCalendario"
      />
    </div>

    <!-- El calendario aparece directamente bajo el campo de texto y no tapa nada -->
    <div v-if="mostrarCalendario" class="calendario-container">
      <!-- Se establece la fecha mínima como el valor actual -->
      <input
        type="datetime-local"
        v-model="fechaHoraSeleccionada"
        :min="minFecha"
      />

      <!-- Botones de Aceptar y Cancelar -->
      <div class="botones">
        <button @click="confirmarFecha">Aceptar</button>
        <button @click="cancelarFecha">Cancelar</button>
      </div>
    </div>

    <!-- Botón para reservar -->
    <div>
      <button @click="reservarCita" :disabled="!cita.fechaHora">
        Reservar
      </button>
    </div>
  </div>
</template>

<script>
import CitaRepository from "@/repositories/CitaRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";

export default {
  data() {
    return {
      barberos: [],
      cita: {
        barberoId: null,
        fechaHora: null,
        clienteId: 1, // Obtener cliente autenticado dinámicamente
      },
      fechaHoraSeleccionada: null, // Valor temporal para la fecha
      fechaHoraTemporal: "", // Muestra la fecha seleccionada en formato legible
      mostrarCalendario: false, // Controla la visualización del calendario
      minFecha: "", // Mínima fecha y hora permitida (la actual)
    };
  },
  mounted() {
    this.cargarBarberos();
    this.establecerMinFecha(); // Establecer la fecha mínima permitida
  },
  methods: {
    async cargarBarberos() {
      try {
        this.barberos = await UsuarioRepository.findAllBarberos(); // Cargar solo barberos
      } catch (error) {
        console.error("Error cargando barberos", error);
      }
    },

    // Establece la fecha mínima como la fecha y hora actual
    establecerMinFecha() {
      const ahora = new Date();
      const año = ahora.getFullYear();
      const mes = String(ahora.getMonth() + 1).padStart(2, "0");
      const dia = String(ahora.getDate()).padStart(2, "0");
      const horas = String(ahora.getHours()).padStart(2, "0");
      const minutos = String(ahora.getMinutes()).padStart(2, "0");

      // Se establece la fecha mínima como "YYYY-MM-DDTHH:MM" para incluir hora y minutos
      this.minFecha = `${año}-${mes}-${dia}T${horas}:${minutos}`;
    },

    // Abre el calendario bajo el campo de texto
    abrirCalendario() {
      this.mostrarCalendario = true;
    },

    // Confirma la fecha seleccionada y la asigna al objeto cita
    confirmarFecha() {
      if (this.fechaHoraSeleccionada) {
        this.cita.fechaHora = this.fechaHoraSeleccionada;
        this.fechaHoraTemporal = new Date(
          this.fechaHoraSeleccionada
        ).toLocaleString(); // Muestra en formato legible
      }
      this.mostrarCalendario = false; // Cierra el calendario
    },

    // Cancela la selección de la fecha
    cancelarFecha() {
      this.fechaHoraSeleccionada = null;
      this.mostrarCalendario = false;
    },

    // Reserva la cita
    async reservarCita() {
      try {
        console.log("Datos de la cita:", this.cita);
        await CitaRepository.reservarCita(this.cita);
        alert("Cita reservada con éxito");
      } catch (error) {
        console.error("Error reservando cita", error);
        alert("Hubo un error al reservar la cita");
      }
    },
  },
};
</script>

<style scoped>
/* Asegura que las secciones están organizadas verticalmente */
div {
  margin-bottom: 20px;
}
label {
  display: block;
  margin-bottom: 5px;
}
select,
input,
button {
  width: 100%;
  padding: 10px;
  font-size: 16px;
}

/* Estilos para el calendario */
.calendario-container {
  position: absolute;
  background-color: #fff;
  padding: 15px;
  border: 1px solid #ccc;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

/* Botones de Aceptar y Cancelar */
.botones {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
}

button {
  padding: 10px;
  cursor: pointer;
}

button:hover {
  background-color: #ddd;
}
</style>
