package service;

import bl.Util;
import database.MemDataBaseFile;
import entity.Counterparties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CounterpartiesServiceTest extends Util {

    Connection connection;
    List<Counterparties> counterparties;

    @BeforeEach
    void beforeALL() throws SQLException {
        new MemDataBaseFile().initDb();
        connection = getConnection();
        counterparties = new CounterpartiesService().getAll();
    }


    @Test
    void getAll() throws SQLException {
        assertNotNull(new CounterpartiesService().getAll());
        assertEquals(new CounterpartiesService().getAll(),counterparties);
    }
}