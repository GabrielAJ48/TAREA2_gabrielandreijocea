package servicios;

import dao.CredencialesDAO;
import entidades.Credenciales;
import entidades.Sesion;

public class CredencialesService {

	private CredencialesDAO cred_dao= new CredencialesDAO();
	
	public String tomarPerfil(String usuario, String contrasenia) {
		return cred_dao.getPerfil(usuario, contrasenia);
	}
	
	/*public boolean comprobarCredenciales(Credenciales cred) {
		boolean ret = false;
		
		cred_dao.
		
		
		return ret;
	}*/
	
	public boolean validarCredenciales(String nombreUsuario, String contrasenia) {
	    // Validar que los campos no estén vacíos
	    if (nombreUsuario == null || contrasenia == null || nombreUsuario.isEmpty() || contrasenia.isEmpty()) {
	        return false;
	    }

	    // Verificar si es el usuario administrador
	    if (nombreUsuario.equals(Propiedades.get("usuarioAdmin"))
	            && contrasenia.equals(Propiedades.get("passwordAdmin"))) {
	        return true;
	    }

	    // Validar credenciales en la base de datos
	    CredencialesDAO credencialesDAO = new CredencialesDAO();
	    String perfil = credencialesDAO.getPerfil(nombreUsuario, contrasenia);

	    // Si el perfil no es nulo ni vacío, las credenciales son válidas
	    return perfil != null && !perfil.isEmpty();
	}

}
