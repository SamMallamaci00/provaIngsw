package esameingsw.salaeventi_backend.persistenza.dao.postgress;

import esameingsw.salaeventi_backend.persistenza.IdClienteBroker;
import esameingsw.salaeventi_backend.persistenza.dao.ClienteDao;
import esameingsw.salaeventi_backend.persistenza.model.Cliente;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoPostgress implements ClienteDao {

    Connection conn;

    public ClienteDaoPostgress(Connection conn) {
        this.conn = conn;
    }


    @Override
    public List<Cliente> findAll() {

        List<Cliente> clienti = new ArrayList<Cliente>();

        String query = "select * from cliente";


        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);


            while (rs.next()){
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("id"));
                cliente.setUsername((rs.getString("username")));
                cliente.setEmail((rs.getString("email")));
                cliente.setPassword((rs.getString("password")));
                cliente.setPermessi((rs.getString("permessi")));



                clienti.add(cliente);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return clienti;

    }

    @Override
    public void saveOrUpdate(Cliente cliente) {
        if (cliente.getId() == null){


            String insertStr = "INSERT INTO cliente VAlUES (?, ?, ?, ?, ?)";

            try {
                PreparedStatement st = conn.prepareStatement(insertStr);

                Long  newId = IdClienteBroker.getId(conn);
                cliente.setId(newId);

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(cliente.getPassword());


                st.setLong(1, newId);
                st.setString(2, cliente.getUsername());
                st.setString(3, encodedPassword);
                st.setString(4, cliente.getEmail());
                st.setString(5, cliente.getPermessi());
                st.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        else {
            String updateStr = "UPDATE cliente set username = ?, password = ?, email = ?, permessi = ? where id = ?";

            PreparedStatement st;

            try {
                st = conn.prepareStatement(updateStr);

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(cliente.getPassword());


                st.setString(1, cliente.getUsername());
                st.setString(2, encodedPassword);
                st.setString(3, cliente.getEmail());
                st.setString(4, cliente.getPermessi());

                st.setLong(5, cliente.getId());


                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

  @Override
  public Cliente findByUsername(String username) {
    Cliente cliente = null;
    String query = "select * from cliente where username = ?";

    try {
      PreparedStatement st = conn.prepareStatement(query);
      st.setString(1,username);
      ResultSet rs = st.executeQuery();

      if (rs.next()){
        cliente = new Cliente();


        cliente.setId(rs.getLong("id"));


        cliente.setEmail(rs.getString("email"));
        //cliente.setCognome(rs.getString("cognome") );
        //cliente.setNome(rs.getString("nome"));
        cliente.setPassword(rs.getString("password"));
        cliente.setUsername(rs.getString("username"));
        cliente.setPermessi(rs.getString("permessi"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return cliente;
  }

    @Override
    public Cliente findByEmail(String email) {
        Cliente cliente = null;
        String query = "select * from cliente where email = ?";

        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1,email);
            ResultSet rs = st.executeQuery();

            if (rs.next()){
                cliente = new Cliente();


                cliente.setId(rs.getLong("id"));


                cliente.setEmail(rs.getString("email"));
                //cliente.setCognome(rs.getString("cognome") );
                //cliente.setNome(rs.getString("nome"));
                cliente.setPassword(rs.getString("password"));
                cliente.setUsername(rs.getString("username"));
                cliente.setPermessi(rs.getString("permessi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    @Override
    public Cliente findById(Long id) {
        Cliente cliente = null;
        String query = "select * from cliente where id = ?";

        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setLong(1,id);
            ResultSet rs = st.executeQuery();

            if (rs.next()){
                cliente = new Cliente();


                cliente.setId(id);


                cliente.setEmail(rs.getString("email"));
                //cliente.setCognome(rs.getString("cognome") );
                //cliente.setNome(rs.getString("nome"));
                cliente.setPassword(rs.getString("password"));
                cliente.setUsername(rs.getString("username"));
                cliente.setPermessi(rs.getString("permessi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    @Override
    public void delete(Cliente cliente) {

        String query = " DELETE FROM cliente where id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setLong(1, cliente.getId());
            st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
