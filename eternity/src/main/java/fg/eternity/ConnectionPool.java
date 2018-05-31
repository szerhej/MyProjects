/*
 * Created on 30-Jan-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fg.eternity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author gxfulop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConnectionPool {

    private static ConnectionPool connectionPool = new ConnectionPool();

    /**
     *  
     */
    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        return connectionPool;
    }

    public Connection getConnection() throws ClassNotFoundException,
            SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost/c:/Gabor/db/db", "sa", "");
        conn.setAutoCommit(false);
        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
