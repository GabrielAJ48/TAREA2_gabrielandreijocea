package servicios;

import dao.PersonaDAO;
import utilidades.GestorNacionalidades;

public class PersonaService {

	private PersonaDAO pers_dao = new PersonaDAO();
	
	public String getNombrePersona(String nombreUsuario, String contrasenia) {
		return pers_dao.getNombrePersona(nombreUsuario, contrasenia);
	}
	
	public String validarPersona(String nombre, String email, String nacionalidad) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre no puede estar vacío.";
        }

        String patronEmail = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$";
        if (!email.matches(patronEmail)) {
            return "El email no es válido.";
        }

        if (pers_dao.existeEmail(email)) {
        	return "El email ya está registrado.";
        }

        if (!GestorNacionalidades.comprobarNacionalidad(nacionalidad)) {
            return "La nacionalidad introducida no existe en el XML.";
        }

        return null;
    }

	public long insertarPersona (String nombre, String email, String nacionalidad) {
		long persona_id = pers_dao.insertarPersona(nombre, email, nacionalidad);
		
		return persona_id;
	}
}
