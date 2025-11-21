package servicios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.ArtistaDAO;
import entidades.Artista;
import entidades.Especialidades;

public class ArtistaService {

    private ArtistaDAO artDAO = new ArtistaDAO();
    
    public long insertarArtista(long persona_id, String apodo) {
    	return artDAO.insertarArtista(persona_id, apodo);
    }
    
    public Set<String> validarEspecialidades(Set<String> especialidades){
    	Set<String> especialidadesInvalidas = new HashSet<>();
    	
    	if (especialidades.isEmpty() || especialidades == null) {
    		especialidadesInvalidas.add("Debe introducir al menos una especialidad.");
    		return especialidadesInvalidas;
    	}
    	for (String e: especialidades) {
    		if (!e.matches("ACROBACIA|HUMOR|MAGIA|EQUILIBRISMO|MALABARISMO")) {
    			especialidadesInvalidas.add("La especialidad "+e+" no es una especialidad valida");
    		}
    	}
    	
    	if (especialidadesInvalidas.isEmpty()) {
    		especialidadesInvalidas = null;
    	}
    	
    	return especialidadesInvalidas;
    			
    }

	public boolean insertarEspecialidades(long artista_id, Set<String> especialidades) {
		boolean ret = true;
		
		for (String e: especialidades) {
			ret = artDAO.insertarArtistaEspecialidad(artista_id, e);
		}
		
		return ret;
	}
	
	public Artista obtenerArtistaPorPersona(long personaId) {
	    return artDAO.obtenerArtistaPorPersonaId(personaId);
	}

	public java.util.Set<String> listarEspecialidades(long personaId) {
	    return artDAO.obtenerEspecialidadesPorArtista(personaId);
	}

	public String agregarEspecialidad(long personaId, String especialidad) {
	    if (especialidad == null || especialidad.trim().isEmpty()) {
	        return "Especialidad vacía.";
	    }
	    try {
	        Especialidades.valueOf(especialidad.toUpperCase());
	    } catch (Exception ex) {
	        return "Especialidad no válida.";
	    }
	    boolean ok = artDAO.insertarArtistaEspecialidad(personaId, especialidad.toUpperCase());
	    return ok ? null : "No se pudo añadir la especialidad";
	}

	public String eliminarEspecialidad(long personaId, String especialidad) {
	    if (especialidad == null || especialidad.trim().isEmpty()) {
	        return "Especialidad vacía.";
	    }
	    try {
	        Especialidades.valueOf(especialidad.toUpperCase());
	    } catch (Exception ex) {
	        return "Especialidad no válida.";
	    }
	    boolean ok = artDAO.eliminarEspecialidadPorNombre(personaId, especialidad.toUpperCase());
	    return ok ? null : "No se pudo eliminar la especialidad (quizá no existe).";
	}

	public String modificarApodo(long personaId, String nuevoApodo) {
	    if (nuevoApodo == null || nuevoApodo.trim().isEmpty()) {
	        return "El apodo no puede estar vacío.";
	    }
	    boolean ok = artDAO.actualizarApodo(personaId, nuevoApodo);
	    return ok ? null : "Error actualizando apodo.";
	}
    
	public Map<Long, String> listarArtistasParaSeleccion() {
        return artDAO.obtenerListaArtistas(); 
    }

	public java.util.List<String[]> obtenerTrayectoria(long personaId) {
	    return artDAO.obtenerTrayectoria(personaId);
	}
    
}