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
import servicios.*;
import utilidades.*;

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
					} while (confirmacion.equals("S") && confirmacion.equals("N"));
					
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
				System.out.println("5. Modificar datos de espectáculo");
				System.out.println("6. Cerrar sesión");
				System.out.println("7. Salir");
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
				    System.out.println("=== VER ESPECTÁCULOS COMPLETOS ===");

				    Map<Long, String> listaEsp = coordServ.obtenerTodosEspectaculos();
				    
				    if (listaEsp.isEmpty()) {
				        System.out.println("No hay espectáculos registrados.");
				        break;
				    }

				    System.out.println("--- Espectáculos Disponibles ---");
				    listaEsp.forEach((id, nom) -> System.out.println("ID: " + id + " | " + nom));

				    System.out.println("Introduzca el ID del espectáculo a visualizar:");
				    long idVer = -1;
				    try {
				        idVer = leer.nextLong();
				        leer.nextLine();
				    } catch (Exception e) {
				        leer.nextLine();
				        System.out.println("Entrada inválida.");
				        break;
				    }

				    if (!listaEsp.containsKey(idVer)) {
				        System.out.println("ID no encontrado.");
				        break;
				    }

				    Espectaculo eCompleto = espServ.obtenerDetalleEspectaculo(idVer);
				    
				    if (eCompleto == null) {
				        System.out.println("Error al cargar el detalle del espectáculo.");
				        break;
				    }
				    Coordinacion coordInfo = coordServ.obtenerDatosDelCoordinador(eCompleto.getIdCoord());
				    
				    String nombreCoord =coordInfo.getNombre();
				    String emailCoord = coordInfo.getEmail();
				    String esSenior = coordInfo.isSenior() ? "SÍ" : "NO";

				    System.out.println("#################################################");
				    System.out.println("          INFORME DE ESPECTÁCULO");
				    System.out.println("#################################################");
				    System.out.println("TITULO:       " + eCompleto.getNombre().toUpperCase());
				    System.out.println("FECHAS:       Del " + eCompleto.getFechaInicio() + " al " + eCompleto.getFechaFin());
				    System.out.println("-------------------------------------------------");
				    System.out.println("DIRIGIDO POR: " + nombreCoord);
				    System.out.println("CONTACTO:     " + emailCoord);
				    System.out.println("SENIOR:       " + esSenior);
				    System.out.println("-------------------------------------------------");
				    System.out.println("              PROGRAMA DE NÚMEROS");
				    System.out.println("-------------------------------------------------");

				    if (eCompleto.getNumeros().isEmpty()) {
				        System.out.println(" (No hay números registrados aún)");
				    } else {
				        for (Numero n : eCompleto.getNumeros()) {
				            System.out.println("ORDEN " + n.getOrden() + ": \"" + n.getNombre() + "\" (" + n.getDuracion() + " min)");
				            
				            Set<Artista> artistasNum = espServ.obtenerArtistasDelNumero(n.getId());
				            
				            System.out.println("   Participantes:");
				            if (artistasNum.isEmpty()) {
				                System.out.println("      (Sin artistas asignados)");
				            } else {
				                for (Artista a : artistasNum) {
				                    String mostrarArtista = "      - " + a.getNombre();
				                    
				                    if (a.getApodo() != null && !a.getApodo().isEmpty()) {
				                        mostrarArtista += " alias '" + a.getApodo() + "'";
				                    }
				                    System.out.println(mostrarArtista);
				                }
				            }
				            System.out.println();
				        }
				    }
				    System.out.println("#################################################");
				    break;
					
				case 2:
				    System.out.println("CREAR NUEVO ESPECTÁCULO");
				    leer = new Scanner(System.in);

				    String nombreEsp = "", fIni = "", fFin = "";
				    boolean fechasOk = false;
				    
				    while (!fechasOk) {
				        System.out.println("Nombre del espectáculo:");
				        nombreEsp = leer.nextLine();
				        System.out.println("Fecha Inicio (dd/MM/yyyy):");
				        fIni = leer.nextLine();
				        System.out.println("Fecha Fin (dd/MM/yyyy):");
				        fFin = leer.nextLine();

				        if (espServ.comprobarFechasEspectaculo(fIni, fFin) && espServ.comprobarNombreEspectaculo(nombreEsp)) {
				            fechasOk = true;
				        } else {
				            System.out.println("Datos incorrectos. Inténtelo de nuevo.");
				        }
				    }
				    
				    Espectaculo nuevoEsp = new Espectaculo();
				    nuevoEsp.setNombre(nombreEsp);
				    nuevoEsp.setFechaInicio(LocalDate.parse(fIni, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				    nuevoEsp.setFechaFin(LocalDate.parse(fFin, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

				    long idCoordSel = -1;
				    boolean coordValido = false;
				    
				    Map<Long, String> coords = coordServ.obtenerListaCoordinadores();
				    System.out.println("--- Coordinadores Disponibles ---");
				    coords.forEach((id, nom) -> System.out.println("ID: " + id + " | " + nom));

				    while (!coordValido) {
				        System.out.println("Introduzca el ID de Coordinación:");
				        try {
				            idCoordSel = leer.nextLong();
				            leer.nextLine();

				            if (coords.containsKey(idCoordSel)) {
				                coordValido = true;
				                nuevoEsp.setIdCoord(idCoordSel);
				            } else {
				                System.out.println("El ID introducido no corresponde a un coordinador de la lista.");
				            }
				        } catch (InputMismatchException e) {
				            System.out.println("Debe introducir un número.");
				            leer.nextLine();
				        }
				    }

				    List<Numero> listaNumeros = new java.util.ArrayList<>();
				    boolean terminarNumeros = false;
				    int orden = 1;
				    
				    Map<Long, String> mapArtistas = artServ.listarArtistasParaSeleccion();

				    while (!terminarNumeros) {
				        System.out.println("\n--- Añadiendo Número (Orden " + orden + ") ---");
				        Numero num = new Numero();
				        num.setOrden(orden);
				        
				        boolean datosNumOk = false;
				        while (!datosNumOk) {
				            System.out.println("Nombre del número:");
				            String nomNum = leer.nextLine();
				            System.out.println("Duración (minutos, ej: 5,5):");
				            double durNum = 0;
				            try {
				                durNum = leer.nextDouble();
				                leer.nextLine();
				            } catch (InputMismatchException e) {
				                leer.nextLine();
				            }

				            String errorNum = espServ.validarDatosNumero(nomNum, durNum);
				            if (errorNum == null) {
				                num.setNombre(nomNum);
				                num.setDuracion(durNum);
				                datosNumOk = true;
				            } else {
				                System.out.println("Error: " + errorNum);
				            }
				        }

				        System.out.println("--- Artistas Disponibles ---");
				        mapArtistas.forEach((id, nom) -> System.out.println("ID: " + id + " - " + nom));
				        
				        Set<Artista> artistasSet = new HashSet<>();
				        boolean finArtistas = false;
				        
				        while (!finArtistas) {
				            System.out.println("Introduzca ID del Artista (0 para terminar selección):");
				            long idArt = -1;
				            try {
				                idArt = leer.nextLong();
				                leer.nextLine();
				            } catch (Exception e) { leer.nextLine(); }

				            if (idArt == 0) {
				                if (artistasSet.isEmpty()) {
				                    System.out.println("Debe asignar al menos un artista.");
				                } else {
				                    finArtistas = true;
				                }
				            } else {
				                if (mapArtistas.containsKey(idArt)) {
				                    Artista a = new Artista();
				                    a.setIdArt(idArt);
				                    artistasSet.add(a);
				                    System.out.println("Artista añadido al número.");
				                } else {
				                    System.out.println("Artista no encontrado.");
				                }
				            }
				        }
				        num.setArtistas(artistasSet);
				        listaNumeros.add(num);
				        orden++;

				        if (listaNumeros.size() >= 3) {
				            System.out.println("¿Desea añadir otro número? (S/N)");
				            String resp = leer.nextLine().trim().toUpperCase();
				            if (resp.equals("N")) {
				                terminarNumeros = true;
				            }
				        } else {
				            System.out.println("Lleva " + listaNumeros.size() + " números. Necesita mínimo 3.");
				        }
				    }

				    String resultadoRegistro = espServ.registrarEspectaculoCompleto(nuevoEsp, listaNumeros);
				    
				    if (resultadoRegistro == null) {
				        System.out.println("¡Espectáculo registrado correctamente en la Base de Datos!");
				    } else {
				        System.out.println("Hubo un error al registrar: " + resultadoRegistro);
				    }
				    break;
					
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
					    
					    if (tipo == 1) {
					    	credServ.insertarPerfil("COORDINACION", credencial_id);
					        boolean isSenior = false;
					        LocalDate fechaSenior = null;

					        String resp = "";
					        leer = new Scanner(System.in);
					        do {
					            System.out.println("¿Es senior? (S/N):");
					            resp = leer.nextLine().toUpperCase();

					        } while (!resp.equals("S") && !resp.equals("N"));

					        if (resp.equals("S")) {
					            isSenior = true;

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

					        String errorCoord = coordServ.registrarCoordinador(persona_id, isSenior, fechaSenior);

					        if (errorCoord != null) {
					            System.out.println(errorCoord);
					        } else {
					            System.out.println("Coordinador registrado correctamente.");
					        }
					    }
					    
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
				    
				case 5: {
				    System.out.println("=== MODIFICAR ESPECTÁCULO ===");

				    Map<Long, String> todosEspectaculos = coordServ.obtenerTodosEspectaculos(); 

				    if (todosEspectaculos.isEmpty()) {
				        System.out.println("No hay espectáculos registrados en el sistema.");
				        break;
				    }

				    System.out.println("--- Lista de Espectáculos ---");
				    todosEspectaculos.forEach((idE, nom) -> System.out.println("ID: " + idE + " | " + nom));

				    System.out.println("Introduzca el ID del espectáculo que desea gestionar:");
				    long idMod = -1; 
				    try {
				        idMod = leer.nextLong();
				        leer.nextLine();
				    } catch (Exception e) {
				        leer.nextLine();
				        System.out.println("Entrada inválida. Debe ser un número.");
				        break;
				    }

				    if (!todosEspectaculos.containsKey(idMod)) {
				        System.out.println("El ID introducido no corresponde a ningún espectáculo registrado.");
				        break;
				    }

				    System.out.println("¿Qué desea gestionar de este espectáculo?");
				    System.out.println("1. Datos Generales (Nombre/Fechas)");
				    System.out.println("2. Números y Artistas");
				    
				    int subOp = 0;
				    try {
				        subOp = leer.nextInt();
				        leer.nextLine();
				    } catch (Exception e) { leer.nextLine(); }

				    switch (subOp) {
				        case 1: 
				            Espectaculo espToMod = espServ.obtenerDetalleEspectaculo(idMod);
				            if (espToMod != null) {
				                System.out.println("Deje vacío y pulse Enter para mantener el valor actual.");
				                
				                System.out.println("Nombre actual [" + espToMod.getNombre() + "]:");
				                String newNom = leer.nextLine().trim();
				                if (!newNom.isEmpty()) espToMod.setNombre(newNom);
				                
				                System.out.println("Fecha Inicio actual [" + espToMod.getFechaInicio() + "] (dd/MM/yyyy):");
				                String newFini = leer.nextLine().trim();
				                if (!newFini.isEmpty()) {
				                    try {
				                        espToMod.setFechaInicio(LocalDate.parse(newFini, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				                    } catch (Exception e) { System.out.println("Fecha inválida. Se mantiene la anterior."); }
				                }

				                System.out.println("Fecha Fin actual [" + espToMod.getFechaFin() + "] (dd/MM/yyyy):");
				                String newFfin = leer.nextLine().trim();
				                if (!newFfin.isEmpty()) {
				                     try {
				                        espToMod.setFechaFin(LocalDate.parse(newFfin, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				                    } catch (Exception e) { System.out.println("Fecha inválida. Se mantiene la anterior."); }
				                }

				                String resUpdate = espServ.modificarEspectaculo(espToMod);
				                System.out.println(resUpdate == null ? "Datos generales actualizados." : "Error: " + resUpdate);
				            }
				            break;

				        case 2:
				            Set<Numero> numeros = espServ.obtenerNumerosDeEspectaculo(idMod);
				            
				            if (numeros.isEmpty()) {
				                System.out.println("Este espectáculo no tiene números registrados.");
				                break;
				            }

				            System.out.println("--- Lista de Números ---");
				            numeros.forEach(n -> System.out.println("ID: " + n.getId() + " | Orden: " + n.getOrden() + " | " + n.getNombre()));

				            System.out.println("Introduzca el ID del NÚMERO a gestionar:");
				            long idNumMod = -1;
				            try {
				                idNumMod = leer.nextLong();
				                leer.nextLine();
				            } catch (Exception e) { leer.nextLine(); }

				            Numero numSeleccionado = null;
				            for (Numero n : numeros) {
				                if (n.getId() == idNumMod) {
				                    numSeleccionado = n;
				                    break;
				                }
				            }

				            if (numSeleccionado != null) {
				                System.out.println("=== Gestionando Número: " + numSeleccionado.getNombre() + " ===");
				                System.out.println("1. Editar Nombre/Duración");
				                System.out.println("2. Gestionar Artistas (Añadir/Quitar)");
				                System.out.println("3. Volver");
				                
				                int opNum = 0;
				                try {
				                    opNum = leer.nextInt();
				                    leer.nextLine();
				                } catch (Exception e) { leer.nextLine(); }
				                
				                switch(opNum) {
				                    case 1:
				                        System.out.println("Deje vacío para mantener valor.");
				                        System.out.println("Nuevo Nombre:");
				                        String nn = leer.nextLine().trim();
				                        if (!nn.isEmpty()) numSeleccionado.setNombre(nn);

				                        System.out.println("Nueva Duración actual [" + numSeleccionado.getDuracion() + "]:");
				                        String durStr = leer.nextLine().trim();
				                        if (!durStr.isEmpty()) {
				                            try {
				                                double nd = Double.parseDouble(durStr);
				                                numSeleccionado.setDuracion(nd);
				                            } catch (NumberFormatException e) { 
				                                System.out.println("Formato incorrecto.");
				                            }
				                        }
				                        String resNum = espServ.modificarNumero(numSeleccionado);
				                        System.out.println(resNum == null ? "Datos actualizados." : resNum);
				                        break;

				                    case 2:
				                        boolean finArt = false;
				                        while(!finArt) {
				                            Set<Artista> artsAsignados = espServ.obtenerArtistasDelNumero(numSeleccionado.getId());
				                            
				                            System.out.println("-- Artistas participando actualmente --");
				                            if (artsAsignados.isEmpty()) {
				                                System.out.println("(Ninguno)"); 
				                            } else {
				                                artsAsignados.forEach(a -> System.out.println("ID: " + a.getIdArt() + " | " + a.getNombre() + (a.getApodo() != null ? " ("+a.getApodo()+")" : "")));
				                            }

				                            System.out.println("\nEscoja una opción:");
				                            System.out.println("1. Añadir artista");
				                            System.out.println("2. Quitar artista");
				                            System.out.println("3. Volver");
				                            
				                            int opArt = 0;
				                            try {
				                                opArt = leer.nextInt();
				                                leer.nextLine();
				                            } catch (Exception e) { leer.nextLine(); }
				                            
				                            if (opArt == 1) {
				                                Map<Long, String> todosArtistas = artServ.listarArtistasParaSeleccion();
				                                System.out.println("--- Catálogo de Artistas ---");
				                                todosArtistas.forEach((id, nom) -> System.out.println("ID: " + id + " - " + nom));
				                                
				                                System.out.println("ID del artista a AÑADIR:");
				                                long idAdd = leer.nextLong();
				                                leer.nextLine();
				                                
				                                String resAdd = espServ.agregarArtistaANumeroExistente(numSeleccionado.getId(), idAdd);
				                                System.out.println(resAdd == null ? ">> Artista añadido." : ">> Error: " + resAdd);
				                                
				                            } else if (opArt == 2) {
				                                System.out.println("ID del artista a QUITAR (de la lista de participantes):");
				                                long idRem = leer.nextLong();
				                                leer.nextLine();
				                                
				                                String resRem = espServ.quitarArtistaDeNumero(numSeleccionado.getId(), idRem);
				                                System.out.println(resRem == null ? ">> Artista eliminado del número." : ">> Error: " + resRem);
				                                
				                            } else {
				                                finArt = true;
				                            }
				                        }
				                        break;
				                        
				                    default: break;
				                }
				            } else {
				                System.out.println("ID de número no encontrado en este espectáculo.");
				            }
				            break;

				        default:
				            System.out.println("Opción inválida");
				    }
				} break;
				    
				case 6: sesiServ.cerrarSesion(sesion);
						break;
						
				case 7: 
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
				break;
			
			case COORDINACION: 
				System.out.println("Escoja una opción:");
				System.out.println("1. Ver espectáculos completos");
				System.out.println("2. Crear espectáculo");
				System.out.println("3. Modificar datos de espectáculo");
				System.out.println("4. Cerrar sesión");
				System.out.println("5. Salir");
				
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
				    System.out.println("=== VER ESPECTÁCULOS COMPLETOS ===");

				    Map<Long, String> listaEsp = coordServ.obtenerTodosEspectaculos();
				    
				    if (listaEsp.isEmpty()) {
				        System.out.println("No hay espectáculos registrados.");
				        break;
				    }

				    System.out.println("--- Espectáculos Disponibles ---");
				    listaEsp.forEach((id, nom) -> System.out.println("ID: " + id + " | " + nom));

				    System.out.println("Introduzca el ID del espectáculo a visualizar:");
				    long idVer = -1;
				    try {
				        idVer = leer.nextLong();
				        leer.nextLine();
				    } catch (Exception e) {
				        leer.nextLine();
				        System.out.println("Entrada inválida.");
				        break;
				    }

				    if (!listaEsp.containsKey(idVer)) {
				        System.out.println("ID no encontrado.");
				        break;
				    }

				    Espectaculo eCompleto = espServ.obtenerDetalleEspectaculo(idVer);
				    
				    if (eCompleto == null) {
				        System.out.println("Error al cargar el detalle del espectáculo.");
				        break;
				    }
				    Coordinacion coordInfo = coordServ.obtenerDatosDelCoordinador(eCompleto.getIdCoord());
				    
				    String nombreCoord =coordInfo.getNombre();
				    String emailCoord = coordInfo.getEmail();
				    String esSenior = coordInfo.isSenior() ? "SÍ" : "NO";

				    System.out.println("#################################################");
				    System.out.println("          INFORME DE ESPECTÁCULO");
				    System.out.println("#################################################");
				    System.out.println("TITULO:       " + eCompleto.getNombre().toUpperCase());
				    System.out.println("FECHAS:       Del " + eCompleto.getFechaInicio() + " al " + eCompleto.getFechaFin());
				    System.out.println("-------------------------------------------------");
				    System.out.println("DIRIGIDO POR: " + nombreCoord);
				    System.out.println("CONTACTO:     " + emailCoord);
				    System.out.println("SENIOR:       " + esSenior);
				    System.out.println("-------------------------------------------------");
				    System.out.println("              PROGRAMA DE NÚMEROS");
				    System.out.println("-------------------------------------------------");

				    if (eCompleto.getNumeros().isEmpty()) {
				        System.out.println(" (No hay números registrados aún)");
				    } else {
				        for (Numero n : eCompleto.getNumeros()) {
				            System.out.println("ORDEN " + n.getOrden() + ": \"" + n.getNombre() + "\" (" + n.getDuracion() + " min)");
				            
				            Set<Artista> artistasNum = espServ.obtenerArtistasDelNumero(n.getId());
				            
				            System.out.println("   Participantes:");
				            if (artistasNum.isEmpty()) {
				                System.out.println("      (Sin artistas asignados)");
				            } else {
				                for (Artista a : artistasNum) {
				                    String mostrarArtista = "      - " + a.getNombre();
				                    
				                    if (a.getApodo() != null && !a.getApodo().isEmpty()) {
				                        mostrarArtista += " alias '" + a.getApodo() + "'";
				                    }
				                    System.out.println(mostrarArtista);
				                }
				            }
				            System.out.println();
				        }
				    }
				    System.out.println("#################################################");
				    break;

		        case 2:
		            System.out.println("CREAR NUEVO ESPECTÁCULO");

		            String nombreEsp = "", fIni = "", fFin = "";
		            boolean fechasOk = false;

		            while (!fechasOk) {
		                System.out.println("Nombre del espectáculo:");
		                nombreEsp = leer.nextLine();
		                System.out.println("Fecha Inicio (dd/MM/yyyy):");
		                fIni = leer.nextLine();
		                System.out.println("Fecha Fin (dd/MM/yyyy):");
		                fFin = leer.nextLine();

		                if (espServ.comprobarFechasEspectaculo(fIni, fFin) && espServ.comprobarNombreEspectaculo(nombreEsp)) {
		                    fechasOk = true;
		                } else {
		                    System.out.println("Datos incorrectos o nombre ya existente. Inténtelo de nuevo.");
		                }
		            }

		            Espectaculo nuevoEsp = new Espectaculo();
		            nuevoEsp.setNombre(nombreEsp);
		            nuevoEsp.setFechaInicio(LocalDate.parse(fIni, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		            nuevoEsp.setFechaFin(LocalDate.parse(fFin, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

		            long idPersonaLogueada = sesion.getPersona_id();
		            long idCoordinacion = coordServ.obtenerIdCoord(idPersonaLogueada);

		            if (idCoordinacion == -1) {
		                System.out.println("Error: No se encuentra tu ficha de coordinador. Contacta al Admin.");
		                break;
		            }

		            nuevoEsp.setIdCoord(idCoordinacion);
		            System.out.println(">> Espectáculo asignado automáticamente a tu perfil.");

		            List<Numero> listaNumeros = new java.util.ArrayList<>();
		            boolean terminarNumeros = false;
		            int orden = 1;

		            Map<Long, String> mapArtistas = artServ.listarArtistasParaSeleccion();

		            while (!terminarNumeros) {
		                System.out.println("\n--- Añadiendo Número (Orden " + orden + ") ---");
		                Numero num = new Numero();
		                num.setOrden(orden);

		                boolean datosNumOk = false;
		                while (!datosNumOk) {
		                    System.out.println("Nombre del número:");
		                    String nomNum = leer.nextLine();
		                    System.out.println("Duración (minutos, ej: 5,5):");
		                    double durNum = 0;
		                    try {
		                        durNum = leer.nextDouble();
		                        leer.nextLine();
		                    } catch (InputMismatchException e) {
		                        leer.nextLine();
		                    }

		                    String errorNum = espServ.validarDatosNumero(nomNum, durNum);
		                    if (errorNum == null) {
		                        num.setNombre(nomNum);
		                        num.setDuracion(durNum);
		                        datosNumOk = true;
		                    } else {
		                        System.out.println("Error: " + errorNum);
		                    }
		                }

		                System.out.println("--- Artistas Disponibles ---");
		                mapArtistas.forEach((id, nom) -> System.out.println("ID: " + id + " - " + nom));

		                Set<Artista> artistasSet = new HashSet<>();
		                boolean finArtistas = false;

		                while (!finArtistas) {
		                    System.out.println("Introduzca ID del Artista (0 para terminar selección):");
		                    long idArt = -1;
		                    try {
		                        idArt = leer.nextLong();
		                        leer.nextLine();
		                    } catch (Exception e) {
		                        leer.nextLine();
		                    }

		                    if (idArt == 0) {
		                        if (artistasSet.isEmpty()) {
		                            System.out.println("Debe asignar al menos un artista.");
		                        } else {
		                            finArtistas = true;
		                        }
		                    } else {
		                        if (mapArtistas.containsKey(idArt)) {
		                            Artista a = new Artista();
		                            a.setIdArt(idArt);
		                            artistasSet.add(a);
		                            System.out.println("Artista añadido.");
		                        } else {
		                            System.out.println("ID no encontrado.");
		                        }
		                    }
		                }
		                num.setArtistas(artistasSet);
		                listaNumeros.add(num);
		                orden++;

		                if (listaNumeros.size() >= 3) {
		                    System.out.println("¿Desea añadir otro número? (S/N)");
		                    String resp = leer.nextLine().trim().toUpperCase();
		                    if (resp.equals("N")) terminarNumeros = true;
		                } else {
		                    System.out.println("Necesita mínimo 3 números (Lleva " + listaNumeros.size() + ").");
		                }
		            }

		            String error = espServ.registrarEspectaculoCompleto(nuevoEsp, listaNumeros);
		            if (error == null) {
		                System.out.println("¡Espectáculo creado con éxito!");
		            } else {
		                System.out.println("Error al guardar: " + error);
		            }
		            break;

		        case 3:
		            System.out.println("MODIFICAR ESPECTÁCULO");
		            Map<Long, String> todosEspectaculos = coordServ.obtenerTodosEspectaculos(); 

		            if (todosEspectaculos.isEmpty()) {
		                System.out.println("No hay espectáculos registrados en el sistema.");
		                break;
		            }

		            System.out.println("--- Lista de Espectáculos ---");
		            todosEspectaculos.forEach((idE, nom) -> System.out.println("ID: " + idE + " | " + nom));

		            System.out.println("Introduzca el ID del espectáculo que desea gestionar:");
		            long idMod = -1; 
		            try {
		                idMod = leer.nextLong();
		                leer.nextLine();
		            } catch (Exception e) {
		                leer.nextLine();
		                System.out.println("Entrada inválida. Debe ser un número.");
		                break;
		            }

		            if (!todosEspectaculos.containsKey(idMod)) {
		                System.out.println("El ID introducido no corresponde a ningún espectáculo registrado.");
		                break;
		            }

		            System.out.println("¿Qué desea gestionar de este espectáculo?");
		            System.out.println("1. Datos Generales (Nombre/Fechas)");
		            System.out.println("2. Números y Artistas");
		            
		            int subOp = 0;
		            try {
		                subOp = leer.nextInt();
		                leer.nextLine();
		            } catch (Exception e) { leer.nextLine(); }

		            switch (subOp) {
		                case 1: 
		                    Espectaculo espToMod = espServ.obtenerDetalleEspectaculo(idMod);
		                    if (espToMod != null) {
		                         System.out.println("Nombre actual [" + espToMod.getNombre() + "]. Nuevo nombre (Enter para saltar):");
		                         String newNom = leer.nextLine().trim();
		                         if (!newNom.isEmpty()) espToMod.setNombre(newNom);

		                         String resUpdate = espServ.modificarEspectaculo(espToMod);
		                         System.out.println(resUpdate == null ? "Datos generales actualizados." : "Error: " + resUpdate);
		                    }
		                    break;

		                case 2:
		                    Set<Numero> numeros = espServ.obtenerNumerosDeEspectaculo(idMod);
		                    
		                    if (numeros.isEmpty()) {
		                        System.out.println("Este espectáculo no tiene números registrados.");
		                        break;
		                    }

		                    System.out.println("--- Lista de Números ---");
		                    numeros.forEach(n -> System.out.println("ID: " + n.getId() + " | Orden: " + n.getOrden() + " | " + n.getNombre()));

		                    System.out.println("Introduzca el ID del NÚMERO a gestionar:");
		                    long idNumMod = -1;
		                    try {
		                        idNumMod = leer.nextLong();
		                        leer.nextLine();
		                    } catch (Exception e) { leer.nextLine(); }

		                    long finalIdNum = idNumMod;
		                    Numero numSeleccionado = null;
		                    for (Numero n : numeros) {
		                        if (n.getId() == idNumMod) {
		                            numSeleccionado = n;
		                            break;
		                        }
		                    }

		                    if (numSeleccionado != null) {
		                        System.out.println("=== Gestionando Número: " + numSeleccionado.getNombre() + " ===");
		                        System.out.println("1. Editar Nombre/Duración");
		                        System.out.println("2. Gestionar Artistas (Añadir/Quitar)");
		                        System.out.println("3. Volver");
		                        
		                        int opNum = 0;
		                        try {
		                            opNum = leer.nextInt();
		                            leer.nextLine();
		                        } catch (Exception e) { leer.nextLine(); }
		                        
		                        switch(opNum) {
		                            case 1:
		                                System.out.println("Deje vacío para mantener valor.");
		                                System.out.println("Nuevo Nombre:");
		                                String nn = leer.nextLine().trim();
		                                if (!nn.isEmpty()) numSeleccionado.setNombre(nn);

		                                System.out.println("Nueva Duración actual [" + numSeleccionado.getDuracion() + "]:");
		                                String durStr = leer.nextLine().trim();
		                                if (!durStr.isEmpty()) {
		                                    try {
		                                        double nd = Double.parseDouble(durStr);
		                                        numSeleccionado.setDuracion(nd);
		                                    } catch (NumberFormatException e) { 
		                                    	System.out.println("Formato incorrecto.");
		                                    }
		                                }
		                                String resNum = espServ.modificarNumero(numSeleccionado);
		                                System.out.println(resNum == null ? "Datos actualizados." : resNum);
		                                break;

		                            case 2:
		                                boolean finArt = false;
		                                while(!finArt) {
		                                    Set<Artista> artsAsignados = espServ.obtenerArtistasDelNumero(finalIdNum);
		                                    System.out.println("-- Artistas participando actualmente --");
		                                    if (artsAsignados.isEmpty()) {
		                                    	System.out.println("(Ninguno)"); 
		                                    } else {
		                                    	artsAsignados.forEach(a -> System.out.println("ID: " + a.getIdArt() + " | " + a.getNombre() + (a.getApodo() != null ? " ("+a.getApodo()+")" : "")));
		                                    }

		                                    System.out.println("Escoja una opción:");
		                                    System.out.println("1. Añadir artista");
		                                    System.out.println("2. Quitar artista");
		                                    System.out.println("3. Volver");
		                                    
		                                    int opArt = 0;
		                                    try {
		                                        opArt = leer.nextInt();
		                                        leer.nextLine();
		                                    } catch (Exception e) { leer.nextLine(); }
		                                    
		                                    if (opArt == 1) {
		                                        Map<Long, String> todosArtistas = artServ.listarArtistasParaSeleccion();
		                                        System.out.println("--- Catálogo de Artistas ---");
		                                        todosArtistas.forEach((id, nom) -> System.out.println("ID: " + id + " - " + nom));
		                                        
		                                        System.out.println("ID del artista a AÑADIR:");
		                                        long idAdd = leer.nextLong();
		                                        leer.nextLine();
		                                        
		                                        String resAdd = espServ.agregarArtistaANumeroExistente(finalIdNum, idAdd);
		                                        System.out.println(resAdd == null ? ">> Artista añadido." : ">> Error: " + resAdd);
		                                        
		                                    } else if (opArt == 2) {
		                                        System.out.println("ID del artista a QUITAR (de la lista de participantes):");
		                                        long idRem = leer.nextLong();
		                                        leer.nextLine();
		                                        
		                                        String resRem = espServ.quitarArtistaDeNumero(finalIdNum, idRem);
		                                        System.out.println(resRem == null ? ">> Artista eliminado del número." : ">> Error: " + resRem);
		                                        
		                                    } else {
		                                        finArt = true;
		                                    }
		                                }
		                                break;
		                        }
		                    } else {
		                        System.out.println("ID de número no encontrado en este espectáculo.");
		                    }
		                    break;

		                default:
		                    System.out.println("Opción inválida");
		            }
		            break;

		        case 4:
		            sesiServ.cerrarSesion(sesion);
		            System.out.println("Sesión cerrada.");
		            break;

		        case 5:
		            String confirmacion = "";
		            do {
		                System.out.println("¿Está seguro que desea salir? (S/N)");
		                confirmacion = leer.nextLine().toUpperCase().trim();
		            } while (!confirmacion.equals("S") && !confirmacion.equals("N"));

		            if (confirmacion.equals("S")) {
		                System.out.println("¡Adiós!");
		                confirmarSalida = true;
		            }
		            break;

		        default:
		            System.out.println("Opción incorrecta.");
		            break;
		    }
		    break;
			
			case ARTISTA: 
				System.out.println("Escoja una opción:");
				System.out.println("1. Ver espectáculos completos");
				System.out.println("2. Ver ficha de datos");
				System.out.println("3. Cerrar sesión");
				System.out.println("4. Salir");
			
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
				    System.out.println("=== VER ESPECTÁCULOS COMPLETOS ===");

				    Map<Long, String> listaEsp = coordServ.obtenerTodosEspectaculos();
				    
				    if (listaEsp.isEmpty()) {
				        System.out.println("No hay espectáculos registrados.");
				        break;
				    }

				    System.out.println("--- Espectáculos Disponibles ---");
				    listaEsp.forEach((id, nom) -> System.out.println("ID: " + id + " | " + nom));

				    System.out.println("Introduzca el ID del espectáculo a visualizar:");
				    long idVer = -1;
				    try {
				        idVer = leer.nextLong();
				        leer.nextLine();
				    } catch (Exception e) {
				        leer.nextLine();
				        System.out.println("Entrada inválida.");
				        break;
				    }

				    if (!listaEsp.containsKey(idVer)) {
				        System.out.println("ID no encontrado.");
				        break;
				    }

				    Espectaculo eCompleto = espServ.obtenerDetalleEspectaculo(idVer);
				    
				    if (eCompleto == null) {
				        System.out.println("Error al cargar el detalle del espectáculo.");
				        break;
				    }
				    Coordinacion coordInfo = coordServ.obtenerDatosDelCoordinador(eCompleto.getIdCoord());
				    
				    String nombreCoord =coordInfo.getNombre();
				    String emailCoord = coordInfo.getEmail();
				    String esSenior = coordInfo.isSenior() ? "SÍ" : "NO";

				    System.out.println("#################################################");
				    System.out.println("          INFORME DE ESPECTÁCULO");
				    System.out.println("#################################################");
				    System.out.println("TITULO:       " + eCompleto.getNombre().toUpperCase());
				    System.out.println("FECHAS:       Del " + eCompleto.getFechaInicio() + " al " + eCompleto.getFechaFin());
				    System.out.println("-------------------------------------------------");
				    System.out.println("DIRIGIDO POR: " + nombreCoord);
				    System.out.println("CONTACTO:     " + emailCoord);
				    System.out.println("SENIOR:       " + esSenior);
				    System.out.println("-------------------------------------------------");
				    System.out.println("              PROGRAMA DE NÚMEROS");
				    System.out.println("-------------------------------------------------");

				    if (eCompleto.getNumeros().isEmpty()) {
				        System.out.println(" (No hay números registrados aún)");
				    } else {
				        for (Numero n : eCompleto.getNumeros()) {
				            System.out.println("ORDEN " + n.getOrden() + ": \"" + n.getNombre() + "\" (" + n.getDuracion() + " min)");
				            
				            Set<Artista> artistasNum = espServ.obtenerArtistasDelNumero(n.getId());
				            
				            System.out.println("   Participantes:");
				            if (artistasNum.isEmpty()) {
				                System.out.println("      (Sin artistas asignados)");
				            } else {
				                for (Artista a : artistasNum) {
				                    String mostrarArtista = "      - " + a.getNombre();
				                    
				                    if (a.getApodo() != null && !a.getApodo().isEmpty()) {
				                        mostrarArtista += " alias '" + a.getApodo() + "'";
				                    }
				                    System.out.println(mostrarArtista);
				                }
				            }
				            System.out.println();
				        }
				    }
				    System.out.println("#################################################");
				    break;
				    
				case 2: 
				    
				case 3:
		            sesiServ.cerrarSesion(sesion);
		            System.out.println("Sesión cerrada.");
		            break;

		        case 4:
		            String confirmacion = "";
		            do {
		                System.out.println("¿Está seguro que desea salir? (S/N)");
		                confirmacion = leer.nextLine().toUpperCase().trim();
		            } while (!confirmacion.equals("S") && !confirmacion.equals("N"));

		            if (confirmacion.equals("S")) {
		                System.out.println("¡Adiós!");
		                confirmarSalida = true;
		            }
		            break;

		        default:
		            System.out.println("Opción incorrecta.");
		            break;
				
				}
					break;

			}
		} while (!confirmarSalida);

		leer.close();
	}
}
