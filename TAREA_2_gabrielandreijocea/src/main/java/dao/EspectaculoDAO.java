package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entidades.Espectaculo;

public class EspectaculoDAO {

    private Connection conex;
    private PreparedStatement p;
    private ResultSet rs;

    public EspectaculoDAO() {
        conex = ConexionBD.getConexion();
    }

    public List<Espectaculo> obtenerTodos() {
        List<Espectaculo> lista = new ArrayList<>();

        String consulta = "SELECT espectaculo_id, nombre, fecha_inicio, fecha_fin FROM espectaculo";

        try {
            p = conex.prepareStatement(consulta);
            rs = p.executeQuery();

            while (rs.next()) {
                Espectaculo esp = new Espectaculo();

                esp.setId(rs.getLong("espectaculo_id"));
                esp.setNombre(rs.getString("nombre"));
                esp.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                esp.setFechaFin(rs.getDate("fecha_fin").toLocalDate());

                lista.add(esp);
            }

            rs.close();
            p.close();

        } catch (SQLException e) {
        	
        }

        return lista;
    }

	public boolean existeNombreEsp(String nombreEsp) {
		boolean existe = false;
		String consulta = "SELECT COUNT(*) FROM persona WHERE nombre = ?";
		try {
			p = conex.prepareStatement(consulta);
			
			p.setString(1, nombreEsp);
			
			rs = p.executeQuery();
            rs.next();
            if (rs.getLong(1) > 0) {
                existe = true;
            }
            rs.close();
			p.close();
		} catch (SQLException e) {
			
		}
		
		return existe;
	}
	
	public long insertarEspectaculo(Espectaculo e) {
        long id = -1;
        String sql = "INSERT INTO espectaculo (nombre, fecha_inicio, fecha_fin, coordinador_id) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement p = conex.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, e.getNombre());
            p.setObject(2, e.getFechaInicio());
            p.setObject(3, e.getFechaFin());
            
            if (e.getIdCoord() != null && e.getIdCoord() > 0) {
                p.setLong(4, e.getIdCoord());
            } else {
                p.setNull(4, java.sql.Types.BIGINT);
            }
            
            p.executeUpdate();
            
            ResultSet rs = p.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
            }
            rs.close();
        } catch (SQLException ex) {
        	
        }
        return id;
    }

    public Espectaculo obtenerEspectaculoCompleto(long id) {
        Espectaculo esp = null;
        String sql = "SELECT * FROM espectaculo WHERE espectaculo_id = ?";
        
        try (PreparedStatement p = conex.prepareStatement(sql)) {
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                esp = new Espectaculo();
                esp.setId(rs.getLong("espectaculo_id"));
                esp.setNombre(rs.getString("nombre"));
                esp.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                esp.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                esp.setIdCoord(rs.getLong("coordinador_id"));
            }
            rs.close();
        } catch (SQLException e) {

        }
        return esp;
    }
    
    public boolean actualizarEspectaculo(Espectaculo e) {
        boolean ok = false;
        String sql = "UPDATE espectaculo SET nombre = ?, fecha_inicio = ?, fecha_fin = ?, coordinador_id = ? WHERE espectaculo_id = ?";
        
        try {
            p = conex.prepareStatement(sql);
            p.setString(1, e.getNombre());
            p.setObject(2, e.getFechaInicio());
            p.setObject(3, e.getFechaFin());
            
            if (e.getIdCoord() != null) {
                p.setLong(4, e.getIdCoord());
            } else {
                p.setNull(4, java.sql.Types.BIGINT);
            }
            
            p.setLong(5, e.getId());
            
            int filas = p.executeUpdate();
            ok = filas > 0;
            p.close();
        } catch (SQLException ex) {

        }
        return ok;
    }
}