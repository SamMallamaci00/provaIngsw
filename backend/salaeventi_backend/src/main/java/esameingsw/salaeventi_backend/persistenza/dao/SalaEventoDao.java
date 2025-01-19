package esameingsw.salaeventi_backend.persistenza.dao;



import esameingsw.salaeventi_backend.persistenza.model.SalaEvento;

import java.util.List;

public interface SalaEventoDao {

    public List<SalaEvento> findAll();

    public SalaEvento findById(Long id);

    public void saveOrUpdate(SalaEvento salaEvento);

    public void delete(SalaEvento salaEvento);





}
