package dataaccess;

import entity.BankId;
import entity.User;
import errors.BalanceErrorException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface BankImp {
    //create
    void add(BankId bankId) throws SQLException;

    //read
    List<BankId> getAll() throws SQLException;

    BankId getById(Long id) throws SQLException;

    BankId getByCode(String code) throws SQLException;

    void addManey(BankId bankId, BigDecimal balance) throws SQLException;

    void sendManey(BankId sendler, BankId receiver, BigDecimal balance) throws SQLException, BalanceErrorException;

    List<BankId> getByUser(User user) throws SQLException;

//    //update
//    void update(BankId bankId);

}
