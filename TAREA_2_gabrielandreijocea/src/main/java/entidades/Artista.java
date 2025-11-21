package entidades;

import java.util.HashSet;
import java.util.Set;

public class Artista extends Persona{
	
	private Long idArt;
	private String apodo = null;
	private Set <Especialidades> especialidades = new HashSet<>();
	
	public Artista() {
		
	}

	public Long getIdArt() {
		return idArt;
	}

	public void setIdArt(Long idArt) {
		this.idArt = idArt;
	 }

	public String getApodo() {
		return apodo;
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	public Set<Especialidades> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidad(Set<Especialidades> especialidad) {
		this.especialidades = especialidad;
	}
	
	public void addEspecialidad(Especialidades e) {
        if (e != null) {
            especialidades.add(e);
        }
    }
	
}
