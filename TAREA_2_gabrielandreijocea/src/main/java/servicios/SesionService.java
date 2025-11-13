package servicios;

import dao.CredencialesDAO;
import dao.PersonaDAO;
import entidades.Sesion;

public class SesionService {

	PersonaDAO perdao = new PersonaDAO();
	CredencialesDAO credao = new CredencialesDAO();
	
	public boolean iniciarSesion(String usuario, String contrasenia) {
		boolean ok = false;
		
		if (usuario.equals(Propiedades.get("usuarioAdmin")) && contrasenia.equals(Propiedades.get("contraseniaAdmin"))) {
			Sesion.setNombre("Administrador");
			Sesion.setPerfil("ADMIN");
		} else {
			String nombrePersona = perdao.getNombrePersona(usuario, contrasenia);
			Sesion.setNombre(nombrePersona);
			
			String perfil = credao.getPerfil(usuario, contrasenia);
			Sesion.setPerfil(perfil);
			
			ok=true;
		}
		
		return ok;
	}
	
	//public boolean asignar
}
