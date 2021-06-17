package database;

import bl.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

/**
 * Создание таблиц и их заполнение
 */
public class MemDataBaseFile extends Util {
    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;
    private static PreparedStatement preparedStatement;

    /**
     * метод для создания таблиц и их заполнение
     */
    public void initDb() throws SQLException {
        connection = getConnection();
        stmt = connection.createStatement();
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        try {
            Class.forName("org.h2.Driver");
            stmt.executeUpdate("DROP TABLE IF EXISTS USER,CARDS,BANK,COUNTERPARTIES");
            stmt.executeUpdate("CREATE TABLE USER\n" +
                    "(\n" +
                    "    UNUM IDENTITY PRIMARY KEY,\n" +
                    "    NAME VARCHAR(255)\n" +
                    ")");
            stmt.executeUpdate("CREATE TABLE BANK\n" +
                    "(\n" +
                    "    BNUM    IDENTITY PRIMARY KEY,\n" +
                    "    CODE    VARCHAR,\n" +
                    "    UNUM    INT,\n" +
                    "    BALANCE DECIMAL (15,2),\n" +
                    "    FOREIGN KEY (UNUM) REFERENCES USER (UNUM)\n" +
                    ");");
            stmt.executeUpdate("CREATE TABLE CARDS\n" +
                    "(\n" +
                    "    CNUM IDENTITY PRIMARY KEY,\n" +
                    "    NAME VARCHAR,\n" +
                    "    UNUM INT,\n" +
                    "    BNUM INT,\n" +
                    "    FOREIGN KEY (UNUM) REFERENCES USER(UNUM),\n" +
                    "    FOREIGN KEY (BNUM) REFERENCES BANK(BNUM)\n" +
                    ")");

            stmt.executeUpdate("CREATE TABLE COUNTERPARTIES\n" +
                    "(\n" +
                    "    ID IDENTITY PRIMARY KEY,\n" +
                    "    SENDLER VARCHAR(255),\n" +
                    "    RECEIVER VARCHAR(255),\n" +
                    "    BALANCE DECIMAL (15,2),\n" +
                    "    STATUS VARCHAR(255)\n" +
                    ")");

            String line = "";
            connection.setAutoCommit(false);
            String sql = "INSERT INTO USER (NAME) VALUES(?)";
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/AllUsers.csv"));
            line = reader.readLine();
            while (line != null) {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, line);
                preparedStatement.executeUpdate();
                line = reader.readLine();
            }
            //preparedStatement.close();

            String sql2 = "INSERT INTO BANK (CODE, UNUM, BALANCE) VALUES(?,?,?)";
            reader = new BufferedReader(new FileReader("src/main/resources/AllBankID.csv"));
            line = reader.readLine();
            while (line != null) {
                String[] str = line.split(",");
                preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.setString(1, str[0]);
                preparedStatement.setLong(2, Long.parseLong(str[1]));
                preparedStatement.setBigDecimal(3, new BigDecimal(str[2]));
                preparedStatement.executeUpdate();
                line = reader.readLine();
            }

            String sql3 = "INSERT INTO CARDS (NAME, UNUM, BNUM) VALUES(?,?,?)";
            reader = new BufferedReader(new FileReader("src/main/resources/AllCards.csv"));
            line = reader.readLine();
            while (line != null) {
                String[] str = line.split(",");
                preparedStatement = connection.prepareStatement(sql3);
                preparedStatement.setString(1, str[0]);
                preparedStatement.setLong(2, Long.parseLong(str[1].trim()));
                preparedStatement.setLong(3, Long.parseLong(str[2].trim()));
                preparedStatement.executeUpdate();
                line = reader.readLine();
            }
            connection.commit();

        } catch (SQLException | ClassNotFoundException | IOException throwables) {
            connection.rollback(savepointOne);
            throwables.printStackTrace();
        } finally {
            {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
