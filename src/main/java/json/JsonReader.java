package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonReader {
    public List<CardJson> getCards(String json) {
        ObjectMapper mapper = new ObjectMapper();
        List<CardJson> cardList = new ArrayList<>();
        try {
            cardList = Arrays.asList(mapper.readValue(json, CardJson[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return cardList;
    }

    public List<BankIdJson> getBankId(String json) {
        ObjectMapper mapper = new ObjectMapper();
        List<BankIdJson> bankIdJsons = new ArrayList<>();
        try {
            bankIdJsons = Arrays.asList(mapper.readValue(json, BankIdJson[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bankIdJsons;
    }

    public List<CounterpartiesJson> getCounterparties(String json) {
        ObjectMapper mapper = new ObjectMapper();
        List<CounterpartiesJson> counterpartiesJsons = new ArrayList<>();
        try {
            counterpartiesJsons = Arrays.asList(mapper.readValue(json, CounterpartiesJson[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return counterpartiesJsons;
    }
}
