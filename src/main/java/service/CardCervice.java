package service;

import bl.Util;
import dataaccess.CardInp;
import entity.BankId;
import entity.Card;
import entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Работа с таблицей карт
 */
public class CardCervice extends Util implements CardInp {

    Connection connection = getConnection();

    /**
     * внести новые данные в таблицу
     *
     * @param bankCode
     * @throws SQLException
     */
    @Override
    public void add(String bankCode) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        BankId bankId = new BankIdService().getByCode(bankCode);
        final Random random = new Random();

        String cardName = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + " " + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + " " + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + " " + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);

        String sql = "INSERT INTO CARDS (NAME, UNUM, BNUM) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardName);
            preparedStatement.setLong(2, bankId.getUnum());
            preparedStatement.setLong(3, bankId.getId());
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
    public List<Card> getAll() throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        List<Card> cards = new ArrayList<>();
        String sql = "select * from CARDS";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Card card = new Card();
                card.setId(resultSet.getLong("CNUM"));
                card.setName(resultSet.getString("NAME"));
                card.setUserId(resultSet.getLong("UNUM"));
                card.setBankId(resultSet.getLong("BNUM"));
                cards.add(card);
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
        return cards;
    }

    /**
     * получить данные спецефически
     *
     * @param bankCode
     * @return
     * @throws SQLException
     */
    @Override
    public List<Card> getByBank(String bankCode) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        BankId bankId = new BankIdService().getByCode(bankCode);
        List<Card> cards = new ArrayList<>();
        String sql = "select * from CARDS WHERE BNUM=" + bankId.getId();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Card card = new Card();
                card.setId(resultSet.getLong("CNUM"));
                card.setName(resultSet.getString("NAME"));
                card.setUserId(resultSet.getLong("UNUM"));
                card.setBankId(resultSet.getLong("BNUM"));
                cards.add(card);
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
        return cards;
    }

    /**
     * получить данные спецефически
     *
     * @param code
     * @return
     * @throws SQLException
     */
    @Override
    public Card getByNameCode(String code) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        Card card = new Card();
        String sql = "select * from CARDS WHERE NAME=" + "'" + code + "'";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                card.setId(resultSet.getLong("CNUM"));
                card.setName(resultSet.getString("NAME"));
                card.setUserId(resultSet.getLong("UNUM"));
                card.setBankId(resultSet.getLong("BNUM"));
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
        return card;
    }

    /**
     * получить данные спецефически
     *
     * @param userName
     * @return
     * @throws SQLException
     */
    @Override
    public List<Card> getByUser(String userName) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");
        List<Card> cards = new ArrayList<>();
        try {
            User user = new UserService().getByName(userName);
            for (BankId bankId : new BankIdService().getByUser(user)) {
                cards.addAll(new CardCervice().getByBank(bankId.getCode()));
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
        return cards;
    }

}
