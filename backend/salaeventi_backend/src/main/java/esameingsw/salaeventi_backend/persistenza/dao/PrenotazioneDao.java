package esameingsw.salaeventi_backend.persistenza.dao;

import esameingsw.salaeventi_backend.persistenza.model.Cliente;
import esameingsw.salaeventi_backend.persistenza.model.Prenotazione;
import esameingsw.salaeventi_backend.persistenza.model.SalaEvento;

import java.sql.Date;
import java.util.List;

public interface PrenotazioneDao {

    public List<Prenotazione> findAll();

    public Prenotazione findById(Long id);

    public List<Prenotazione> findByCliente(Cliente cliente);

    public void saveOrUpdate(Prenotazione prenotazione);



    public void delete(Prenotazione prenotazione);

    public  List<SalaEvento> SaleDispByDate(Date date);

    public boolean DispSalaByDate (SalaEvento salaEvento, Date data);


}
