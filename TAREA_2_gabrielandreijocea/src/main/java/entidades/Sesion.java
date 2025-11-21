package entidades;

public class Sesion {
	
	private String nombre; 
	private Perfiles perfil;
	private long persona_id;
	
	public Sesion() {
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

	public long getPersona_id() {
		return persona_id;
	}

	public void setPersona_id(long persona_id) {
		this.persona_id = persona_id;
	}
}
