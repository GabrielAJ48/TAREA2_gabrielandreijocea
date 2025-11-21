package servicios;

import dao.CredencialesDAO;
import dao.PersonaDAO;
import entidades.Perfiles;
import entidades.Sesion;
import utilidades.Propiedades;

public class SesionService {

	PersonaDAO perdao = new PersonaDAO();
	CredencialesDAO credao = new CredencialesDAO();
	
	public boolean iniciarSesion(String usuario, String contrasenia, Sesion sesion) {
		boolean ok = false;
		
		if (usuario.equals(Propiedades.get("usuarioAdmin")) && contrasenia.equals(Propiedades.get("passwordAdmin"))) {
			sesion.setNombre("Administrador");
			sesion.setPerfil(Perfiles.ADMIN);
		} else {
			String nombrePersona = perdao.getNombrePersona(usuario, contrasenia);
			sesion.setNombre(nombrePersona);
			
			String perfil = credao.getPerfil(usuario, contrasenia);
			sesion.setPerfil(Perfiles.valueOf(perfil));
			
			ok=true;
		}
		
		return ok;
	}
	
	public boolean cerrarSesion(Sesion sesion) {
		boolean ok = false;
		
		sesion.setNombre("Invitado");
		sesion.setPerfil(Perfiles.INVITADO);
		
		ok = true;
		return ok;
	}
}
