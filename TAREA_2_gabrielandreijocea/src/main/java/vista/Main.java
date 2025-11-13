package vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

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

		Credenciales cred = new Credenciales();
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

					if (credserv.validarCredenciales(nombreUsuario, contrasenia)) {
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
			
			case "ADMIN": System.out.println("Eres Admin");
				break;
			
			case "COORDINACION": System.out.println("Eres coord");
				break;
			
			case "ARTISTA": System.out.println("Eres artista");
				break;

			}
		} while (!confirmarSalida);

		leer.close();
	}
}
