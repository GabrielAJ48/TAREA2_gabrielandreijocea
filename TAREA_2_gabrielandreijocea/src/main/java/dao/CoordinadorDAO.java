package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CoordinadorDAO {

    private Connection conex;
    private PreparedStatement p;
    private ResultSet rs;

    public CoordinadorDAO() {
        conex = ConexionBD.getConexion();
    }

    public long insertarCoordinador(long persona_id, boolean senior, LocalDate fechaSenior) {
        String insert = "INSERT INTO coordinacion (persona_id, es_senior, fecha_senior) VALUES (?,?,?)";
        long coordinador_id = -1;
        try {
            p = conex.prepareStatement(insert, java.sql.Statement.RETURN_GENERATED_KEYS);
            p.setLong(1, persona_id);
            p.setBoolean(2, senior);

            if (senior) {
                p.setObject(3, fechaSenior);
            } else {
                p.setNull(3, java.sql.Types.DATE);
            }
            
            p.executeUpdate();
            rs = p.getGeneratedKeys();
            if (rs.next()) {
	            coordinador_id = rs.getLong(1);
	        }
            
            p.close();

        } catch (SQLException e) {
            
        }
        return coordinador_id;
    }
}

