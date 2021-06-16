package service;

import bl.Util;
import database.MemDataBaseFile;
import entity.BankId;
import entity.User;
import errors.BalanceErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankIdServiceTest extends Util {
    Connection connection;
    List<BankId> bankIds;

    @BeforeEach
    void beforeALL() throws SQLException {
        new MemDataBaseFile().initDb();
        connection = getConnection();
        bankIds = new BankIdService().getAll();
    }

    @Test
    void sendManey() throws SQLException, BalanceErrorException {
        new BankIdService().sendManey(new BankIdService()
                        .getByCode("40817810099910004777")
                , new BankIdService().getByCode("40817810099910006666")
                , new BigDecimal(204700));
        assertThrows(BalanceErrorException.class
                , () -> new BankIdService()
                        .sendManey(new BankIdService()
                                        .getByCode("40817810099910004777")
                                , new BankIdService().getByCode("40817810099910006666")
                                , new BigDecimal(204700)));
    }

    @Test
    void getAll() throws SQLException {
        assertNotNull(new BankIdService().getAll());
        assertEquals(new BankIdService().getAll(), bankIds);
    }

    @Test
    void getById() throws SQLException {
        assertNotNull(new BankIdService().getById(1L).getCode());
        assertNotEquals(new BankIdService()
                        .getById(1L).getCode()
                , new BankIdService().getById(2L).getCode());
        assertEquals("40817810099910009999",
                new BankIdService().getById(2L).getCode());
    }

    @Test
    void getByCode() throws SQLException {
        assertNotNull(new BankIdService()
                .getByCode("40817810099910009999").getBalance());
        assertNotEquals(new BankIdService()
                .getByCode("40817810099910009999").getId(), 1L);
        assertEquals(new BankIdService()
                .getByCode("40817810099910009999").getId(), 2L);
    }


    @Test
    void getByUser() throws SQLException {
        User user = new User();
        user.setId(999L);
        user.setName("Gena");
        new UserService().add(user);
        assertThrows(IndexOutOfBoundsException.class, () -> new BankIdService().getByUser(user).get(0).getId());
    }
}