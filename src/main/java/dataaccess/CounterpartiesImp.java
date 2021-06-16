package dataaccess;

import entity.Counterparties;

import java.sql.SQLException;
import java.util.List;

public interface CounterpartiesImp {

    //вносим в базу

    void add(Counterparties counterparties) throws SQLException;

    //получаем всю историю
    List<Counterparties> getAll() throws SQLException;

}
