package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

	private static Connection conexion;
	private static String URL;
	private static String usuario;
	private static String contrasenia;

	static {
		try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("application.properties")) {
			if (input == null) {
				System.out.println("No se encontró el archivo application.properties");
			} else {
				Properties props = new Properties();
				props.load(input);

				URL = props.getProperty("URL");
				usuario = props.getProperty("Usuario");
				contrasenia = props.getProperty("contrasenia");
			}
		} catch (IOException e) {
			System.out.println("Error al leer fichero de propiedades");
		}
	}

	public static Connection getConexion() {
		try {
			conexion = DriverManager.getConnection(URL, usuario, contrasenia);
		} catch (SQLException e) {
			System.out.println("Error al conectarse a la bbdd");
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
