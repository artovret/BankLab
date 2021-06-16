package service;

import bl.Util;
import dataaccess.CounterpartiesImp;
import entity.Counterparties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * класс для взаимодействия с таблицей контрагентов (история переводов)
 */
public class CounterpartiesService extends Util implements CounterpartiesImp {
    Connection connection = getConnection();

    /**
     * внести новые данные в таблицу
     *
     * @param counterparties
     * @throws SQLException
     */
    @Override
    public void add(Counterparties counterparties) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        String sql = "INSERT INTO COUNTERPARTIES (SENDLER,RECEIVER,BALANCE,STATUS) VALUES(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, counterparties.getSender());
            preparedStatement.setString(2, counterparties.getReceiver());
            preparedStatement.setBigDecimal(3, counterparties.getSumm());
            preparedStatement.setString(4, counterparties.getStatus());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepointOne);
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * получить все данные из таблицы
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<Counterparties> getAll() throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        List<Counterparties> counterpartiesList = new ArrayList<>();
        String sql = "select * from COUNTERPARTIES";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Counterparties counterparties = new Counterparties();
                counterparties.setId(resultSet.getLong("ID"));
                counterparties.setSender(resultSet.getString("SENDLER"));
                counterparties.setReceiver(resultSet.getString("RECEIVER"));
                counterparties.setSumm(resultSet.getBigDecimal("BALANCE"));
                counterparties.setStatus(resultSet.getString("STATUS"));
                counterpartiesList.add(counterparties);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepointOne);
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return counterpartiesList;
    }
}
