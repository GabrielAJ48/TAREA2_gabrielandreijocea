package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Paises_DAO {

	private Connection conex;
	private PreparedStatement p;
	private ResultSet rs;
	
	public Paises_DAO() {
		conex = ConexionBD.getConexion();
	}
	
	public boolean existePais(String nacionalidad) {
		boolean existe = false;
		String consulta = "SELECT COUNT(*) FROM paises WHERE pais_id = ?";
        try {
        	p = conex.prepareStatement(consulta);
        	
        	p.setString(1, nacionalidad);
            rs = p.executeQuery();
            rs.next();
            if (rs.getInt(1) != 0) {
                existe = true;
            }
        } catch (SQLException e) {
			
		}
		return existe;
	}
}
