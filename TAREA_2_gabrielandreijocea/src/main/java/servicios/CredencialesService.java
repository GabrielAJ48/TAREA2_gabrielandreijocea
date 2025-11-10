package servicios;

import dao.CredencialesDAO;
import entidades.Credenciales;

public class CredencialesService {

	CredencialesDAO cred_dao= new CredencialesDAO();
	
	public String tomarPerfil(String usuario, String contrasenia) {
		return cred_dao.getPerfil(usuario, contrasenia);
	}
	
	/*public boolean comprobarCredenciales(Credenciales cred) {
		boolean ret = false;
		
		cred_dao.
		
		
		return ret;
	}*/

}
