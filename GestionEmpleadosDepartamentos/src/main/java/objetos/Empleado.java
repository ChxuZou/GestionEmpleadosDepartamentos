package objetos;

import javax.management.InvalidAttributeValueException;

/**
 * @author Rubén Arranz, Sebastián Castaño, Chenxuan Zou
 */
public class Empleado {
	private Integer id;
	private String nombre;
	private Double salario;
	private Departamento departamento;

	private void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "El empleado \"" + getNombre() + "\" con id: [" + getId() + "] cobra: " + getSalario()
				+ ((departamento != null) ? (" pertenece al departamento de " + departamento.getNombre() + " con id: [" + departamento.getId() + "] ")
						: (" no pertenece a ningún departamento"));
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) throws InvalidAttributeValueException {
		if (salario >= 0) {
			this.salario = salario;
		} else {
			throw new InvalidAttributeValueException("El salario no puede ser negativo.\n");
		}

	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public int getId() {
		return id;
	}

	public Empleado(Integer id, String nombre, Double salario, Departamento departamento)
			throws InvalidAttributeValueException {
		setId(id);
		setNombre(nombre);
		setSalario(salario);
		setDepartamento(departamento);
	}

	public Empleado(String nombre, Double salario, Departamento departamento) throws InvalidAttributeValueException {
		this(0, nombre, salario, departamento);
	}

}