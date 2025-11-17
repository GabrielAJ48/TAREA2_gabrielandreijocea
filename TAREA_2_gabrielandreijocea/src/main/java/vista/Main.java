package vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import entidades.*;
import servicios.ArtistaService;
import servicios.CoordinadorService;
import servicios.CredencialesService;
import servicios.PersonaService;
import servicios.SesionService;

public class Main {

	public static void main(String[] args) {
		Scanner leer = new Scanner(System.in);
		CredencialesService credserv = new CredencialesService();
		PersonaService perserv = new PersonaService();
		SesionService sesiserv = new SesionService();
		CoordinadorService coordServ = new CoordinadorService();
		ArtistaService artServ = new ArtistaService();
		
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
				leer = new Scanner(System.in);
				try {
					op = leer.nextInt();
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
							leer = new Scanner(System.in);
							nombre = leer.nextLine();
							
							System.out.println("Introduzca el email");
							leer = new Scanner(System.in);
							email = leer.nextLine();
							
							System.out.println("Introduzca la nacionalidad");
							leer = new Scanner(System.in);
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
						
						int tipo = -1;
					    do {
					        System.out.println("¿Qué perfil tendrá esta persona?");
					        System.out.println("1. Coordinación");
					        System.out.println("2. Artista");

					        try {
					            leer = new Scanner(System.in);
					        	tipo = leer.nextInt();
					        } catch (InputMismatchException e) {
					            System.out.println("Por favor introduzca 1 o 2.");
					            tipo = -1;
					        }

					    } while (tipo != 1 && tipo != 2);

					    if (tipo == 1) {

					        boolean esSenior = false;
					        LocalDate fechaSenior = null;

					        String resp = "";
					        do {
					            System.out.println("¿Es senior? (S/N):");
					            resp = leer.nextLine().toUpperCase();

					        } while (!resp.equals("S") && !resp.equals("N"));

					        if (resp.equals("S")) {
					            esSenior = true;

					            boolean fechaOk = false;
					            do {
					                System.out.println("Introduzca la fecha (DD/MM/YYYY:");
					                String fecha = leer.nextLine();

					                try {
					                    
					                    fechaSenior = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					                    fechaOk = true;

					                } catch (Exception e) {
					                    System.out.println("Formato de fecha inválido.");
					                }

					            } while (!fechaOk);
					        }

					        String errorCoord = coordServ.registrarCoordinador(persona_id, esSenior, fechaSenior);

					        if (errorCoord != null) {
					            System.out.println(errorCoord);
					        } else {
					            System.out.println("Coordinador registrado correctamente.");
					        }
					    }

					    if (tipo == 2) {
  
					        String resp = "";
					        String apodo = null;

					        do {
					            System.out.println("¿Tiene apodo? (S/N):");
					            resp = leer.nextLine().toUpperCase();

					        } while (!resp.equals("S") && !resp.equals("N"));

					        if (resp.equals("S")) {
					            System.out.println("Introduzca el apodo:");
					            apodo = leer.nextLine();
					        }

					        List<String> especialidades = null;
					        boolean valido = false;

					        do {
					            System.out.println("Introduzca las especialidades (separadas por coma):");
					            System.out.println("Opciones: ACROBACIA, HUMOR, MAGIA, EQUILIBRISMO, MALABARISMO");

					            String linea = leer.nextLine().toUpperCase();

					            String[] partes = linea.split(",");

					            especialidades = new java.util.ArrayList<>();

					            for (String e : partes) {
					                e = e.trim();

					                if (e.matches("ACROBACIA|HUMOR|MAGIA|EQUILIBRISMO|MALABARISMO")) {
					                    especialidades.add(e);
					                } else {
					                    System.out.println("Habilidad incorrecta: " + e);
					                    especialidades.clear();
					                    break;
					                }
					            }

					            if (!especialidades.isEmpty()) {
					                valido = true;
					            }

					        } while (!valido);

					        String err = artServ.registrarArtista(persona_id, apodo, especialidades);

					        if (err != null) {
					            System.out.println(err);
					        } else {
					            System.out.println("Artista registrado correctamente.");
					        }
					    }

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
