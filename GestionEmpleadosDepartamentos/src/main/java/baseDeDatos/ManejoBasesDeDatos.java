package baseDeDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.management.InvalidAttributeValueException;
import objetos.Departamento;
import objetos.Empleado;

/**
 * @author Rubén Arranz, Sebastián Castaño, Chenxuan Zou
 */
public class ManejoBasesDeDatos {
	private Connection conn = null;

	/**
	 * Constructor de ManejoBasesDeDatos, se conecta a la base de datos y crea las
	 * tablas en caso de que no existan.
	 */
	public ManejoBasesDeDatos() {
		conn = BaseDeDatos.getConnection();
		createTables();
	}

	/**
	 * Cierra la conexión con la base de datos.
	 */
	public void close() {
		BaseDeDatos.close();
	}

	/**
	 * Añade un departamento a la base de datos, el jefe de departamento es null por
	 * defecto
	 * 
	 * @param departamento que se quiere añadir
	 * @return true en caso de haberse añadido, false en caso contrario
	 */
	public boolean addDepartamento(Departamento departamento) {
		String sentencia = "INSERT INTO departamento (nombre, jefe) VALUES (?, ?)";
		PreparedStatement sentenciaPreparada;
		boolean anadido = false;

		try {
			sentenciaPreparada = conn.prepareStatement(sentencia);

			sentenciaPreparada.setString(1, departamento.getNombre());
			sentenciaPreparada.setObject(2, null);

			anadido = sentenciaPreparada.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return anadido;
	}

	/**
	 * Añade un empleado a la base de datos, el departamento del empleado es null
	 * por defecto
	 * 
	 * @param empleado
	 * @return true en caso de haberse añadido, false en caso contrario
	 */
	public boolean addEmpleado(Empleado empleado) {
		String sentencia = "INSERT INTO empleado (nombre, salario, departamento) VALUES (?, ?, ?)";
		PreparedStatement sentenciaPreparada;
		boolean anadido = false;

		try {
			sentenciaPreparada = conn.prepareStatement(sentencia);

			sentenciaPreparada.setString(1, empleado.getNombre());
			sentenciaPreparada.setDouble(2, empleado.getSalario());
			sentenciaPreparada.setObject(3, null);

			anadido = sentenciaPreparada.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return anadido;
	}

	/**
	 * Modifica un departamento en la base de datos, si se modifica el jefe de departamento, el nuevo jefe de departamento es cambiado a este departamento
	 * 
	 * @param departamento con los datos YA MODIFICADOS
	 * @return true si se ha modificado, false en caso contrario
	 */
	public boolean modificarDepartamento(Departamento departamento) {
		String sentencia = "UPDATE departamento SET nombre = ?, jefe = ? WHERE id = ?";
		PreparedStatement sentenciaPreparada;
		boolean departamentoModificado = false;

		try {
			conn.setAutoCommit(false);
			sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setString(1, departamento.getNombre());
			sentenciaPreparada.setInt(3, departamento.getId());

			if (departamento.getJefe() != null) {
				sentenciaPreparada.setInt(2, departamento.getJefe().getId());
				departamentoModificado = updateNewJefe(departamento.getId(), departamento.getJefe().getId())
						&& sentenciaPreparada.executeUpdate() > 0;
			} else {
				sentenciaPreparada.setObject(2, null);
				departamentoModificado = sentenciaPreparada.executeUpdate() > 0;
			}

			if (departamentoModificado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return departamentoModificado;
	}

	/**
	 * El nuevo jefe de departamento es reasignado a su nuevo departamento
	 * 
	 * @param NewDepartamentoId el nuevo departamento al que es asignado
	 * @param idJefe id del empleado del que se quiere modificar su departamento
	 * @return true si se ha modificado, false en caso contrario
	 * @throws SQLException
	 */
	private boolean updateNewJefe(Integer NewDepartamentoId, Integer idJefe) throws SQLException {
		String sentencia = "UPDATE empleado SET departamento = ? WHERE id = ?";
		PreparedStatement sentenciaPreparada;
		boolean updateRealizado = false;

		try {
			conn.setAutoCommit(false);
			sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setInt(1, NewDepartamentoId);
			sentenciaPreparada.setInt(2, idJefe);

			updateRealizado = sentenciaPreparada.executeUpdate() > 0;

			if (updateRealizado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			conn.rollback();
			throw new SQLException();
		}
		return updateRealizado;
	}

	/**
	 * Modifica un empleado en la base de datos
	 * 
	 * @param empleado con los datos YA MODIFICADOS
	 * @return true si se ha modificado, false en caso contrario
	 */
	public boolean modificarEmpleado(Empleado empleado) {
		String sentencia = "UPDATE empleado SET nombre = ?, salario = ?, departamento = ? WHERE id = ?";
		PreparedStatement sentenciaPreparada;
		boolean empleadoModificado = false;
		
		try {
			conn.setAutoCommit(false);
			sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setString(1, empleado.getNombre());
			sentenciaPreparada.setDouble(2, empleado.getSalario());
			sentenciaPreparada.setInt(4, empleado.getId());
			
			if(empleado.getDepartamento()!=null) {
				sentenciaPreparada.setInt(3, empleado.getDepartamento().getId());
				
			}else {
				sentenciaPreparada.setObject(3, null);
			}
			
			empleadoModificado = sentenciaPreparada.executeUpdate() > 0;
			
			if (empleadoModificado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empleadoModificado;
	}
	
	/**
	 * Modifica un departamento poniendo el jefe de departamento a nulll
	 * 
	 * @param idOldDepartamento el id del departamento antiguo
	 * @return true si se ha modificado, false en caso contrario
	 * @throws SQLException
	 */
	public boolean updateOldDepartamento(Integer idOldDepartamento) {
		String sentencia = "UPDATE departamento SET jefe = ? WHERE id = ?";
		PreparedStatement sentenciaPreparada;
		boolean departamentoModificado= false;
		
		try {
			conn.setAutoCommit(false);
			sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setObject(1, null);
			sentenciaPreparada.setInt(2, idOldDepartamento);
			
			departamentoModificado= sentenciaPreparada.executeUpdate()>0;
			
			if(departamentoModificado) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return departamentoModificado;
	}

	/**
	 * Borra un departamento en la base de datos. Si hay empleados que pertenezcan a
	 * ese departamento, se actualizan a null en la base de datos.
	 * 
	 * @param idDepartamento que se quiere borrar.
	 * @return true si se ha borrado, false en caso contrario
	 */
	public boolean removeDepartamento(Integer idDepartamento) {
		String sentencia = "DELETE FROM departamento WHERE id = ?";
		boolean departamentoBorrado = false;

		try {
			conn.setAutoCommit(false);
			PreparedStatement sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setInt(1, idDepartamento);

			departamentoBorrado = updateDeEliminarDepartamento(idDepartamento)
					&& sentenciaPreparada.executeUpdate() > 0;

			if (departamentoBorrado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return departamentoBorrado;
	}

	/**
	 * Los empleados afectados por el borrado del departamento se actualizan a
	 * departamento = null.
	 * 
	 * @param idDepartamento que se ha borrado.
	 * @return true si se ha actualizado, false en caso contrario.
	 * @throws SQLException
	 */
	private boolean updateDeEliminarDepartamento(Integer idDepartamento) throws SQLException {
		String sentencia = "UPDATE empleado SET departamento = ? WHERE departamento = ?";
		PreparedStatement sentenciaPreparada;
		boolean tablaActualizada = false;
		try {
			conn.setAutoCommit(false);
			sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setObject(1, null);
			sentenciaPreparada.setInt(2, idDepartamento);

			tablaActualizada = sentenciaPreparada.executeUpdate() > 0;

			if (tablaActualizada) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			conn.rollback();
			throw new SQLException();
		}
		return tablaActualizada;
	}

	/**
	 * Borra un empleado en la base de datos. Si el empleado es jefe de algún
	 * departamento, se actualizan a null en la base de datos.
	 * 
	 * @param idEmpleado que se quiere borrar.
	 * @return true si se ha borrado, false en caso contrario
	 */
	public boolean removeEmpleado(Integer idEmpleado) {
		String sentencia = "DELETE FROM empleado WHERE id = ?";
		boolean empleadoBorrado = false;

		try {
			conn.setAutoCommit(false);
			PreparedStatement sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setInt(1, idEmpleado);

			empleadoBorrado = updateDeEliminarEmpleado(idEmpleado) && sentenciaPreparada.executeUpdate() > 0;

			if (empleadoBorrado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return empleadoBorrado;
	}

	/**
	 * Los departamentos afectados por el borrado del empleado se actualizan a jefe
	 * = null.
	 * 
	 * @param idJefe que se ha borrado.
	 * @return true si se ha actualizado, false en caso contrario.
	 * @throws SQLException
	 */
	private boolean updateDeEliminarEmpleado(Integer idJefe) throws SQLException {
		String sentencia = "UPDATE departamento SET jefe = ? WHERE jefe = ?";
		PreparedStatement sentenciaPreparada;
		boolean tablaActualizada = false;
		try {
			conn.setAutoCommit(false);
			sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setObject(1, null);
			sentenciaPreparada.setInt(2, idJefe);

			tablaActualizada = sentenciaPreparada.executeUpdate() > 0;

			if (tablaActualizada) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			conn.rollback();
			throw new SQLException();
		}
		return tablaActualizada;
	}

	/**
	 * Muestra todos los departamentos.
	 * 
	 * @return una cadena con todos los departamentos.
	 */
	public String mostrarDepartamentos() {
		String sentencia = "SELECT * FROM departamento";
		StringBuffer sb;
		ResultSet rs;
		try {
			sb = new StringBuffer();
			rs = conn.createStatement().executeQuery(sentencia);

			while (rs.next()) {
				Departamento dep = readDepartamento(rs, true);
				sb.append(dep);
				sb.append("\n");
			}

			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Lee un departamento a partir de un ResultSet.
	 * 
	 * @param rs      del que se sacan los datos.
	 * @param addJefe si es true, se busca al jefe para añadirlo, si es false, no se
	 *                añade un jefe.
	 * @return un objeto Departamento con los datos del ResultSet.
	 */
	private Departamento readDepartamento(ResultSet rs, boolean addJefe) {
		Integer id;
		String nombre;

		try {
			id = rs.getInt("id");
			nombre = rs.getString("nombre");
			Departamento dep = new Departamento(id, nombre, null);

			if (addJefe) {
				dep.setJefe(buscarEmpleadoPorId(rs.getInt("jefe"), false));
			}

			return dep;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Muestra todos los empleados.
	 * 
	 * @return una cadena con todos los empleados
	 */
	public String mostrarEmpleados() {
		String sentencia = "SELECT * FROM empleado";
		StringBuffer sb;
		ResultSet rs;

		try {
			sb = new StringBuffer();
			rs = conn.createStatement().executeQuery(sentencia);

			while (rs.next()) {
				Empleado emp = readEmpleado(rs, true);
				sb.append(emp.toString());
				sb.append("\n");
			}

			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Lee un empleado a partir de un ResultSet.
	 * 
	 * @param rs              del que se sacan los datos.
	 * @param addDepartamento si es true, se busca el departamento para añadirlo, si
	 *                        es false, no se añade el departamento.
	 * @return un objeto Empleado con los datos del ResultSet.
	 */
	private Empleado readEmpleado(ResultSet rs, boolean addDepartamento) {
		Integer id;
		String nombre;
		Double salario;

		try {
			id = rs.getInt("id");
			nombre = rs.getString("nombre");
			salario = rs.getDouble("salario");

			Empleado empleado = new Empleado(id, nombre, salario, null);

			if (addDepartamento) {
				empleado.setDepartamento(buscarDepartamentoPorId(rs.getInt("departamento"), false));
			}

			return empleado;

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidAttributeValueException e) {
			e.getMessage();
		}
		return null;
	}

	/**
	 * Busca un empleado en la base de datos a partir de su id.
	 * 
	 * @param id        del empleado a buscar.
	 * @param ExtraInfo true si se quiere buscar el departamento, false en caso de
	 *                  no querer buscarlo
	 * @return un objeto Empleado
	 */
	public Empleado buscarEmpleadoPorId(Integer id, boolean ExtraInfo) {
		String sentencia = "SELECT * FROM empleado WHERE id = ?";
		ResultSet rs;

		try {
			PreparedStatement sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setInt(1, id);
			rs = sentenciaPreparada.executeQuery();

			if (rs.next()) {
				Empleado empleado = readEmpleado(rs, ExtraInfo);
				return empleado;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Busca un departamento en la base de datos a partir de su id.
	 * 
	 * @param id        del departamento a buscar
	 * @param ExtraInfo true si se quiere buscar el jefe, false en caso de no querer
	 *                  buscarlo
	 * @return un objeto Empleado
	 */
	public Departamento buscarDepartamentoPorId(Integer id, boolean ExtraInfo) {

		String sentencia = "SELECT * FROM departamento WHERE id = ?";
		ResultSet rs;

		try {
			PreparedStatement sentenciaPreparada = conn.prepareStatement(sentencia);
			sentenciaPreparada.setInt(1, id);
			rs = sentenciaPreparada.executeQuery();

			if (rs.next()) {
				Departamento departamento = readDepartamento(rs, ExtraInfo);
				return departamento;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void dropTables() {

		dropTableDepartamento();
		dropTableEmpleado();
	}

	private void dropTableDepartamento() {
		String sentencia = "DELETE FROM departamento";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sentencia);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void dropTableEmpleado() {
		String sentencia = "DELETE FROM empleado";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sentencia);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void createDepartamento() {
		String sentenciaCreacion = null;

		// Si la base de datos es sqlite
		if (BaseDeDatos.typeDB.equals("sqlite")) {
			sentenciaCreacion = """
					CREATE TABLE IF NOT EXISTS departamento (
						id INTEGER PRIMARY KEY AUTOINCREMENT,
						nombre TEXT NOT NULL,
						jefe INTEGER
					)
					 """;
		}

		// Si la base de datos es mariadb
		if (BaseDeDatos.typeDB.equals("mariadb")) {
			// de tablas entre mariadb y sqlite
			sentenciaCreacion = """
						CREATE TABLE IF NOT EXISTS departamento (
						id INT PRIMARY KEY AUTO_INCREMENT,
						nombre VARCHAR(255),
						jefe INT
					)
					 """;
		}

		try {
			conn.createStatement().executeUpdate(sentenciaCreacion);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createEmpleado() {
		String sentenciaCreacion = null;

		// Si la base de datos es sqlite
		if (BaseDeDatos.typeDB.equals("sqlite")) {
			sentenciaCreacion = """
					CREATE TABLE IF NOT EXISTS empleado (
						id INTEGER PRIMARY KEY AUTOINCREMENT,
						nombre TEXT NOT NULL,
						salario REAL DEFAULT 0.0,
						departamento INTEGER
					);
					 """;
		}

		// Si la base de datos es mariadb
		if (BaseDeDatos.typeDB.equals("mariadb")) {
			sentenciaCreacion = """
					CREATE TABLE IF NOT EXISTS empleado (
						id INT PRIMARY KEY AUTO_INCREMENT,
						nombre VARCHAR(255),
						salario DOUBLE(10,2),
						departamento INT
					);
					 """;
		}

		try {
			conn.createStatement().executeUpdate(sentenciaCreacion);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createTables() {
		createDepartamento();
		createEmpleado();
	}

}
