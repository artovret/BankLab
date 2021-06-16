package entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Counterparties {
    private Long id;
    private String sender;      //отправитель
    private String receiver;    //получатель
    private BigDecimal summ;
    private String status;

    public Counterparties() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getSumm() {
        return summ;
    }

    public void setSumm(BigDecimal summ) {
        this.summ = summ;
    }

    @Override
    public String toString() {
        return "{" +
                "\"Sender\": \"" + sender + "\",\n" +
                "        \"Seceiver\": \"" + receiver + "\",\n" +
                "        \"Transaction amount\": \"" + summ + "\",\n" +
                "        \"Transaction status\": \"" + status + "\"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counterparties that = (Counterparties) o;
        return Objects.equals(id, that.id) && Objects.equals(sender, that.sender) && Objects.equals(receiver, that.receiver) && Objects.equals(summ, that.summ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver, summ);
    }
}
