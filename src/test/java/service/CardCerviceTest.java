package service;

import bl.Util;
import database.MemDataBaseFile;
import entity.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CardCerviceTest extends Util {
    Connection connection;
    List<Card> cards;

    @BeforeEach
    void beforeALL() throws SQLException {
        new MemDataBaseFile().initDb();
        connection = getConnection();
        cards = new CardCervice().getAll();
    }

    @Test
    void getAll() throws SQLException {
        assertNotNull(new CardCervice().getAll());
        assertEquals(new CardCervice().getAll(), cards);
        new CardCervice().add("40817810099910006666");
        assertThrows(NullPointerException.class, () -> new CardCervice().add("44444"));
        assertNotEquals(new CardCervice().getAll(), cards);
    }

    @Test
    void getByBank() throws SQLException {
        assertNotNull(new CardCervice().getByBank("40817810099910006666"));
    }

    @Test
    void getByNameCode() throws SQLException {
        assertNotNull(new CardCervice().getByNameCode("1111 1111 1111 1111"));
        assertEquals(new CardCervice().getByNameCode("1111 1111 1111 1111").getUserId(),1);
        assertEquals(new CardCervice().getByNameCode("1111 1111 1111 1111").getName(),"1111 1111 1111 1111");
    }

    @Test
    void getByUser() throws SQLException {
        assertNotNull(new CardCervice().getByUser("Artem"));
        assertEquals(new CardCervice().getByUser("Artem").get(0).getName(),"7777 7777 7777 7777");
    }
}