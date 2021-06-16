package dataaccess;

import entity.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface UserImp {
    //create
    void add(User user) throws SQLException;

    BigDecimal getBalance(String name, String anyName) throws SQLException;

    //read
    List<User> getAll() throws SQLException;

    User getById(Long id) throws SQLException;

    User getByName(String name) throws SQLException;

    //update
    void update(User user) throws SQLException;

}
