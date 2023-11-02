package menu;

import java.util.InputMismatchException;
import java.util.Scanner;
import javax.management.InvalidAttributeValueException;
import baseDeDatos.ManejoBasesDeDatos;
import objetos.Departamento;
import objetos.Empleado;

/**
 * @author Rubén Arranz, Sebastián Castaño, Chenxuan Zou
 */

public class Menu {
	public static void main(String[] args) {
		ManejoBasesDeDatos bd = new ManejoBasesDeDatos();
		Scanner sc = new Scanner(System.in);
		int menu = -1;
		do {
			try {
				menuPrincipal();
				menu = sc.nextInt();
				sc.nextLine();
				switch (menu) {
				case 0:
					cerrarBaseDeDatos(bd);
					break;
				case 1:
					System.out.println(bd.mostrarDepartamentos());
					break;
				case 2:
					System.out.println(bd.mostrarEmpleados());
					break;
				case 3:
					anadirDepartamento(bd, sc);
					break;
				case 4:
					anadirEmpleado(bd, sc);
					break;
				case 5:
					menuModificacionDepartamento(bd, sc);
					break;
				case 6:
					menuModificacionEmpleado(bd, sc);
					break;
				case 7:
					eliminarDepartamento(bd, sc);
					break;
				case 8:
					eliminarEmpleado(bd, sc);
					break;
				default:
					System.out.println("Opción inválida, por favor otro número");
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Sólo se permiten números");
				sc.nextLine();
			}

		} while (menu != 0);
	}

	private static void anadirDepartamento(ManejoBasesDeDatos bd, Scanner sc) {
		String nombre;
		boolean anadido = false;
		System.out.println("Introduce el nombre");
		nombre = sc.nextLine();
		anadido = bd.addDepartamento(new Departamento(nombre, null));
		System.out.println(anadido ? "Añadido" : "No se ha añadido");
	}

	private static void anadirEmpleado(ManejoBasesDeDatos bd, Scanner sc) {
		Double salario;
		String nombre;
		boolean anadido = false;
		System.out.println("Introduce el nombre");
		nombre = sc.nextLine();
		System.out.println("Introduce el salario");
		salario = sc.nextDouble();
		sc.nextLine();
		try {
			anadido = bd.addEmpleado(new Empleado(nombre, salario, null));
		} catch (InvalidAttributeValueException e) {
			e.printStackTrace();
		}
		System.out.println(anadido ? "Añadido" : "No se ha añadido");
	}

	private static void eliminarDepartamento(ManejoBasesDeDatos bd, Scanner sc) {
		Integer id;
		boolean remove = false;
		System.out.println("Id del departamento?");
		id = sc.nextInt();
		remove = bd.removeDepartamento(id);
		System.out.println(remove ? "Eliminado" : "No se ha podido eliminar");
	}

	private static void eliminarEmpleado(ManejoBasesDeDatos bd, Scanner sc) {
		Integer id;
		boolean remove = false;
		System.out.println("Id del empleado?");
		id = sc.nextInt();
		remove = bd.removeEmpleado(id);
		System.out.println(remove ? "Eliminado" : "No se ha podido eliminar");
	}

	private static void cerrarBaseDeDatos(ManejoBasesDeDatos bd) {
		System.out.println("Saliendo..");
		bd.close();
	}

	private static void menuPrincipal() {
		System.out.println("---------------------------------------------");
		System.out.println("1.Mostrar departamentos");
		System.out.println("2.Mostrar empleados");
		System.out.println("3.Añadir departamento");
		System.out.println("4.Añadir empleado");
		System.out.println("5.Modificar departamento");
		System.out.println("6.Modificar empleado");
		System.out.println("7.Eliminar Departamento");
		System.out.println("8.Eliminar empleado");
		System.out.println("0.Salir");
		System.out.println("---------------------------------------------");
	}

	private static void menuModificacionEmpleado(ManejoBasesDeDatos bd, Scanner sc) {
		Integer id;
		Empleado empleado;
		Integer opt;
		boolean finDelMenu = false;
		String nombre;
		double salario;
		int idDepartamento;

		try {
			System.out.println("Introduce el id del empleado a modificar: ");
			id = sc.nextInt();
			sc.nextLine();
			empleado = bd.buscarEmpleadoPorId(id, true);

			do {
				try {
					System.out.println(empleado.toString());
					System.out.println("1.Modificar el nombre");
					System.out.println("2.Modificar el salario");
					System.out.println("3.Modificar el departamento");
					System.out.println("0.Terminar");
					System.out.println("Qué desea realizar: ");

					opt = sc.nextInt();
					sc.nextLine();
					switch (opt) {
					case 1:
						System.out.println("Está modificando el nombre\nIntroduzca el nuevo nombre del empleado: ");
						nombre = sc.nextLine();
						empleado.setNombre(nombre);
						break;
					case 2:
						System.out.println("Está modificando el salario\nIntroduzca el nuevo salario: ");
						salario = sc.nextDouble();
						try {
							empleado.setSalario(salario);
						} catch (InvalidAttributeValueException e) {
							System.out.println(e.getMessage());
						}
						break;
					case 3:
						System.out.println("Está modificando el departamento\nIntroduzca el id del nuevo departamento: ");
						idDepartamento = sc.nextInt();
						sc.nextLine();

						if (empleado.getDepartamento() != null) {
							bd.updateOldDepartamento(empleado.getDepartamento().getId());
						}

						empleado.setDepartamento(bd.buscarDepartamentoPorId(idDepartamento, false));

						break;
					case 0:
						finDelMenu = true;
						break;
					default:
						System.out.println("No es una elección válida.\n");
						break;
					}
				} catch (InputMismatchException e) {
					System.out.println("Prueba a introducir un número.\n");
					sc.nextLine();
				}

			} while (finDelMenu == false);

			System.out.println(bd.modificarEmpleado(empleado) ? "Modificado" : "No modificado");

		} catch (NullPointerException e) {
			System.out.println("No se pudo encontrar el empleado.\n");
			sc.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("El id del empleado es un número.\n");
			sc.nextLine();
		}

	}

	private static void menuModificacionDepartamento(ManejoBasesDeDatos bd, Scanner sc) {
		int id;
		Departamento departamento;
		int opt;
		boolean finDelMenu = false;
		String nombre;
		int idJefe;

		try {
			System.out.println("Introduce el id del departamento a modificar: ");
			id = sc.nextInt();
			sc.nextLine();

			departamento = bd.buscarDepartamentoPorId(id, true);

			do {

				try {
					System.out.println(departamento.toString());
					System.out.println("Qué desea realizar: ");
					System.out.println("1.Modificar el nombre");
					System.out.println("2.Modificar el jefe");
					System.out.println("0.Terminar");
					opt = sc.nextInt();
					sc.nextLine();

					switch (opt) {
					case 1:
						System.out.println("Está modificando el nombre\nIntroduzca el nuevo nombre del departamento: ");
						nombre = sc.nextLine();
						departamento.setNombre(nombre);
						break;
					case 2:
						System.out.println("Está modificando el jefe\nIntroduzca el id del nuevo jefe: ");
						idJefe = sc.nextInt();
						sc.nextLine();
						departamento.setJefe(bd.buscarEmpleadoPorId(idJefe, false));
						break;
					case 0:
						finDelMenu = true;
						break;
					default:
						System.out.println("No es una elección válida.\n");
						break;
					}

				} catch (InputMismatchException e) {
					System.out.println("Prueba con un número.\n");
					sc.nextLine();
				}

			} while (finDelMenu == false);

			System.out.println(bd.modificarDepartamento(departamento) ? "Modificado" : "No modificado");

		} catch (NullPointerException e) {
			System.out.println("No se pudo encontrar el departamento.\n");
			sc.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("El id del departamento es un número.\n");
			sc.nextLine();
		}
	}
}