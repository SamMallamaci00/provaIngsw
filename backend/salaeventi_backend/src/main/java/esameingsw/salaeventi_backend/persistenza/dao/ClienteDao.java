package esameingsw.salaeventi_backend.persistenza.dao;

import esameingsw.salaeventi_backend.persistenza.model.Cliente;

import java.util.List;

public interface ClienteDao {

    public List<Cliente> findAll();

    public void saveOrUpdate(Cliente cliente);


    public Cliente findByEmail(String email);


  public Cliente findByUsername(String username);


  public Cliente findById(Long id);

    void delete(Cliente cliente);

}
