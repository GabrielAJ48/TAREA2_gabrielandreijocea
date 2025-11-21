package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import entidades.Artista;

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
            ret = true;

        } catch (SQLException e) {
            
        }
        
        return ret;
    }
    
    public Artista obtenerArtistaPorPersonaId(long personaId) {
        Artista a = null;
        String consulta = "SELECT persona_id, artista_id, apodo FROM artista WHERE persona_id = ?";
        try {
            p = conex.prepareStatement(consulta);
            p.setLong(1, personaId);
            rs = p.executeQuery();
            if (rs.next()) {
                a = new Artista();
                a.setId(rs.getLong("persona_id"));
                a.setIdArt(rs.getLong("artista_id"));
                a.setApodo(rs.getString("apodo"));
            }
            if (rs != null) rs.close();
            if (p != null) p.close();
        } catch (SQLException e) { }
        return a;
    }

    public java.util.Set<String> obtenerEspecialidadesPorArtista(long artistaPersonaId) {
        java.util.Set<String> set = new java.util.HashSet<>();
        String consulta = "SELECT e.nombre FROM artista_especialidad ae JOIN especialidad e ON ae.especialidad_id = e.especialidad_id WHERE ae.artista_id = ?";
        try {
            p = conex.prepareStatement(consulta);
            p.setLong(1, artistaPersonaId);
            rs = p.executeQuery();
            while (rs.next()) {
                set.add(rs.getString("nombre"));
            }
            if (rs != null) rs.close();
            if (p != null) p.close();
        } catch (SQLException e) { }
        return set;
    }

    public boolean eliminarEspecialidades(long artistaPersonaId) {
        boolean ok = false;
        String delete = "DELETE FROM artista_especialidad WHERE artista_id = ?";
        try {
            p = conex.prepareStatement(delete);
            p.setLong(1, artistaPersonaId);
            int filas = p.executeUpdate();
            ok = filas >= 0;
            p.close();
        } catch (SQLException e) {
            ok = false;
        }
        return ok;
    }

    public boolean eliminarEspecialidadPorNombre(long artistaPersonaId, String especialidadNombre) {
        long idEsp = obtenerIdEspecialidad(especialidadNombre);
        if (idEsp == -1) return false;
        String delete = "DELETE FROM artista_especialidad WHERE artista_id = ? AND especialidad_id = ?";
        try {
            p = conex.prepareStatement(delete);
            p.setLong(1, artistaPersonaId);
            p.setLong(2, idEsp);
            int filas = p.executeUpdate();
            p.close();
            return filas > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean actualizarApodo(long artistaPersonaId, String apodo) {
        boolean ok = false;
        String update = "UPDATE artista SET apodo = ? WHERE persona_id = ?";
        try {
            p = conex.prepareStatement(update);
            p.setString(1, apodo);
            p.setLong(2, artistaPersonaId);
            int filas = p.executeUpdate();
            ok = filas > 0;
            p.close();
        } catch (SQLException e) { 
        	ok = false; 
        }
        return ok;
    }
    
    public Map<Long, String> obtenerListaArtistas() {
        java.util.Map<Long, String> mapa = new java.util.HashMap<>();
        String sql = "SELECT a.artista_id, p.nombre, a.apodo "+
                     "FROM artista a JOIN persona p ON a.persona_id = p.persona_id";
        try {
            p = conex.prepareStatement(sql);
            rs = p.executeQuery();
            while(rs.next()){
                String nombre = rs.getString("nombre");
                if(rs.getString("apodo") != null) nombre += " (" + rs.getString("apodo") + ")";
                mapa.put(rs.getLong("artista_id"), nombre);
            }
            rs.close();
            p.close();
        } catch (SQLException e) {

        }
        return mapa;
    }

	public List<String[]> obtenerTrayectoria(long personaId) {
		List<String[]> trayectoria = new java.util.ArrayList<>();
	    
	    String sql = "SELECT e.espectaculo_id, e.nombre AS esp_nombre, n.numero_id, n.nombre AS num_nombre " +
	                 "FROM artista a " +
	                 "JOIN numero_artista na ON a.artista_id = na.artista_id " +
	                 "JOIN numero n ON na.numero_id = n.numero_id " +
	                 "JOIN espectaculo e ON n.espectaculo_id = e.espectaculo_id " +
	                 "WHERE a.persona_id = ? " +
	                 "ORDER BY e.fecha_inicio DESC, n.orden ASC";           
	    try {
	        p = conex.prepareStatement(sql);
	        p.setLong(1, personaId);
	        rs = p.executeQuery();
	        
	        while (rs.next()) {
	            String[] fila = new String[4];
	            fila[0] = String.valueOf(rs.getLong("espectaculo_id"));
	            fila[1] = rs.getString("esp_nombre");
	            fila[2] = String.valueOf(rs.getLong("numero_id"));
	            fila[3] = rs.getString("num_nombre");
	            trayectoria.add(fila);
	        }
	        rs.close();
	        p.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return trayectoria;
	}
}