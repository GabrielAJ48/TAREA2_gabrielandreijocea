package entidades;

public class Credenciales {
	
	private Long id;
	private String nombreUsuario;
	private String password;
	
	public Credenciales() {
		
	}

	public Credenciales(Long id, String nombre, String password) {
		super();
		this.id = id;
		this.nombreUsuario = nombre;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombre) {
		this.nombreUsuario = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
