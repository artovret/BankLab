import database.MemDataBaseFile;
import httpServer.SimpleServer;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        new MemDataBaseFile().initDb();
        SimpleServer.createConnection();
    }
}
