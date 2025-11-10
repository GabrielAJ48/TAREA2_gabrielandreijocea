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
		String perfil = "";
		String consulta = "SELECT perfil FROM credenciales WHERE nombre_usuario = ? AND password = ?";
		
		try {
			p = conex.prepareStatement(consulta);
			
			p.setString(1, nombreUsuario);
			p.setString(2, contrasenia);
			
			rs = p.executeQuery();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
}
