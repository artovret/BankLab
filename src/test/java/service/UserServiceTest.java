package service;

import bl.Util;
import database.MemDataBaseFile;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends Util {

    Connection connection;
    List<User> users;

    @BeforeEach
    void beforeALL() throws SQLException {
        new MemDataBaseFile().initDb();
        connection = getConnection();
        users = new UserService().getAll();
    }

    @Test
    void getById() throws SQLException {
        assertNotNull(new UserService().getById(1L));
        assertEquals(new UserService().getById(1L).getName(),"Artem");
        User user = new User();
        user.setId(999L);
        user.setName("Gena");
        new UserService().add(user);
        assertNotEquals(new UserService().getByName("Gena").getId(), 999L);
        assertEquals(new UserService().getByName("Gena").getId(),4);
    }

    @Test
    void getByName() throws SQLException {
        assertNotNull(new UserService().getByName("Artem"));
        User user = new User();
        user.setId(999L);
        user.setName("Gena");
        new UserService().add(user);
        assertNotEquals(new UserService().getByName("Gena").getId(), 999L);
        assertEquals(new UserService().getByName("Gena").getId(),4);
    }

    @Test
    void getBalance() throws SQLException {
        assertNotNull(new UserService().getBalance("Artem","Balance"));
        assertEquals(new UserService().getBalance("Artem","7777 7777 7777 7777"),new BigDecimal("204700.28"));
        assertEquals(new UserService().getBalance("Artem","88888888899914439333"),new BigDecimal("30000.01"));
        assertEquals(new UserService().getBalance("Artem","Balance"),new BigDecimal("204700.28").add(new BigDecimal("30000.01")));
    }

    @Test
    void getAll() throws SQLException {
        assertNotNull(new UserService().getAll());
        assertEquals(new UserService().getAll(),users);
        User user = new User();
        user.setId(7L);
        user.setName("Gena");
        new UserService().add(user);
        assertNotEquals(new UserService().getAll(),users);
    }
}
