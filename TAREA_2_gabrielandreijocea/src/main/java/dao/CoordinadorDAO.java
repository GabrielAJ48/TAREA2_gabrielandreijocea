package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CoordinadorDAO {

    private Connection conex;
    private PreparedStatement p;

    public CoordinadorDAO() {
        conex = ConexionBD.getConexion();
    }

    public boolean insertarCoordinador(long persona_id, boolean senior, String fechaSenior) {
        String sql = "INSERT INTO coordinacion (persona_id, senior, fecha_senior) VALUES (?,?,?)";

        try {
            p = conex.prepareStatement(sql);
            p.setLong(1, persona_id);
            p.setBoolean(2, senior);

            if (senior) {
                p.setString(3, fechaSenior);
            } else {
                p.setNull(3, java.sql.Types.DATE);
            }

            p.executeUpdate();
            p.close();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }
}

