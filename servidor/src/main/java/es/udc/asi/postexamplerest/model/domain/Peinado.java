package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.*;

@Entity
public class Peinado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(length = 1000)
    private String descripcionTendencias;  // Descripción para la pantalla de tendencias

    @Column(length = 1000)
    private String descripcionRecomendador;  // Descripción para el recomendador de peinados

    private boolean tendencia;

    private String estructuraFacial;  // Tipo(s) de estructura facial recomendada

    private String urlImagen;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcionTendencias() { return descripcionTendencias; }
    public void setDescripcionTendencias(String descripcionTendencias) { this.descripcionTendencias = descripcionTendencias; }

    public String getDescripcionRecomendador() { return descripcionRecomendador; }
    public void setDescripcionRecomendador(String descripcionRecomendador) { this.descripcionRecomendador = descripcionRecomendador; }

    public boolean isTendencia() { return tendencia; }
    public void setTendencia(boolean tendencia) { this.tendencia = tendencia; }

    public String getEstructuraFacial() { return estructuraFacial; }
    public void setEstructuraFacial(String estructuraFacial) { this.estructuraFacial = estructuraFacial; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}
