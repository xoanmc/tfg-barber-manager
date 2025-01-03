package es.udc.asi.postexamplerest.web;

import es.udc.asi.postexamplerest.model.domain.Horario;
import es.udc.asi.postexamplerest.model.service.HorariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/horarios")
public class HorariosResource {

    @Autowired
    private HorariosService horariosService;

    // Obtener horarios de un barbero específico
    @GetMapping("/barbero/{barberoId}")
    @PreAuthorize("hasAuthority('EMPLEADO') or hasAuthority('CLIENTE')")
    public List<Horario> obtenerHorariosPorBarbero(@PathVariable Long barberoId) {
        return horariosService.obtenerHorariosPorBarbero(barberoId);
    }

    // Actualizar horarios de un barbero
    @PostMapping("/barbero/{barberoId}")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public void actualizarHorario(@PathVariable Long barberoId, @RequestBody List<Horario> horarios) {
        horariosService.actualizarHorario(barberoId, horarios);
    }

    // Obtener todos los horarios
    @GetMapping("/todos")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public List<Map<String, Object>> obtenerTodosLosHorarios() {
        List<Horario> horarios = horariosService.obtenerTodosLosHorarios();

        // Convertir horarios con información del barbero
        return horarios.stream().map(h -> {
            Map<String, Object> horarioMap = new HashMap<>();
            horarioMap.put("id", h.getId());
            horarioMap.put("barbero", Map.of(
                    "id", h.getBarbero().getId(),
                    "nombre", h.getBarbero().getNombre()
            ));
            horarioMap.put("diaSemana", h.getDiaSemana());
            horarioMap.put("horaInicio", h.getHoraInicio());
            horarioMap.put("horaFin", h.getHoraFin());
            return horarioMap;
        }).collect(Collectors.toList());
    }

    @GetMapping("/barbero/{barberoId}/slots")
    @PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('EMPLEADO')")
    public List<LocalTime> obtenerSlotsDisponibles(
            @PathVariable Long barberoId,
            @RequestParam("fecha") String fechaString) {
        try {
            LocalDate fecha = LocalDate.parse(fechaString);
            return horariosService.calcularSlotsDisponibles(barberoId, fecha);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de fecha inválido");
        }
    }


}
