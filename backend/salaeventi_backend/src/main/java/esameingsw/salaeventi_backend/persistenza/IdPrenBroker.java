package esameingsw.salaeventi_backend.persistenza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdPrenBroker {

    private static final String query = "SELECT nextval ('prenotazione_sequence') AS id";

    public static Long getId(Connection conn) {
        Long id = null;

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            id = resultSet.getLong("id");


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return id;
    }
}
