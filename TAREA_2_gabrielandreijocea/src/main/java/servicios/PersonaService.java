package servicios;

import dao.Paises_DAO;
import dao.PersonaDAO;

public class PersonaService {

	private PersonaDAO pers_dao = new PersonaDAO();
	private Paises_DAO paises_dao = new Paises_DAO();
	
	public String getNombrePersona(String nombreUsuario, String contrasenia) {
		return pers_dao.getNombrePersona(nombreUsuario, contrasenia);
	}
	
	public String validarPersona(String nombre, String email, String nacionalidad) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre no puede estar vacío.";
        }

        String patronEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(patronEmail)) {
            return "El email no es válido.";
        }

        if (pers_dao.existeEmail(email)) {
        	return "El email ya está registrado.";
        }

        if (!paises_dao.existePais(nacionalidad)) {
            return "La nacionalidad introducida no existe.";
        }

        return null;
    }

	public long insertarPersona (String nombre, String email, String nacionalidad) {
		long persona_id = pers_dao.insertarPersona(nombre, email, nacionalidad);
		
		return persona_id;
	}
}
