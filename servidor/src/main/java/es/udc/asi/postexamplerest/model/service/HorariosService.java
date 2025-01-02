package es.udc.asi.postexamplerest.model.service;

import es.udc.asi.postexamplerest.model.domain.Cita;
import es.udc.asi.postexamplerest.model.domain.Empleado;
import es.udc.asi.postexamplerest.model.domain.Horario;
import es.udc.asi.postexamplerest.model.domain.Usuario;
import es.udc.asi.postexamplerest.model.repository.CitaDao;
import es.udc.asi.postexamplerest.model.repository.HorarioDao;
import es.udc.asi.postexamplerest.model.repository.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class HorariosService {

    @Autowired
    private HorarioDao horarioDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private CitaDao citaDao;

    // Obtener horarios por barbero
    public List<Horario> obtenerHorariosPorBarbero(Long barberoId) {
        return horarioDao.findByBarberoId(barberoId);
    }

    // Crear o actualizar horarios
    public void actualizarHorario(Long barberoId, List<Horario> horariosActualizados) {
        Usuario usuario = usuarioDao.findById(barberoId);
        if (usuario == null || !(usuario instanceof Empleado)) {
            throw new RuntimeException("Barbero no encontrado");
        }
        Empleado barbero = (Empleado) usuario;


        // Eliminar los horarios actuales del barbero
        List<Horario> horariosExistentes = horarioDao.findByBarberoId(barberoId);
        horariosExistentes.forEach(horarioDao::delete);

        // Guardar los nuevos horarios
        horariosActualizados.forEach(horario -> {
            horario.setBarbero(barbero);
            horarioDao.create(horario);
        });
    }

    // Obtener todos los horarios (para disponibilidad general)
    public List<Horario> obtenerTodosLosHorarios() {
        return horarioDao.findAll();
    }

    public List<LocalTime> calcularSlotsDisponibles(Long barberoId, LocalDate fecha) {
        List<Horario> horarios = horarioDao.findByBarberoId(barberoId);
        String diaSemana = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).toUpperCase();

        System.out.println("Barbero ID: " + barberoId);
        System.out.println("Fecha seleccionada: " + fecha);
        System.out.println("Día de la semana: " + diaSemana);

        // Filtrar horarios por día de la semana
        List<Horario> horariosDia = horarios.stream()
                .filter(h -> h.getDiaSemana().equalsIgnoreCase(diaSemana))
                .collect(Collectors.toList());
        System.out.println("Horarios del barbero para el día seleccionado: " + horariosDia);

        // Obtener todas las citas existentes para el día y barbero
        List<Cita> citasDelDia = citaDao.findCitasByBarberoIdAndFecha(barberoId, fecha);
        System.out.println("Citas existentes para el día seleccionado: " + citasDelDia);

        List<LocalTime> slotsDisponibles = new ArrayList<>();
        for (Horario horario : horariosDia) {
            LocalTime slot = horario.getHoraInicio();
            while (slot.isBefore(horario.getHoraFin())) {
                LocalTime finalSlot = slot;
                LocalTime slotFin = slot.plusMinutes(30);

                // Verificar si el slot actual está ocupado
                boolean ocupado = citasDelDia.stream().anyMatch(cita -> {
                    LocalTime citaInicio = cita.getHora();
                    LocalTime citaFin = citaInicio.plusMinutes(30); // Duración estándar de las citas

                    // Comprobar si el slot actual se solapa con una cita existente
                    return (finalSlot.isBefore(citaFin) && slotFin.isAfter(citaInicio));
                });

                if (!ocupado) {
                    slotsDisponibles.add(slot);
                }
                slot = slot.plusMinutes(30);
            }
        }

        System.out.println("Slots disponibles calculados: " + slotsDisponibles);
        return slotsDisponibles;
    }


}
