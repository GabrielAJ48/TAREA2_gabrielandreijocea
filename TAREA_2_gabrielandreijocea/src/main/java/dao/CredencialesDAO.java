package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CredencialesDAO {
	
	private Connection conex;
	private PreparedStatement p;
	private ResultSet rs;
	
	public CredencialesDAO() {
		conex = ConexionBD.getConexion();
		
	}
	
	public String getPerfil(String nombreUsuario, String contrasenia) {
		String perfil = null;
		String consulta = "SELECT perfil FROM credenciales WHERE nombre_usuario = ? AND password = ?";
		
		try {
			p = conex.prepareStatement(consulta);
			
			p.setString(1, nombreUsuario);
			p.setString(2, contrasenia);
			
			rs = p.executeQuery();
			if (rs.next()) {
				perfil = rs.getString("perfil"); 
			}
			rs.close();
			p.close();
		} catch (SQLException e) {
			
		}
		return perfil;
	}
	
	public boolean comprobarLogin(String user, String pass) {
		boolean ret = false;
		String consulta = "SELECT * FROM CREDENCIALES WHERE USER=? AND PASSWORD=?";
		try {
			p = conex.prepareStatement(consulta);
			
			p.setString(1, user);
			p.setString(2, pass);
			
			rs =p.executeQuery();
			
			rs.close();
			p.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean existeNombreUsuario(String nombreUsuario) {
		boolean existe = false;
		String consulta = "SELECT COUNT(*) FROM credenciales WHERE nombre_usuario = ?";
		
		try {
			p = conex.prepareStatement(consulta);
			
			p.setString(1, nombreUsuario);
			
			rs = p.executeQuery();
			
			rs.next();
			if(rs.getInt(1) > 0) {
				existe = true;
			}
		} catch (SQLException e) {
			
		}
		
		return existe;
	}
	
	public int insertarCredenciales(String usuario, String contrasenia, long persona_id) {
		String insercion = "INSERT INTO credenciales (nombre_usuario, password, persona_id) VALUES (?, ?, ?)";
		int credencial_id = -1;
		
	    try {
	    	p = conex.prepareStatement(insercion);
	    	p.setString(1, usuario);
	        p.setString(2, contrasenia);
	        p.setLong(3, persona_id);
	        p.executeUpdate();

	        rs = p.getGeneratedKeys();
	        if (rs.next()) {
	            credencial_id = rs.getInt(1);
	        }
	    } catch (SQLException e) {
			
		}

	    return credencial_id;
	}
	
}
