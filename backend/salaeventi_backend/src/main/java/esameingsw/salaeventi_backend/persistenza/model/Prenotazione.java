package esameingsw.salaeventi_backend.persistenza.model;


import java.sql.Date;

public class Prenotazione {

    Long id;

    Date data;

    float costo;

    SalaEvento sala;

    Cliente cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public SalaEvento getSala() {
        return sala;
    }

    public void setSala(SalaEvento sala) {
        this.sala = sala;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
