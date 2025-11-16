package servicios;

import dao.CredencialesDAO;

public class CredencialesService {

	private CredencialesDAO cred_dao= new CredencialesDAO();
	
	public String tomarPerfil(String usuario, String contrasenia) {
		return cred_dao.getPerfil(usuario, contrasenia);
	}
	
	public boolean validarCredencialesLogin(String nombreUsuario, String contrasenia) {
	    if (nombreUsuario == null || contrasenia == null || nombreUsuario.isEmpty() || contrasenia.isEmpty()) {
	        return false;
	    }

	    if (nombreUsuario.equals(Propiedades.get("usuarioAdmin"))
	            && contrasenia.equals(Propiedades.get("passwordAdmin"))) {
	        return true;
	    }

	    CredencialesDAO credencialesDAO = new CredencialesDAO();
	    String perfil = credencialesDAO.getPerfil(nombreUsuario, contrasenia);

	    return perfil != null && !perfil.isEmpty();
	}
	
	public String validarCredencialesRegistro(String nombreUsuario, String contrasenia) {
		if(nombreUsuario == null || nombreUsuario.length() <= 2) {
	        return "El nombre de usuario debe tener más de 2 caracteres.";
	    }

	    if (nombreUsuario.contains(" ")) {
	        return "El nombre de usuario no puede contener espacios.";
	    }

	    if (!nombreUsuario.matches("^[a-zA-Z0-9]+$")) {
	        return "El nombre de usuario solo puede contener letras sin tildes, dieresis o números.";
	    }

	    if (cred_dao.existeNombreUsuario(nombreUsuario)) {
	        return "El nombre de usuario ya está registrado. Debe elegir otro.";
	    }

	    if (contrasenia == null || contrasenia.length() <= 2) {
	        return "La contraseña debe tener más de 2 caracteres.";
	    }

	    if (contrasenia.contains(" ")) {
	        return "La contraseña no puede contener espacios.";
	    }
	    
	    return null;
	}
	
	public int insertarCredenciales(String nombreUsuario, String contrasenia, long persona_id) {
		return cred_dao.insertarCredenciales(nombreUsuario, contrasenia, persona_id);
	}

}
