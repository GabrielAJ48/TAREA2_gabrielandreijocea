package servicios;

import dao.PersonaDAO;

public class PersonaService {

	private PersonaDAO pers_dao = new PersonaDAO();
	
	public String getNombrePersona(String nombreUsuario, String contrasenia) {
		return pers_dao.getNombrePersona(nombreUsuario, contrasenia);
	}
}
