package entidades;

import java.time.LocalDate;

public class Coordinacion extends Persona{
	private Long idCoord;
	private boolean senior=false;
	private LocalDate fechasenior=null;
	
	public Coordinacion() {
		
	}

	public Coordinacion(Long idCoord, boolean senior, LocalDate fechasenior) {
		super();
		this.idCoord = idCoord;
		this.senior = senior;
		this.fechasenior = fechasenior;
	}

	public Long getIdCoord() {
		return idCoord;
	}

	public void setIdCoord(Long idCoord) {
		this.idCoord = idCoord;
	}

	public boolean isSenior() {
		return senior;
	}

	public void setSenior(boolean senior) {
		this.senior = senior;
	}

	public LocalDate getFechasenior() {
		return fechasenior;
	}

	public void setFechasenior(LocalDate fechasenior) {
		this.fechasenior = fechasenior;
	}
	
	
	
}
