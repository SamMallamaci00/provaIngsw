package esameingsw.salaeventi_backend.persistenza.dao.postgress;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import esameingsw.salaeventi_backend.persistenza.DBManager;
import esameingsw.salaeventi_backend.persistenza.IdSalaBroker;
import esameingsw.salaeventi_backend.persistenza.dao.SalaEventoDao;
import esameingsw.salaeventi_backend.persistenza.model.Cliente;
import esameingsw.salaeventi_backend.persistenza.model.SalaEvento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaEventoDaoPostgress implements SalaEventoDao {

    Connection conn;

    public SalaEventoDaoPostgress(Connection conn) {
        this.conn = conn;
    }


    @Override
    public List<SalaEvento> findAll() {
        List<SalaEvento> sale = new ArrayList<SalaEvento>();

        String query = "select * from salaevento";


        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);


            while (rs.next()){
                SalaEvento sala = new SalaEvento();
                sala.setId(rs.getLong("id"));
                sala.setCapienza((rs.getInt("capienza")));
                sala.setPrezzo((rs.getFloat("prezzo")));
                sala.setNome((rs.getString("nome")));
                sala.setImmagine((rs.getString("immagine")));
                sala.setDescrizione((rs.getString("descrizione")));



                sale.add(sala);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return sale;
    }

    @Override
    public SalaEvento findById(Long id) {

        System.out.println("id che arriva: " + id);


        String query = "select *  from salaevento s where s.id = ? ";


        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                SalaEvento salaEvento = new SalaEvento();


                salaEvento.setId(rs.getLong("id"));
                salaEvento.setPrezzo(rs.getFloat("prezzo"));
                salaEvento.setNome(rs.getString("nome"));
                salaEvento.setDescrizione(rs.getString("descrizione"));
                salaEvento.setImmagine(rs.getString("immagine"));
                salaEvento.setCapienza(rs.getInt("capienza"));


                return salaEvento;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;    }

    @Override
    public void saveOrUpdate(SalaEvento salaEvento) {
        SalaEventoDao sDao = DBManager.getInstance().getSalaEventoDao();


        if(sDao.findById(salaEvento.getId()) == null){
            String insertStr = "INSERT INTO salaevento VALUES (?, ?, ?, ?,?,?); ";

            System.out.println("dentro if");
            try {
                PreparedStatement st = conn.prepareStatement(insertStr);

                Long id = IdSalaBroker.getId(conn);
                st.setLong(1, id);
                st.setInt(2, salaEvento.getCapienza());
                st.setFloat(3, salaEvento.getPrezzo());
                st.setString(4, salaEvento.getNome());
                st.setString(5, salaEvento.getImmagine());
                st.setString(6, salaEvento.getDescrizione());



                st.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            String updateStr = "UPDATE salaevento set capienza = ?, prezzo = ?, nome = ?, immagine = ?, descrizione = ? where id = ?";

            System.out.println("dentro else");
            try {
                PreparedStatement st = conn.prepareStatement(updateStr);
                st.setInt(1, salaEvento.getCapienza());
                st.setFloat(2, salaEvento.getPrezzo());
                st.setString(3, salaEvento.getNome());
                st.setString(4, salaEvento.getImmagine());
                st.setString(5, salaEvento.getDescrizione());



                st.setLong(6, salaEvento.getId());



                st.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void delete(SalaEvento salaEvento) {

        String query = " DELETE FROM salaevento where id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setLong(1, salaEvento.getId());
            st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
