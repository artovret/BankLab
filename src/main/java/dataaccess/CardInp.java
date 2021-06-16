package dataaccess;


import entity.Card;

import java.sql.SQLException;
import java.util.List;

public interface CardInp {
    //create
    void add(String bankId) throws SQLException;

    //read
    List<Card> getAll() throws SQLException;

    List<Card> getByBank(String bankCode) throws SQLException;

    Card getByNameCode(String bankCode) throws SQLException;

    List<Card> getByUser(String userName) throws SQLException;

}
