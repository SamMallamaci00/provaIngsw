package esameingsw.salaeventi_backend.persistenza.dao.postgress;

import esameingsw.salaeventi_backend.persistenza.DBManager;
import esameingsw.salaeventi_backend.persistenza.IdPrenBroker;
import esameingsw.salaeventi_backend.persistenza.IdSalaBroker;
import esameingsw.salaeventi_backend.persistenza.dao.PrenotazioneDao;
import esameingsw.salaeventi_backend.persistenza.dao.SalaEventoDao;
import esameingsw.salaeventi_backend.persistenza.model.Cliente;
import esameingsw.salaeventi_backend.persistenza.model.Prenotazione;
import esameingsw.salaeventi_backend.persistenza.model.SalaEvento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDaoPostgress implements PrenotazioneDao {

    Connection conn;

    public PrenotazioneDaoPostgress(Connection conn) {
        this.conn = conn;
    }


    @Override
    public List<Prenotazione> findAll() {
        List<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();

        String query = "select * from prenotazione";


        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);


            while (rs.next()){
                Prenotazione prenotazione = new Prenotazione();

                SalaEvento sala = DBManager.getInstance().getSalaEventoDao().findById(rs.getLong("sala"));
                Cliente cliente = DBManager.getInstance().getClienteDao().findById(rs.getLong("cliente"));

                prenotazione.setId(rs.getLong("id"));
                prenotazione.setData((rs.getDate("data")));
                prenotazione.setCosto(rs.getFloat("costo"));

                prenotazione.setSala(sala);
                prenotazione.setCliente(cliente);


                prenotazioni.add(prenotazione);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return prenotazioni;

    }

    @Override
    public List<Prenotazione> findByCliente(Cliente cliente) {
        List<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();

        String query = "select * from prenotazione where cliente = ?";


        try {

            PreparedStatement st = conn.prepareStatement(query);

            st.setLong(1, cliente.getId());
            ResultSet rs = st.executeQuery();


            while (rs.next()){
                Prenotazione prenotazione = new Prenotazione();

                SalaEvento sala = DBManager.getInstance().getSalaEventoDao().findById(rs.getLong("sala"));

                prenotazione.setId(rs.getLong("id"));
                prenotazione.setData((rs.getDate("data")));
                prenotazione.setCosto(rs.getFloat("costo"));
                prenotazione.setSala(sala);
                prenotazione.setCliente(cliente);


                prenotazioni.add(prenotazione);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return prenotazioni;

    }

    @Override
    public Prenotazione findById(Long id) {
        System.out.println("id che arriva: " + id);


        String query = "select *  from prenotazione p where p.id = ? ";


        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                Prenotazione prenotazione = new Prenotazione();

                SalaEvento sala = DBManager.getInstance().getSalaEventoDao().findById(rs.getLong("sala"));
                Cliente cliente = DBManager.getInstance().getClienteDao().findById(rs.getLong("cliente"));

                prenotazione.setId(rs.getLong("id"));
                prenotazione.setData((rs.getDate("data")));
                prenotazione.setCosto(rs.getFloat("costo"));
                prenotazione.setSala(sala);
                prenotazione.setCliente(cliente);


                return prenotazione;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveOrUpdate(Prenotazione prenotazione) {
        PrenotazioneDao pDao = DBManager.getInstance().getPrenotazioneDao();


        if(pDao.findById(prenotazione.getId()) == null){
            String insertStr = "INSERT INTO prenotazione VALUES (?, ?, ?, ?, ?); ";

            System.out.println("dentro if");
            try {
                PreparedStatement st = conn.prepareStatement(insertStr);

                Long id = IdPrenBroker.getId(conn);
                st.setLong(1, id);
                st.setDate(2, prenotazione.getData());
                st.setFloat(3, prenotazione.getCosto());
                st.setLong(4, prenotazione.getSala().getId());
                st.setLong(5, prenotazione.getCliente().getId());



                st.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            String updateStr = "UPDATE prenotazione set data = ?, costo = ?, sala = ?, cliente = ? where id = ?";

            System.out.println("dentro else");
            try {
                PreparedStatement st = conn.prepareStatement(updateStr);
                st.setDate(1, prenotazione.getData());
                st.setFloat(2, prenotazione.getCosto());
                st.setLong(3, prenotazione.getSala().getId());
                st.setLong(4, prenotazione.getCliente().getId());
                st.setLong(5, prenotazione.getId());






                st.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void delete(Prenotazione prenotazione) {

        String query = " DELETE FROM prenotazione where id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setLong(1, prenotazione.getId());
            st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SalaEvento>  SaleDispByDate(Date date) {

        List<SalaEvento> sale = new ArrayList<SalaEvento>();

        String query = "SELECT s.id, s.nome, s.capienza, s.descrizione, s.prezzo, s.immagine " +
                "FROM sala s " +
                "LEFT JOIN prenotazione p ON s.id = p.id_sala AND p.data = ? " +
                "WHERE p.id_sala IS NULL";


        try {

            PreparedStatement st = conn.prepareStatement(query);

            st.setDate(1, date);
            ResultSet rs = st.executeQuery(query);


            while (rs.next()){
                SalaEvento salaEvento = new SalaEvento();
                salaEvento.setId(rs.getLong("id"));
                salaEvento.setNome(rs.getString("nome"));
                salaEvento.setCapienza(rs.getInt("capacita"));
                salaEvento.setDescrizione(rs.getString("descrizione"));
                salaEvento.setPrezzo(rs.getFloat("prezzo")); // Supponendo che il prezzo sia un double
                salaEvento.setImmagine(rs.getString("immagine")); // Supponendo che immagine sia una stringa (URL o path)


                sale.add(salaEvento);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return sale;

    }

    @Override
    public boolean DispSalaByDate(SalaEvento salaEvento, Date data) {

        String query = "SELECT NOT  EXISTS (" +
                "    SELECT 1 " +
                "    FROM prenotazione p " +
                "    WHERE p.sala = ? AND p.data = ?" +
                ") AS is_prenotata";

        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setLong(1, salaEvento.getId());
            st.setDate(2, data);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("is_prenotata"); // Restituisce true o false
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }



}
