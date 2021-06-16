package entity;

import lombok.SneakyThrows;
import service.BankIdService;
import service.UserService;

import java.util.Objects;

public class Card {
    private Long id;
    private Long userId;
    private Long bankId;
    private String name;

    public Card() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return "{" +
                "\"Name\": \"" + new UserService().getById(userId).getName() + "\",\n" +
                "        \"Card number\": \"" + name + "\",\n" +
                "        \"Bank ID\": \"" + new BankIdService().getById(bankId).getCode() + "\",\n" +
                "        \"Balance\": \"" + new BankIdService().getById(bankId).getBalance() + "\"" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) && Objects.equals(userId, card.userId) && Objects.equals(bankId, card.bankId) && Objects.equals(name, card.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, bankId, name);
    }
}
