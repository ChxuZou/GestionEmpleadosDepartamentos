package baseDeDatos;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Rubén Arranz, Sebastián Castaño, Chenxuan Zou
 */
public class BaseDeDatos {
	private static Connection conn = null;
	public static String typeDB = null;

	/**
	 * Conexion a la base de datos
	 */
	private BaseDeDatos() {
		String driver, dsn, user, pass;
		try {
			Properties prop = new Properties();
			prop.load(new FileReader("properties.database.prop"));
			typeDB = prop.getProperty("db");
			driver = prop.getProperty("driver");
			dsn = prop.getProperty("dsn");
			user = prop.getProperty("user", "");
			pass = prop.getProperty("pass", "");
			Class.forName(driver);
			conn = DriverManager.getConnection(dsn, user, pass);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve una conexión a la base de datos
	 * 
	 * @return Conexión a la base de datos
	 */
	public static Connection getConnection() {
		if (conn == null) {
			new BaseDeDatos();
		}
		return conn;
	}

	/**
	 * Cierra la conexión
	 */
	public static void close() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
