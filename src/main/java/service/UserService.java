package service;

import bl.Util;
import dataaccess.UserImp;
import entity.BankId;
import entity.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * класс для работы с таблицей пользователей
 */
public class UserService extends Util implements UserImp {

    Connection connection = getConnection();

    /**
     * внести новые данные в таблицу
     *
     * @param user
     * @throws SQLException
     */
    @Override
    public void add(User user) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO USER (NAME) VALUES(?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepointOne);
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * получить данные спецефические
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public User getById(Long id) throws SQLException { //preparedStatement
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        PreparedStatement preparedStatement = null;
        String sql = "select UNUM,NAME from USER WHERE UNUM=?";
        User user = new User();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setId(resultSet.getLong("UNUM"));
                user.setName(resultSet.getString("NAME"));
                //preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepointOne);
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return user;
    }

    /**
     * получить данные спецефические
     *
     * @param name
     * @return
     * @throws SQLException
     */
    @Override
    public User getByName(String name) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        PreparedStatement preparedStatement = null;
        String sql = "select UNUM,NAME from USER WHERE NAME=?";
        User user = new User();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setId(resultSet.getLong("UNUM"));
                user.setName(resultSet.getString("NAME"));
                //preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepointOne);
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return user;
    }

    /**
     * получить данные спецефические
     *
     * @param name
     * @param anyName
     * @return
     * @throws SQLException
     */
    @Override
    public BigDecimal getBalance(String name, String anyName) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        BigDecimal balance = null;
        try {
            balance = new BigDecimal(0);
            if (new UserService().getByName(name).getId() != null) {
                long id = new UserService().getByName(name).getId();
                if ("Balance".equals(anyName)) {
                    for (BankId bankId : new BankIdService().getByUser(new UserService().getByName(name))) {
                        balance = balance.add(bankId.getBalance());
                    }
                    return balance;
                }
                if ((new BankIdService().getByCode(anyName).getId() != null) && id == new BankIdService().getByCode(anyName).getUnum()) {
                    return balance.add(new BankIdService().getByCode(anyName).getBalance());
                }
                if ((new CardCervice().getByNameCode(anyName).getId() != null) && new CardCervice().getByNameCode(anyName).getUserId() == id) {
                    return balance.add(new BankIdService().getById(new CardCervice().getByNameCode(anyName).getBankId()).getBalance());
                }
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepointOne);
            e.printStackTrace();
        }
        return balance;
    }

    /**
     * получить данные спецефические
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<User> getAll() throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        List<User> users = new ArrayList<>();
        String sql = "select * from USER";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("UNUM"));
                user.setName(resultSet.getString("NAME"));
                users.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepointOne);
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return users;
    }

    /**
     * изменить юзверя
     *
     * @param user
     * @throws SQLException
     */
    @Override
    public void update(User user) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE USER SET NAME=? WHERE UNUM=?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getId());

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepointOne);
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}

