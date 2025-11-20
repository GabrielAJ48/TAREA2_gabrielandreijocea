package vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import entidades.*;
import servicios.ArtistaService;
import servicios.CoordinadorService;
import servicios.CredencialesService;
import servicios.PersonaService;
import servicios.SesionService;
import utilidades.GestorNacionalidades;
import utilidades.Propiedades;

public class Main {

	public static void main(String[] args) {
		GestorNacionalidades.cargarPaises(Propiedades.get("ficheronacionalidades"));
		Scanner leer = new Scanner(System.in);
		CredencialesService credServ = new CredencialesService();
		PersonaService perServ = new PersonaService();
		SesionService sesiServ = new SesionService();
		CoordinadorService coordServ = new CoordinadorService();
		ArtistaService artServ = new ArtistaService();

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
					leer = new Scanner(System.in);
					op = leer.nextInt();
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
					leer = new Scanner(System.in);
					String nombreUsuario = leer.nextLine();
					System.out.println("Introduzca su contraseña:");
					leer = new Scanner(System.in);
					String contrasenia = leer.nextLine();

					if (credServ.validarCredencialesLogin(nombreUsuario, contrasenia)) {
						sesiServ.iniciarSesion(nombreUsuario, contrasenia);
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
					leer = new Scanner(System.in);
					op = leer.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Entrada inválida. Introduzca un número válido.");
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
							nombre = leer.nextLine().trim();
							
							System.out.println("Introduzca el email");
							leer = new Scanner(System.in);
							email = leer.nextLine().trim();
							
							System.out.println("Lista de países:");
						    Map<String,String> listaPaises = GestorNacionalidades.listaPaises();
						    listaPaises.forEach((id, nombrePais) -> System.out.println(id+" - "+nombrePais));
						    System.out.println("Introduzca el ID del país de la persona (Ej: ES):");
						    leer = new Scanner(System.in);
						    nacionalidad = leer.nextLine().trim().toUpperCase();
							
							validacionPersona = perServ.validarPersona(nombre, email, nacionalidad);
							
							if (validacionPersona == null) {
								persona_id = perServ.insertarPersona(nombre, email, nacionalidad);
								System.out.println(persona_id);
							} else {
								System.out.println(validacionPersona);
							}
						} while (validacionPersona != null);
						long credencial_id = -1;
						do {
							System.out.println("Introduzca el nombre de usuario");
							leer = new Scanner(System.in);
							nombreUsuario = leer.nextLine().trim();
							System.out.println("Introduzca la contraseña");
							leer = new Scanner(System.in);
							contraseña = leer.nextLine().trim();
							
							validacionCred = credServ.validarCredencialesRegistro(nombreUsuario, contraseña);
							
							if (validacionCred == null) {
								credencial_id = credServ.insertarCredenciales(nombreUsuario, contraseña, persona_id);
								System.out.println(credencial_id);
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
					    
					    //COORDINACION
					    if (tipo == 1) {
					    	credServ.insertarPerfil("COORDINACION", credencial_id);
					        boolean esSenior = false;
					        LocalDate fechaSenior = null;

					        String resp;
					        do {
					            System.out.println("¿Es senior? (S/N):");
					            resp = leer.nextLine().toUpperCase();

					        } while (!resp.equals("S") && !resp.equals("N"));

					        if (resp.equals("S")) {
					            esSenior = true;

					            boolean fechaOk = false;
					            do {
					                System.out.println("Introduzca la fecha (DD/MM/YYYY):");
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
					    
					    //ARTISTA
					    if (tipo == 2) {
					    	credServ.insertarPerfil("ARTISTA", credencial_id);
					        String resp = "";
					        String apodo = null;
					        
					        leer = new Scanner(System.in);
					        do {
					            System.out.println("¿Tiene apodo? (S/N):");
					            resp = leer.nextLine().toUpperCase();
					        } while (!resp.equals("S") && !resp.equals("N"));

					        if (resp.equals("S")) {
					            System.out.println("Introduzca el apodo:");
					            leer = new Scanner(System.in);
					            apodo = leer.nextLine();
					        }
					        
					        long artista_id = artServ.insertarArtista(persona_id, apodo);
					        
					        Set<String> especialidades = new HashSet<>();
					        boolean valido = false;

					        do {
					            System.out.println("Introduzca las especialidades (separadas por coma):");
					            System.out.println("Opciones: ACROBACIA, HUMOR, MAGIA, EQUILIBRISMO, MALABARISMO");

					            String especialidadesStr = leer.nextLine().toUpperCase().trim();

					            String[] partes = especialidadesStr.split(",");

					            for (String e : partes) {
					            	if (!e.isEmpty()) {
					                    especialidades.add(e);
					                }
					            }
					            
					            Set<String> error = artServ.validarEspecialidades(especialidades);

					            if (error != null) {
					            	System.out.println("Errores:");
					                error.forEach(err -> System.out.println(err));
					                especialidades.clear();
					            } else {
					            	valido = true;
					            	boolean insertarEspecialidades = artServ.insertarEspecialidades(artista_id, especialidades);
					            	System.out.println("Artista registrado correctamente");
					            }
					        } while (!valido);
					    }
					}
				break;
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
