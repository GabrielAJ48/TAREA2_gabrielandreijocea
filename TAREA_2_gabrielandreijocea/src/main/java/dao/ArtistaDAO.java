package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistaDAO {

    private Connection conex;
    private PreparedStatement p;
    private ResultSet rs;

    public ArtistaDAO() {
        conex = ConexionBD.getConexion();
    }

    public long insertarArtista(long persona_id, String apodo) {
        String insert = "INSERT INTO artista (persona_id, apodo) VALUES (?,?)";
        long artista_id = -1;
        try {
            p = conex.prepareStatement(insert, java.sql.Statement.RETURN_GENERATED_KEYS);
            p.setLong(1, persona_id);

            if (apodo == null) {
                p.setNull(2, java.sql.Types.VARCHAR);
            } else {
                p.setString(2, apodo);
            }
            p.executeUpdate();
            rs = p.getGeneratedKeys();
	        if (rs.next()) {
	            artista_id = rs.getLong(1);
	        }
	        
	        rs.close();
			p.close();
        } catch (SQLException e) {
            
        }
        return artista_id;
    }
    
    public long obtenerIdEspecialidad(String nombreEspecialidad) {
        String consulta = "SELECT especialidad_id FROM especialidad WHERE nombre = ?";
        
        try {
        	p = conex.prepareStatement(consulta);
            p.setString(1, nombreEspecialidad);
            rs = p.executeQuery();

            if (rs.next()) {
                return rs.getLong("especialidad_id");
            }
            
        } catch (SQLException e) {
            
        }
        return -1; 
    }
    
    public boolean insertarArtistaEspecialidad(long artista_id, String especialidad) {
    	boolean ret = false;
    	
    	long especialidad_id = obtenerIdEspecialidad(especialidad);
    	
        String insert = "INSERT INTO artista_especialidad (artista_id, especialidad_id) VALUES (?, ?)";

        try {
        	p = conex.prepareStatement(insert);
            p.setLong(1, artista_id); 
            p.setLong(2, especialidad_id);

            p.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }
}