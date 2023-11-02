package objetos;

/**
 * @author Rubén Arranz, Sebastián Castaño, Chenxuan Zou
 */
public class Departamento {
	private Integer id;
	private String nombre;
	private Empleado jefe;

	
	@Override
	public String toString() {
		return "El departamento \"" + getNombre() + "\" con id: [" + getId() + "] "
				+ ((jefe != null) ? ("tiene como jefe a " + jefe.getNombre() + " con id: [" + jefe.getId() + "] ")
						: ("no tiene jefe"));
	}

	private void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Empleado getJefe() {
		return jefe;
	}

	public void setJefe(Empleado jefe) {
		this.jefe = jefe;
	}

	public int getId() {
		return id;
	}

	public Departamento(Integer id, String nombre, Empleado jefe) {
		setId(id);
		setNombre(nombre);
		setJefe(jefe);
	}

	public Departamento(String nombre, Empleado jefe) {
		this(0, nombre, jefe);
	}

}