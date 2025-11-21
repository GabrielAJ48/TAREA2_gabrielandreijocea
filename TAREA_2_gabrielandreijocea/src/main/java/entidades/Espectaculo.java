package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

public class Espectaculo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Long idCoord;
	private Set<Numero> numeros = new TreeSet<>();
	
	public Espectaculo() {
		
	}

	public Espectaculo(Long id, String nombre, LocalDate fechaInicio, LocalDate fechaFin, Long idCoord,
			Set<Numero> numeros) {
		this.id = id;
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.idCoord = idCoord;
		this.numeros = numeros;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Long getIdCoord() {
		return idCoord;
	}

	public void setIdCoord(Long idCoord) {
		this.idCoord = idCoord;
	}

	public Set<Numero> getNumeros() {
		return numeros;
	}

	public void setNumeros(Set<Numero> numeros) {
		this.numeros = new TreeSet<>(numeros);
	}
	
	
	
}
