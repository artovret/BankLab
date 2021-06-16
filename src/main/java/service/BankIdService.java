package service;

import bl.Util;
import dataaccess.BankImp;
import entity.BankId;
import entity.Counterparties;
import entity.User;
import errors.BalanceErrorException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с таблицей банковских счетов (аккаунтов)
 */
public class BankIdService extends Util implements BankImp {

    Connection connection = getConnection();

    /**
     * внести новые данные в таблицу
     *
     * @param bankId
     * @throws SQLException
     */
    @Override
    public void add(BankId bankId) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        String sql = "INSERT INTO BANK (CODE, UNUM, BALANCE) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, bankId.getCode());
            preparedStatement.setLong(2, bankId.getUnum());
            preparedStatement.setBigDecimal(3, bankId.getBalance());
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
    public List<BankId> getAll() throws SQLException {
        List<BankId> bankIds = new ArrayList<>();
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        String sql = "select * from BANK";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                BankId bankId = new BankId();
                bankId.setId(resultSet.getLong("BNUM"));
                bankId.setCode(resultSet.getString("CODE"));
                bankId.setUnum(resultSet.getLong("UNUM"));
                bankId.setBalance(resultSet.getBigDecimal("BALANCE"));
                bankIds.add(bankId);
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
        return bankIds;
    }

    /**
     * получить данные спецефически
     *
     * @param bnum
     * @return
     * @throws SQLException
     */
    @Override
    public BankId getById(Long bnum) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "select * from BANK WHERE BNUM=?";
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        BankId bankId = new BankId();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, bnum);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bankId.setId(resultSet.getLong("BNUM"));
                bankId.setCode(resultSet.getString("CODE"));
                bankId.setUnum(resultSet.getLong("UNUM"));
                bankId.setBalance(resultSet.getBigDecimal("BALANCE"));
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
        return bankId;
    }

    /**
     * получить данные спецефически
     *
     * @param code
     * @return
     * @throws SQLException
     */
    @Override
    public BankId getByCode(String code) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "select * from BANK WHERE CODE=?";
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        BankId bankId = new BankId();
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bankId.setId(resultSet.getLong("BNUM"));
                bankId.setCode(resultSet.getString("CODE"));
                bankId.setUnum(resultSet.getLong("UNUM"));
                bankId.setBalance(resultSet.getBigDecimal("BALANCE"));
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
        return bankId;
    }

    /**
     * внести деньги
     *
     * @param bankId
     * @param balance
     * @throws SQLException
     */
    @Override
    public void addManey(BankId bankId, BigDecimal balance) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE BANK SET BALANCE=? WHERE BNUM=?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBigDecimal(1, bankId.getBalance().add(balance));
            preparedStatement.setLong(2, bankId.getId());

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
     * переслать средства
     *
     * @param sendler
     * @param receiver
     * @param summ
     * @throws SQLException
     * @throws BalanceErrorException исключение выброшенное в случае, если транзакция не завершена
     */
    @Override
    public void sendManey(BankId sendler, BankId receiver, BigDecimal summ) throws SQLException, BalanceErrorException {
        PreparedStatement preparedStatement = null;
        String sql = null;
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        try {
            sql = "UPDATE BANK SET BALANCE=? WHERE BNUM=?";
            preparedStatement = connection.prepareStatement(sql);
            BigDecimal balance = sendler.getBalance();
            if (balance.max(summ).equals(summ)) {
                Counterparties counterparties = new Counterparties();
                counterparties.setSender(sendler.getCode());
                counterparties.setReceiver(receiver.getCode());
                counterparties.setSumm(summ);
                counterparties.setStatus("not approved");
                new CounterpartiesService().add(counterparties);
                throw new BalanceErrorException("Not enough money in the bank account " + sendler.getCode() + " money transfer error");
            } else {
                preparedStatement.setBigDecimal(1, balance.subtract(summ));
                preparedStatement.setLong(2, sendler.getId());
                preparedStatement.executeUpdate();
                //connection.close();
                sql = "UPDATE BANK SET BALANCE=? WHERE BNUM=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setBigDecimal(1, receiver.getBalance().add(summ));
                preparedStatement.setLong(2, receiver.getId());
                preparedStatement.executeUpdate();
                Counterparties counterparties = new Counterparties();
                counterparties.setSender(sendler.getCode());
                counterparties.setReceiver(receiver.getCode());
                counterparties.setSumm(summ);
                counterparties.setStatus("approved");
                new CounterpartiesService().add(counterparties);
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
    }

    /**
     * получить данные спецефически
     *
     * @param user
     * @return
     * @throws SQLException
     */
    @Override
    public List<BankId> getByUser(User user) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        List<BankId> bankIds = new ArrayList<>();
        String sql = "select * from BANK WHERE UNUM=" + user.getId();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                BankId bankId = new BankId();
                bankId.setId(resultSet.getLong("BNUM"));
                bankId.setCode(resultSet.getString("CODE"));
                bankId.setUnum(resultSet.getLong("UNUM"));
                bankId.setBalance(resultSet.getBigDecimal("BALANCE"));
                bankIds.add(bankId);
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
        return bankIds;
    }
}
