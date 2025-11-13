package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import servicios.Propiedades;

public class ConexionBD {

	private static Connection conexion;
	private static String URL=Propiedades.get("URL");
	private static String usuario=Propiedades.get("usuario");
	private static String contrasenia=Propiedades.get("contrasenia");

	public static Connection getConexion() {
		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Driver JDBC no encontrado");
		        e.printStackTrace();
			}
			conexion = DriverManager.getConnection(URL, usuario, contrasenia);
		} catch (SQLException e) {
			System.out.println("Error al conectarse a la bbdd"+e.getMessage());
		    e.printStackTrace();
		}
		return conexion;
	}

	public static boolean cerrarConexion() {
		boolean ret = false;

		if (conexion != null) {
			try {
				if (!conexion.isClosed())

					conexion.close();
				ret = true;
				return ret;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}
}
