package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import entidades.Persona;

public class PersonaDAO {
	
	private Connection conex;
	private PreparedStatement p;
	private ResultSet rs;
	
	public PersonaDAO() {
		conex = ConexionBD.getConexion();
	}
	
	public String getNombrePersona(String nombreUsuario, String contrasenia) {
		String nombrePersona = "";
		String consulta = "SELECT p.nombre FROM persona p " +
		        "INNER JOIN credenciales c ON p.persona_id = c.persona_id " +
		        "WHERE c.nombre_usuario = ? AND c.password = ?";
		try {
			p = conex.prepareStatement(consulta);
			
			p.setString(1, nombreUsuario);
			p.setString(2, contrasenia);
			
			rs = p.executeQuery();
			if (rs.next()) {
				nombrePersona = rs.getString("nombre");
			}
			rs.close();
			p.close();
		} catch (SQLException e) {
			nombrePersona = "Error al buscar el nombre en la base de datos";
		}
		return nombrePersona;
	}
	
	public long insertarPersona(String nombre, String email, String nacionalidad) {
		long persona_id = -1;
		
		String insercion = "INSERT INTO persona (nombre, email, pais_id) VALUES (?,?,?)";
		
		try {
			p = conex.prepareStatement(insercion, java.sql.Statement.RETURN_GENERATED_KEYS);
			
			p.setString(1, nombre);
			p.setString(2, email);
			p.setString(3, nacionalidad);
			
			p.executeUpdate();
			
			rs = p.getGeneratedKeys();
			
			if(rs.next()) {
				persona_id = rs.getLong(1);
			}
			rs.close();
			p.close();
		} catch (SQLException e) {
			
		}
		
		return persona_id;
	}
	
	public boolean existeEmail(String email) {
		boolean existe = false;
		String consulta = "SELECT COUNT(*) FROM persona WHERE email = ?";
		try {
			p = conex.prepareStatement(consulta);
			
			p.setString(1, email);
			
			rs = p.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                existe = true;
            }
            rs.close();
			p.close();
		} catch (SQLException e) {
			
		}
		
		return existe;
	}

	public Map<Long, String> getListaPersonas() {
		Map<Long, String> listaPersonas = new HashMap<>();
		long id = 0;
		String nombre = "";
		String consulta = "SELECT persona_id, nombre FROM persona";
		try {
			p = conex.prepareStatement(consulta);
			
			rs = p.executeQuery();
			while (rs.next()) {
				id = rs.getLong("persona_id");
				nombre = rs.getString("nombre");
				listaPersonas.put(id, nombre);
			}
			rs.close();
			p.close();	
		} catch (SQLException e) {
			
		}
		
		return listaPersonas;
	}

	public boolean existePersona(long persona_idMod) {
		boolean existe = false;
		String consulta = "SELECT COUNT(*) FROM persona WHERE persona_id = ?";
		try {
			p = conex.prepareStatement(consulta);
			
			p.setLong(1, persona_idMod);
			
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
	
	public Persona obtenerPersona(long personaId) {
	    Persona per = null;
	    String select = "SELECT persona_id, nombre, email, pais_id FROM persona WHERE persona_id = ?";
	    try {
	        p = conex.prepareStatement(select);
	        p.setLong(1, personaId);
	        rs = p.executeQuery();
	        if (rs.next()) {
	            per = new Persona();
	            per.setId(rs.getLong("persona_id"));
	            per.setNombre(rs.getString("nombre"));
	            per.setEmail(rs.getString("email"));
	            per.setNacionalidad(rs.getString("pais_id"));
	        }
	        rs.close();
	        p.close();
	    } catch (SQLException e) {

	    }
	    return per;
	}

	public boolean modificarPersona(long id, String nombre, String email, String paisId) {
	    boolean ok = false;
	    String update = "UPDATE persona SET nombre = ?, email = ?, pais_id = ? WHERE persona_id = ?";
	    try {
	        p = conex.prepareStatement(update);
	        p.setString(1, nombre);
	        p.setString(2, email);
	        p.setString(3, paisId);
	        p.setLong(4, id);
	        int filas = p.executeUpdate();
	        ok = filas > 0;
	        p.close();
	    } catch (SQLException e) {
	        ok = false;
	    }
	    return ok;
	}

	public boolean emailEnUsoPorOtraPersona(String email, long idActual) {
	    boolean existe = false;
	    String consulta = "SELECT COUNT(*) FROM persona WHERE email = ? AND persona_id <> ?";
	    try {
	        p = conex.prepareStatement(consulta);
	        p.setString(1, email);
	        p.setLong(2, idActual);
	        rs = p.executeQuery();
	        if (rs.next()) {
	            existe = rs.getInt(1) > 0;
	        }
	        rs.close();
	        p.close();
	    } catch (SQLException e) {
	    	
	    }
	    return existe;
	}
}
