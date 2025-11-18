package servicios;

import java.util.List;
import java.util.Set;

import dao.ArtistaDAO;

public class ArtistaService {

    private ArtistaDAO artDAO = new ArtistaDAO();
    
    public long insertarArtista(long persona_id, String apodo) {
    	return artDAO.insertarArtista(persona_id, apodo);
    }
    
    
}