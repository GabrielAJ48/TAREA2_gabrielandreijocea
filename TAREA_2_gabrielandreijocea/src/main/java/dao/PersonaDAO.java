package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		} catch (SQLException e) {
			
		}
		return nombrePersona;
	}
	
	public String getNombrePersona(long id_persona) {
		
		String nombrePersona= null;
		String consulta = "SELECT nombre FROM persona WHERE persona_id = ?";
		
		try {
			p = conex.prepareStatement(consulta);
			
			p.setLong(1, id_persona);
			
			rs = p.executeQuery();
			if (rs.next()) {
				nombrePersona = rs.getString("nombre");
			}
			
			rs.close();
			p.close();
		} catch (SQLException e) {
			
		}
		return nombrePersona;
	}
	
	public long insertarPersona(String nombre, String email, String nacionalidad) {
		long persona_id = -1;
		String pais = "";
		String consulta_nacionalidad = "SELECT nombre_pais FROM paises WHERE pais_id = ?";
		
		try {
			p = conex.prepareStatement(consulta_nacionalidad);
			
			p.setString(1, nacionalidad);
			
			rs = p.executeQuery();
			
			if(rs.next()) {
				pais = rs.getString("nombre_pais");
			}
		} catch (SQLException e) {
			
		}
		
		String insercion = "INSERT INTO persona (nombre, email, nacionalidad) VALUES (?,?,?)";
		
		try {
			p = conex.prepareStatement(insercion);
			
			p.setString(1, nombre);
			p.setString(2, email);
			p.setString(3, pais);
			
			p.executeUpdate();
			
			rs = p.getGeneratedKeys();
			
			if(rs.next()) {
				persona_id = rs.getLong(1);
			}
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
		} catch (SQLException e) {
			
		}
		
		return existe;
	}
}
