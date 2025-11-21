package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import entidades.Coordinacion;

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
    
    public Coordinacion obtenerCoordinacionPorPersonaId(long personaId) {
        Coordinacion c = null;
        String consulta = "SELECT persona_id, coordinacion_id, es_senior, fecha_senior FROM coordinacion WHERE persona_id = ?";
        try {
            p = conex.prepareStatement(consulta);
            p.setLong(1, personaId);
            rs = p.executeQuery();
            if (rs.next()) {
                c = new Coordinacion();
                c.setIdCoord(rs.getLong("coordinacion_id"));
                c.setSenior(rs.getBoolean("es_senior"));
                java.sql.Date fd = rs.getDate("fecha_senior");
                if (fd != null) c.setFechasenior(fd.toLocalDate());
                c.setId(rs.getLong("persona_id"));
            }
            rs.close();
            p.close();
        } catch (SQLException e) { }
        return c;
    }

    public java.util.Map<Long,String> getEspectaculosPorCoordinador(long personaId) {
        java.util.Map<Long,String> mapa = new java.util.HashMap<>();
        String consulta = "SELECT espectaculo_id, nombre FROM espectaculo WHERE coordinador_id = ?";
        try {
            p = conex.prepareStatement(consulta);
            p.setLong(1, personaId);
            rs = p.executeQuery();
            while (rs.next()) {
                mapa.put(rs.getLong("espectaculo_id"), rs.getString("nombre"));
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
        	
        }
        return mapa;
    }

    public boolean asignarCoordinadorAEspectaculo(long espectaculoId, long personaId) {
        boolean ok = false;
        String update = "UPDATE espectaculo SET coordinador_id = ? WHERE espectaculo_id = ?";
        try {
            p = conex.prepareStatement(update);
            p.setLong(1, personaId);
            p.setLong(2, espectaculoId);
            int filas = p.executeUpdate();
            ok = filas > 0;
            p.close();
        } catch (SQLException e) {
        	ok = false;
        }
        return ok;
    }

    public boolean quitarCoordinadorDeEspectaculo(long espectaculoId) {
        boolean ok = false;
        String update = "UPDATE espectaculo SET coordinador_id = NULL WHERE espectaculo_id = ?";
        try {
            p = conex.prepareStatement(update);
            p.setLong(1, espectaculoId);
            int filas = p.executeUpdate();
            ok = filas > 0;
            p.close();
        } catch (SQLException e) {
        	ok = false;
        }
        return ok;
    }

    public boolean updateSenior(long personaId, boolean esSenior) {
        boolean ok = false;
        String update = "UPDATE coordinacion SET es_senior = ? WHERE persona_id = ?";
        try {
            p = conex.prepareStatement(update);
            p.setBoolean(1, esSenior);
            p.setLong(2, personaId);
            int filas = p.executeUpdate();
            ok = filas > 0;
            p.close();
        } catch (SQLException e) {
        	ok = false;
        }
        return ok;
    }

    public boolean updateFechaSenior(long personaId, java.time.LocalDate fecha) {
        boolean ok = false;
        String update = "UPDATE coordinacion SET fecha_senior = ? WHERE persona_id = ?";
        try {
            p = conex.prepareStatement(update);
            if (fecha != null) p.setObject(1, fecha);
            else p.setNull(1, java.sql.Types.DATE);
            p.setLong(2, personaId);
            int filas = p.executeUpdate();
            ok = filas > 0;
            p.close();
        } catch (SQLException e) {
        	ok = false;
        }
        return ok;
    }

    public java.util.Map<Long,String> obtenerListaEspectaculosBasica() {
        java.util.Map<Long,String> mapa = new java.util.HashMap<>();
        String consulta = "SELECT espectaculo_id, nombre FROM espectaculo";
        try {
            p = conex.prepareStatement(consulta);
            rs = p.executeQuery();
            while (rs.next()) {
                mapa.put(rs.getLong("espectaculo_id"), rs.getString("nombre"));
            }
            rs.close();
            p.close();
        } catch (SQLException e) {
        	
        }
        return mapa;
    }

	public long obtenerIdCoord(long persona_id) {
	long coord_id = -1;
		
		String consulta = "SELECT coordinacion_id FROM coordinacion WHERE persona_id = ?";
		
		try {
			p = conex.prepareStatement(consulta);
			
			p.setLong(1, persona_id);
			
			rs = p.executeQuery();
			if (rs.next()) {
				coord_id = rs.getLong("coordinacion_id");
			}
			rs.close();
			p.close();
		} catch (SQLException e) {
			
		}
		return coord_id;
	}
	
	public Map<Long, String> listaCoordinadores() {
		Map<Long, String> listaCoord = new HashMap<>();
		
		String consulta = "SELECT c.coordinacion_id, p.nombre " + 
                			"FROM coordinacion c " + 
                			"INNER JOIN persona p ON c.persona_id = p.persona_id";
		
		try {
			p = conex.prepareStatement(consulta);
			rs = p.executeQuery();
			
			while (rs.next()) {
				listaCoord.put(rs.getLong("coordinacion_id"), rs.getString("nombre"));
			}
		} catch (SQLException e) {
			
		}
		
		return listaCoord;
	}
	
	public Coordinacion obtenerDatosCoordinadorCompleto(long coordinacionId) {
	    Coordinacion coord = new Coordinacion();
	    
	    String consulta = "SELECT c.coordinacion_id, c.es_senior, p.nombre, p.email " +
	                      "FROM coordinacion c " +
	                      "INNER JOIN persona p ON c.persona_id = p.persona_id " +
	                      "WHERE c.coordinacion_id = ?";
	    try {
	        p = conex.prepareStatement(consulta);
	        p.setLong(1, coordinacionId);
	        rs = p.executeQuery();
	        
	        if (rs.next()) {
	            coord.setIdCoord(rs.getLong("coordinacion_id"));
	            coord.setSenior(rs.getBoolean("es_senior"));
	            coord.setNombre(rs.getString("nombre"));
	            coord.setEmail(rs.getString("email"));
	        }
	        rs.close();
	        p.close();
	    } catch (SQLException e) {

	    }
	    return coord;
	}
	
}