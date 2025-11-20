package servicios;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dao.ArtistaDAO;

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
    
    
}