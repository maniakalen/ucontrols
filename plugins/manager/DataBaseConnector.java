package plugins.manager;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.*;
import java.sql.*;
public class DataBaseConnector
{
    private static Connection _connect;
    
    private static void _establishConnection(String driver, String host, String user, String pass, String schema) throws Exception {
        try {
            Class.forName(driver);
			// Setup the connection with the DB
		    DataBaseConnector._connect = DriverManager
				.getConnection("jdbc:mysql://" + host + "/" + schema + "?"
						+ "user=" + user + "&password=" + pass);

        } catch (Exception e) {
            throw e;
        }
    }
    public static boolean buildConnection(Element config) {
        try {
            String driver = config.getChild("driver").getText();
            String host = config.getChild("host").getText();
            String user = config.getChild("user").getText();
            String pass = config.getChild("pass").getText();
            String schema = config.getChild("schema").getText();
            _establishConnection(driver, host, user, pass, schema);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    public static Connection getConnection() {
        return _connect;
    }
}
