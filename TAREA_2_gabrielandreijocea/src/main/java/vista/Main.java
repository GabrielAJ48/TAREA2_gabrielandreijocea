package vista;

import java.util.Scanner;

import entidades.*;
import servicios.CredencialesService;

public class Main {

	public static void main(String[] args) {
		Scanner leer = new Scanner(System.in);
		CredencialesService credserv = new CredencialesService();

		boolean confirmarSalida = false;
		int op = 0;

		Credenciales cred = new Credenciales();

		do {
			System.out.println("======================================");
			System.out.println("Usuario: ");
			System.out.println("Perfil: ");
			System.out.println("======================================");

			 //switch(credserv.tomarPerfil(null, null))
		} while (!confirmarSalida);

		/*if (credserv.comprobarCredenciales(cred)) {
			System.out.println("Credenciales correctas!");
		}
		else
			System.out.println("Credenciales incorrectas!");*/
	}
}
