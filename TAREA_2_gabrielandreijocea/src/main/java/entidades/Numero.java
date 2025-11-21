package entidades;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Numero implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private int orden;
	private String nombre;
	private double duracion;
	private Set<Artista> artistas = new HashSet<>();
	private Long idEspectaculo;
	
	public Numero() {
		
	}

	public Numero(Long id, int orden, String nombre, double duracion, Set<Artista> artistas, Long idEspectaculo) {
		this.id = id;
		this.orden = orden;
		this.nombre = nombre;
		this.duracion = duracion;
		this.artistas = artistas;
		this.idEspectaculo = idEspectaculo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getDuracion() {
		return duracion;
	}

	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}

	public Set<Artista> getArtistas() {
		return artistas;
	}

	public void setArtistas(Set<Artista> artistas) {
		this.artistas = artistas;
	}

	public Long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}
	
	
	
	
}
