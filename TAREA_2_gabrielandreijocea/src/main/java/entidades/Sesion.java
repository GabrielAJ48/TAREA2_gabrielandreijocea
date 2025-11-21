package entidades;

public class Sesion {
	
	private String nombre; 
	private Perfiles perfil;
	
	public Sesion() {
		
	}

	public Sesion(String nombre, Perfiles perfil) {
		this.nombre ="Invitado";
		this.perfil = Perfiles.INVITADO;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Perfiles getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfiles perfil) {
		this.perfil = perfil;
	}
}
