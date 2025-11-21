package vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
import servicios.EspectaculoService;
import servicios.PersonaService;
import servicios.SesionService;
import utilidades.GestorNacionalidades;
import utilidades.Propiedades;

public class Main {

	public static void main(String[] args) {
		GestorNacionalidades.cargarPaises(Propiedades.get("ficheronacionalidades"));
		Scanner leer = new Scanner(System.in);
		Sesion sesion = new Sesion();
		CredencialesService credServ = new CredencialesService();
		PersonaService perServ = new PersonaService();
		SesionService sesiServ = new SesionService();
		CoordinadorService coordServ = new CoordinadorService();
		ArtistaService artServ = new ArtistaService();
		EspectaculoService espServ = new EspectaculoService();

		boolean confirmarSalida = false;
		int op = 0;

		do {
			System.out.println("======================================");
			System.out.println("Usuario: " + sesion.getNombre());
			System.out.println("Perfil: " + sesion.getPerfil());
			System.out.println("======================================");

			switch (sesion.getPerfil()) {

			case INVITADO: {
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
					List<Espectaculo> espectaculos = espServ.obtenerEspectaculos();

			    if (espectaculos.isEmpty()) {
			        System.out.println("No hay espectáculos registrados.");
			    } else {
			        System.out.println("====== LISTA DE ESPECTÁCULOS ======");

			        for (Espectaculo e : espectaculos) {
			            System.out.println("ID: " + e.getId());
			            System.out.println("Nombre: " + e.getNombre());
			            System.out.println("Fecha Inicio: " + e.getFechaInicio());
			            System.out.println("Fecha Fin: " + e.getFechaFin());
			            System.out.println("---------------------------------");
			        }
			    }
			    break;
			    
				case 2:
					System.out.println("Introduzca su nombre de usuario:");
					leer = new Scanner(System.in);
					String nombreUsuario = leer.nextLine();
					System.out.println("Introduzca su contraseña:");
					leer = new Scanner(System.in);
					String contrasenia = leer.nextLine();

					if (credServ.validarCredencialesLogin(nombreUsuario, contrasenia)) {
						sesiServ.iniciarSesion(nombreUsuario, contrasenia, sesion);
					}
					break;
				case 3:
					String confirmacion = "";
					leer = new Scanner(System.in);
				do {
					System.out.println("¿Está seguro que desea salir? (S/N)");
					confirmacion = leer.nextLine().toUpperCase().trim();
				} while (!confirmacion.equals("S") || !confirmacion.equals("N"));
					if (confirmacion.equals("S")) {
						System.out.println("Adios!!");
						confirmarSalida = true;
					}
					break;
				default:
					System.out.println("Escoja una opción válida (1-3)");
					break;
				}
				break;
			}
			
			case ADMIN: {
				System.out.println("Escoja una opción:");
				System.out.println("1. Ver espectáculos completos");
				System.out.println("2. Crear nuevo espectáculo");
				System.out.println("3. Registrar persona");
				System.out.println("4. Modificar datos de personas");
				System.out.println("5. Cerrar sesión");
				System.out.println("6. Salir");
				leer = new Scanner(System.in);
				try {
					leer = new Scanner(System.in);
					op = leer.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Entrada inválida. Introduzca un número válido.");
					op = -1;
				}

				switch (op) {
				
				case 1:
					
				case 2: 
					
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

					        String resp = "";
					        leer = new Scanner(System.in);
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
					    break;
					    
				case 4:
				    boolean finModificacion = false;
				    int opMod = 0;
				    long persona_idMod = 0;
				    Perfiles perfilMod = null;

				    System.out.println("Lista de personas:");
				    Map<Long, String> listaPersonas = perServ.getListaPersonas();
				    System.out.println("ID - Nombre");
				    listaPersonas.forEach((idp, nom) -> System.out.println(idp + " - " + nom));

				    System.out.println("Introduzca el ID de la persona que desea modificar:");
				    leer = new Scanner(System.in);
				    persona_idMod = leer.nextLong();

				    if (!perServ.existePersona(persona_idMod)) {
				        System.out.println("No existe ninguna persona con ese ID.");
				        break;
				    }

				    perfilMod = credServ.tomarPerfil(persona_idMod);

				    do {
				        System.out.println("====== MODIFICAR PERSONA ======");
				        System.out.println("1. Modificar datos personales");
				        System.out.println("2. Modificar datos profesionales");
				        System.out.println("3. Volver al menú anterior");

				        leer = new Scanner(System.in);

				        try {
				            opMod = leer.nextInt();
				        } catch (InputMismatchException e) {
				            System.out.println("Ingrese un número válido.");
				            opMod = -1;
				        }

				        switch (opMod) {
				        
				        case 1:
				            boolean finModPers = false;

				            while (!finModPers) {
				                System.out.println("---- Datos personales ----");
				                System.out.println("1. Modificar nombre");
				                System.out.println("2. Modificar email");
				                System.out.println("3. Modificar nacionalidad");
				                System.out.println("4. Volver");

				                leer = new Scanner(System.in);
				                int opModPers = leer.nextInt();
				                leer.nextLine();

				                switch (opModPers) {

				                case 1:
				                    System.out.println("Introduzca el nuevo nombre:");
				                    String newNombre = leer.nextLine().trim();
				                    String errNom = perServ.modificarNombre(persona_idMod, newNombre);
				                    if (errNom == null)
				                        System.out.println("Nombre actualizado correctamente.");
				                    else
				                        System.out.println(errNom);
				                    break;

				                case 2:
				                    System.out.println("Introduzca el nuevo email:");
				                    String newEmail = leer.nextLine().trim();
				                    String errEmail = perServ.modificarEmail(persona_idMod, newEmail);
				                    if (errEmail == null)
				                        System.out.println("Email actualizado correctamente.");
				                    else
				                        System.out.println(errEmail);
				                    break;

				                case 3:
				                    System.out.println("Lista de países:");
				                    GestorNacionalidades.listaPaises()
				                        .forEach((idp, nom) -> System.out.println(idp + " - " + nom));

				                    System.out.println("Introduzca el ID del país:");
				                    String newPais = leer.nextLine().trim().toUpperCase();

				                    String errPais = perServ.modificarNacionalidad(persona_idMod, newPais);
				                    if (errPais == null)
				                        System.out.println("Nacionalidad actualizada correctamente.");
				                    else
				                        System.out.println(errPais);
				                    break;

				                case 4:
				                    finModPers = true;
				                    break;

				                default:
				                    System.out.println("Opción inválida");
				                }
				            }
				            break;

				        case 2:
				            if (perfilMod.equals(Perfiles.COORDINACION)) {

				                boolean finModCoord = false;

				                while (!finModCoord) {
				                    System.out.println("---- Datos de coordinación ----");
				                    System.out.println("1. Gestionar espectáculos que coordina");
				                    System.out.println("2. Marcar como senior");
				                    System.out.println("3. Volver");

				                    leer = new Scanner(System.in);
				                    int opModCoord = leer.nextInt();
				                    leer.nextLine();

				                    switch (opModCoord) {

				                    case 1:
				                        System.out.println("Espectáculos actuales del coordinador:");
				                        Map<Long, String> actuales = coordServ.obtenerEspectaculosQueCoordina(persona_idMod);
				                        actuales.forEach((idE, nom) -> System.out.println(idE + " - " + nom));

				                        System.out.println("Escoja acción:");
				                        System.out.println("1. Añadir espectáculo");
				                        System.out.println("2. Quitar espectáculo");

				                        int opEsp = leer.nextInt();
				                        leer.nextLine();

				                        if (opEsp == 1) {
				                            System.out.println("Todos los espectáculos disponibles:");
				                            Map<Long, String> todos = coordServ.obtenerTodosEspectaculos();
				                            todos.forEach((idE, nom) -> System.out.println(idE + " - " + nom));
				                            System.out.println("Introduzca ID del espectáculo a asignar:");
				                            long idAsign = leer.nextLong();

				                            String res = coordServ.asignarCoordinacion(idAsign, persona_idMod);
				                            if (res == null) System.out.println("Asignado correctamente.");
				                            else System.out.println(res);

				                        } else if (opEsp == 2) {
				                            System.out.println("Introduzca ID del espectáculo a quitar:");
				                            long idQuit = leer.nextLong();

				                            String res = coordServ.quitarCoordinacion(idQuit);
				                            if (res == null) System.out.println("Quitado correctamente.");
				                            else System.out.println(res);

				                        } else {
				                            System.out.println("Opción inválida.");
				                        }
				                        break;

				                    case 2:
				                        System.out.println("Introduzca fecha senior (dd/MM/yyyy):");
				                        String fechaStr = leer.nextLine();
				                        try {
				                            LocalDate fechaS = LocalDate.parse(fechaStr,
				                                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				                            String res = coordServ.marcarComoSenior(persona_idMod, fechaS);
				                            System.out.println(res == null ? "Actualizado correctamente." : res);

				                        } catch (Exception e) {
				                            System.out.println("Formato incorrecto.");
				                        }
				                        break;

				                    case 3:
				                        finModCoord = true;
				                        break;

				                    default:
				                        System.out.println("Opción inválida");
				                    }
				                }

				            } else {

				                boolean finModArt = false;

				                while (!finModArt) {
				                    System.out.println("---- Datos de artista ----");
				                    System.out.println("1. Modificar apodo");
				                    System.out.println("2. Gestionar especialidades");
				                    System.out.println("3. Volver");

				                    leer = new Scanner(System.in);
				                    int opModArt = leer.nextInt();
				                    leer.nextLine();

				                    switch (opModArt) {

				                    case 1:
				                        System.out.println("Introduzca nuevo apodo:");
				                        String newApo = leer.nextLine().trim();
				                        String resApo = artServ.modificarApodo(persona_idMod, newApo);
				                        System.out.println(resApo == null ? "Apodo actualizado." : resApo);
				                        break;

				                    case 2:
				                        System.out.println("Especialidades actuales:");
				                        artServ.listarEspecialidades(persona_idMod)
				                                .forEach(e -> System.out.println("- " + e));

				                        System.out.println("Opciones:");
				                        System.out.println("1. Añadir especialidad");
				                        System.out.println("2. Quitar especialidad");

				                        int opEsp = leer.nextInt();
				                        leer.nextLine();

				                        if (opEsp == 1) {
				                            System.out.println("Introduzca la especialidad a añadir:");
				                            String espAdd = leer.nextLine().trim().toUpperCase();
				                            String res = artServ.agregarEspecialidad(persona_idMod, espAdd);
				                            System.out.println(res == null ? "Añadida correctamente." : res);

				                        } else if (opEsp == 2) {
				                            System.out.println("Introduzca la especialidad a eliminar:");
				                            String espRem = leer.nextLine().trim().toUpperCase();
				                            String res = artServ.eliminarEspecialidad(persona_idMod, espRem);
				                            System.out.println(res == null ? "Eliminada correctamente." : res);

				                        } else {
				                            System.out.println("Opción inválida.");
				                        }
				                        break;

				                    case 3:
				                        finModArt = true;
				                        break;

				                    default:
				                        System.out.println("Opción inválida.");
				                    }
				                }
				            }
				            break;

				        case 3:
				            finModificacion = true;
				            break;

				        default:
				            System.out.println("Opción inválida.");
				        }

				    } while (!finModificacion);

				    break;
				    
				case 5: sesiServ.cerrarSesion(sesion);
						break;
						
				case 6: 
						String confirmacion = "";
						leer = new Scanner(System.in);
						do {
							System.out.println("¿Está seguro que desea salir? (S/N)");
							confirmacion = leer.nextLine().toUpperCase().trim();
						} while (!confirmacion.equals("S") || !confirmacion.equals("N"));
						if (confirmacion.equals("S")) {
							System.out.println("Adios!!");
							confirmarSalida = true;
						}
				break;
					}
				}
			
			case COORDINACION: System.out.println("Eres coord");
				break;
			
			case ARTISTA: System.out.println("Eres artista");
				break;

			}
		} while (!confirmarSalida);

		leer.close();
	}
}
