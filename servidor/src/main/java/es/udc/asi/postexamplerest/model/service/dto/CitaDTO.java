package es.udc.asi.postexamplerest.model.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class CitaDTO {
    private Long barberoId;
    private Long servicioId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fechaHora;

    public CitaDTO() {}

    public CitaDTO(Long barberoId, Long servicioId, LocalDateTime fechaHora) {
        this.barberoId = barberoId;
        this.servicioId = servicioId;
        this.fechaHora = fechaHora;
    }

    public Long getBarberoId() {
        return barberoId;
    }

    public void setBarberoId(Long barberoId) {
        this.barberoId = barberoId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}

