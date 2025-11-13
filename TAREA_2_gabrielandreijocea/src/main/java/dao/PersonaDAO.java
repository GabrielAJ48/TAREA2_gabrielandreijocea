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
		String consulta = "SELECT persona_id FROM credenciales WHERE nombre_usuario = ? AND password = ?";
		int id = 0;
		
		try {
			p = conex.prepareStatement(consulta);
			
			p.setString(1, nombreUsuario);
			p.setString(2, contrasenia);
			
			rs = p.executeQuery();
			if (rs.next()) {
				id = rs.getInt("persona_id"); 
			}
			
			p = conex.prepareStatement("SELECT nombre FROM persona WHERE persona_id = ?");
			p.setInt(1, id);
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
	}
