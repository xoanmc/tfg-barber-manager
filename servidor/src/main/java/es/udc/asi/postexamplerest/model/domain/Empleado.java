package es.udc.asi.postexamplerest.model.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EMPLEADO")
public class Empleado extends Usuario {

    @Column
    private String puesto;

    @Column
    private double salario;

    @Column
    private String contrato;

    @Column
    private String horario;

    @Column
    private String descripcion;

    @Column(nullable = true)
    private boolean despedido = false;

    public Empleado() {
        setAutoridad(AutoridadUsuario.EMPLEADO);
    }


    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isDespedido() {
        return despedido;
    }

    public void setDespedido(boolean despedido) {
        this.despedido = despedido;
    }
}
