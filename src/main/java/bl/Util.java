package bl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    //private static final String URL = "jdbc:h2:/Users/u19224031/IdeaProjects/BankSystems/db";
    //private static final String URL = "jdbc:h2:~/test";
    private static final String URL = "jdbc:h2:mem:clientDatabase;DB_CLOSE_DELAY=-1";
    private static final String USER = "Gref";
    private static final String PASSWORD = "123";
    private static final String DRIVER = "org.h2.Driver";

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
