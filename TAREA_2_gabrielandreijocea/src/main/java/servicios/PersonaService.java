package servicios;

import java.util.Map;

import dao.PersonaDAO;
import entidades.Persona;
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

	public Map<Long, String> getListaPersonas() {
		return pers_dao.getListaPersonas();
	}

	public boolean existePersona(long persona_idMod) {
		return pers_dao.existePersona(persona_idMod);
	}
	
	public Persona obtenerPersona(long id) {
	    return pers_dao.obtenerPersona(id);
	}

	public String validarModificacionPersona(String nombre, String email, String nacionalidad, long idActual) {

	    if (nombre == null || nombre.trim().isEmpty()) {
	        return "El nombre no puede estar vacío.";
	    }

	    String patronEmail = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$";
	    if (!email.matches(patronEmail)) {
	        return "El email no es válido.";
	    }

	    if (pers_dao.emailEnUsoPorOtraPersona(email, idActual)) {
	        return "El email ya está registrado por otra persona.";
	    }

	    if (!GestorNacionalidades.comprobarNacionalidad(nacionalidad)) {
	        return "La nacionalidad introducida no existe en el XML.";
	    }

	    return null;
	}

	public String modificarNombre(long id, String nuevoNombre) {
	    Persona actual = pers_dao.obtenerPersona(id);
	    if (actual == null) return "Persona no encontrada.";

	    String err = validarModificacionPersona(nuevoNombre, actual.getEmail(), actual.getNacionalidad(), id);
	    if (err != null) return err;

	    boolean ok = pers_dao.modificarPersona(id, nuevoNombre, actual.getEmail(), actual.getNacionalidad());
	    return ok ? null : "Error actualizando el nombre.";
	}

	public String modificarEmail(long id, String nuevoEmail) {
	    Persona actual = pers_dao.obtenerPersona(id);
	    if (actual == null) return "Persona no encontrada.";

	    String err = validarModificacionPersona(actual.getNombre(), nuevoEmail, actual.getNacionalidad(), id);
	    if (err != null) return err;

	    boolean ok = pers_dao.modificarPersona(id, actual.getNombre(), nuevoEmail, actual.getNacionalidad());
	    return ok ? null : "Error actualizando el email.";
	}

	public String modificarNacionalidad(long id, String nuevaNacionalidad) {
	    Persona actual = pers_dao.obtenerPersona(id);
	    if (actual == null) return "Persona no encontrada.";

	    String err = validarModificacionPersona(actual.getNombre(), actual.getEmail(), nuevaNacionalidad, id);
	    if (err != null) return err;

	    boolean ok = pers_dao.modificarPersona(id, actual.getNombre(), actual.getEmail(), nuevaNacionalidad);
	    return ok ? null : "Error actualizando la nacionalidad.";
	}
}
