package entidades;

public class Sesion {
	
	private static String nombre ="Invitado";
	private static String perfil = "INVITADO";
	private static Long idPersona = null;
	
	public Sesion() {
		
	}

	public Sesion(String nombre, String perfil) {
		Sesion.nombre = nombre;
		Sesion.perfil = perfil;
	}

	public static String getNombre() {
		return nombre;
	}

	public static void setNombre(String nombre) {
		Sesion.nombre = nombre;
	}

	public static String getPerfil() {
		return perfil;
	}

	public static void setPerfil(String perfil) {
		Sesion.perfil = perfil;
	}
	
	public static Long getIdPersona() {
		return idPersona;
	}

	public static void setIdPersona(Long idPersona) {
		Sesion.idPersona = idPersona;
	}
}
