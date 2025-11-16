package vista;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import entidades.*;
import servicios.CredencialesService;
import servicios.PersonaService;
import servicios.SesionService;

public class Main {

	public static void main(String[] args) {
		Scanner leer = new Scanner(System.in);
		CredencialesService credserv = new CredencialesService();
		PersonaService perserv = new PersonaService();
		SesionService sesiserv = new SesionService();
		
		System.out.println(credserv.tomarPerfil("andrei", "andrei"));
		System.out.println(perserv.getNombrePersona("andrei", "andrei"));

		boolean confirmarSalida = false;
		int op = 0;

		do {
			System.out.println("======================================");
			System.out.println("Usuario: " + Sesion.getNombre());
			System.out.println("Perfil: " + Sesion.getPerfil());
			System.out.println("======================================");

			switch (Sesion.getPerfil()) {

			case "INVITADO": {
				System.out.println("Escoja una opción:");
				System.out.println("1. Ver espectáculos");
				System.out.println("2. Iniciar sesión");
				System.out.println("3. Salir");
				
				try {
					op = leer.nextInt();
					leer.nextLine();
				} catch (InputMismatchException e) {
					System.out.println("Entrada inválida. Introduzca un número válido.");
					leer.nextLine();
					op = -1;
				}

				switch (op) {
				case 1:
					
					break;
				case 2:
					System.out.println("Introduzca su nombre de usuario:");
					String nombreUsuario = leer.nextLine();
					System.out.println("Introduzca su contraseña:");
					String contrasenia = leer.nextLine();

					if (credserv.validarCredencialesLogin(nombreUsuario, contrasenia)) {
						sesiserv.iniciarSesion(nombreUsuario, contrasenia);
					}
					break;
				case 3:
					confirmarSalida = true;
					break;
				default:
					System.out.println("Escoja una opción válida (1-3)");
					break;
				}
				break;
			}
			
			case "ADMIN": {
				System.out.println("Escoja una opción:");
				System.out.println("1. Ver espectáculos");
				System.out.println("2. Crear nuevo espectáculo");
				System.out.println("3. Registrar persona");
				System.out.println("4. Cerrar sesión");
				System.out.println("5. Salir");
				op = leer.nextInt();
				leer.nextLine();
				try {
					op = leer.nextInt();
					leer.nextLine();
				} catch (InputMismatchException e) {
					System.out.println("Entrada inválida. Introduzca un número válido.");
					leer.nextLine();
					op = -1;
				}

				switch (op) {
				case 3: String nombre = "";
						String email = "";
						String nacionalidad = "";
						String validacionPersona = "";
						String nombreUsuario = "";
						String contraseña = "";
						String validacionCred = "";
						long persona_id = -1;
						do {
							System.out.println("Introduzca el nombre de la persona");
							nombre = leer.nextLine();
							System.out.println("Introduzca el email");
							email = leer.nextLine();
							System.out.println("Introduzca la nacionalidad");
							nacionalidad = leer.nextLine();
							
							validacionPersona = perserv.validarPersona(nombre, email, nacionalidad);
							
							if (validacionPersona == null) {
								persona_id = perserv.insertarPersona(nombre, email, nacionalidad);
							} else {
								System.out.println(validacionPersona);
							}
						} while (validacionPersona != null);
						do {
							System.out.println("Introduzca el nombre de usuario");
							nombreUsuario = leer.nextLine();
							System.out.println("Introduzca la contraseña");
							contraseña = leer.nextLine();
							
							validacionCred = credserv.validarCredencialesRegistro(nombreUsuario, contraseña);
							
							if (validacionCred == null) {
								credserv.insertarCredenciales(nombreUsuario, contraseña, persona_id);
							} else {
								System.out.println(validacionCred);
							}	
						} while (validacionCred != null);

						System.out.println("¿Qué perfil tendrá esta persona?");
						System.out.println("1. Coordinación");
						System.out.println("2. Artista");

						
						break;
						
				}
			}
			
			case "COORDINACION": System.out.println("Eres coord");
				break;
			
			case "ARTISTA": System.out.println("Eres artista");
				break;

			}
		} while (!confirmarSalida);

		leer.close();
	}
}
