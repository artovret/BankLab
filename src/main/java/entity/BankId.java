package entity;

import lombok.SneakyThrows;
import service.UserService;

import java.math.BigDecimal;
import java.util.Objects;

public class BankId {
    private Long id;
    private String code;
    private Long unum;
    private BigDecimal balance;

    public BankId() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getUnum() {
        return unum;
    }

    public void setUnum(Long unum) {
        this.unum = unum;
    }
    //new UserService().getById(unum).getName()


    @SneakyThrows
    @Override
    public String toString() {
        return "{" +
                "\"name\": \"" + new UserService().getById(unum).getName() + "\",\n" +
                "        \"code\": \"" + code + "\",\n" +
                "        \"balance\": \"" + balance + '\"' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankId bankId = (BankId) o;
        return Objects.equals(id, bankId.id) && Objects.equals(code, bankId.code) && Objects.equals(unum, bankId.unum) && Objects.equals(balance, bankId.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, unum, balance);
    }
}
