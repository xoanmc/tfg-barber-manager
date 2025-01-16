package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Peinado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(length = 1000)
    private String descripcionTendencias;  // descripción para la pantalla de tendencias

    @Column(length = 1000)
    private String descripcionRecomendador;  // descripción para el recomendador de peinados

    private boolean tendencia;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = EstructuraFacial.class)
    @Enumerated(EnumType.STRING) // Almacena los valores como texto en la base de datos
    @CollectionTable(name = "peinado_estructura_facial", joinColumns = @JoinColumn(name = "peinado_id"))
    @Column(name = "estructura_facial")
    private List<EstructuraFacial> estructurasFaciales;

    private String urlImagen;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcionTendencias() {
        return descripcionTendencias;
    }

    public void setDescripcionTendencias(String descripcionTendencias) {
        this.descripcionTendencias = descripcionTendencias;
    }

    public String getDescripcionRecomendador() {
        return descripcionRecomendador;
    }

    public void setDescripcionRecomendador(String descripcionRecomendador) {
        this.descripcionRecomendador = descripcionRecomendador;
    }

    public boolean isTendencia() {
        return tendencia;
    }

    public void setTendencia(boolean tendencia) {
        this.tendencia = tendencia;
    }

    public List<EstructuraFacial> getEstructurasFaciales() {
        return estructurasFaciales;
    }

    public void setEstructurasFaciales(List<EstructuraFacial> estructurasFaciales) {
        this.estructurasFaciales = estructurasFaciales;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
