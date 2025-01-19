package esameingsw.salaeventi_backend.persistenza;

import esameingsw.salaeventi_backend.persistenza.dao.ClienteDao;
import esameingsw.salaeventi_backend.persistenza.dao.PrenotazioneDao;
import esameingsw.salaeventi_backend.persistenza.dao.SalaEventoDao;
import esameingsw.salaeventi_backend.persistenza.dao.postgress.ClienteDaoPostgress;
import esameingsw.salaeventi_backend.persistenza.dao.postgress.PrenotazioneDaoPostgress;
import esameingsw.salaeventi_backend.persistenza.dao.postgress.SalaEventoDaoPostgress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {


    private static DBManager instance = null;

    public static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
        } return instance;
    }

    private DBManager(){
    }

    Connection conn = null;

    public Connection getConnection(){
        if (conn == null){
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sala-eventi", "postgres", "Samu2000");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public ClienteDao getClienteDao() {return new ClienteDaoPostgress(getConnection());
    }

    public SalaEventoDao getSalaEventoDao() {return new SalaEventoDaoPostgress(getConnection());
    }

    public PrenotazioneDao getPrenotazioneDao() {return new PrenotazioneDaoPostgress(getConnection());
    }


}
