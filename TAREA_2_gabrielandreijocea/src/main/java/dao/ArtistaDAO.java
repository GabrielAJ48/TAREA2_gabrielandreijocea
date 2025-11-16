package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArtistaDAO {

    private Connection conex;
    private PreparedStatement p;

    public ArtistaDAO() {
        conex = ConexionBD.getConexion();
    }

    public boolean insertarArtista(long persona_id, String apodo) {
        String sql = "INSERT INTO artista (persona_id, apodo) VALUES (?,?)";

        try {
            p = conex.prepareStatement(sql);
            p.setLong(1, persona_id);

            if (apodo == null) {
                p.setNull(2, java.sql.Types.VARCHAR);
            } else {
                p.setString(2, apodo);
            }

            p.executeUpdate();
            p.close();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean insertarEspecialidad(long persona_id, String especialidad) {
        String sql = "INSERT INTO habilidades_artista (persona_id, habilidad) VALUES (?,?)";

        try {
            p = conex.prepareStatement(sql);
            p.setLong(1, persona_id);
            p.setString(2, especialidad);

            p.executeUpdate();
            p.close();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }
}