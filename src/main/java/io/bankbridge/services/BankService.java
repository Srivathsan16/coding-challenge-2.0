package io.bankbridge.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.providers.BanksRemoteCalls;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.RemoteBankModel;
import lombok.SneakyThrows;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class BankService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Object EMPTY_OBJECT = new Object();
    /* Sync Implementation*/
    public Set<BankModel>  getBankDetails(String key, String value, Map<BankModel, Object> models, int size, BanksRemoteCalls.Counter counter) throws IOException {
        WebTarget client = ClientBuilder.newClient()
                .target(value);

        Response response = client.request().get(Response.class);
        String responseString = response.readEntity(String.class);
        RemoteBankModel model = readModel(responseString);
        models.put(new BankModel(model.getBic(), model.getName(), model.getCountryCode(), model.getAuth()), EMPTY_OBJECT);
        counter.increment();
        return models.keySet();
        }

    /*Asyn Implementation using InvocationCallback*/
    public Set<BankModel> getBankDetailsAsync(String key, String value, Map<BankModel, Object> models, int size, BanksRemoteCalls.Counter counter) {
        System.out.println("URL ::: " + value);
        WebTarget client = ClientBuilder.newClient()
                .target(value);
        client.request().async().get(new InvocationCallback<String>() {
            @SneakyThrows
            @Override
            public void completed(String response) {
               // String responseString = response.readEntity(String.class);
                RemoteBankModel model = readModel(response);
                models.put(new BankModel(model.getBic(), model.getName(), model.getCountryCode(), model.getAuth()), EMPTY_OBJECT);
                counter.increment();
            }

            @Override
            public void failed(Throwable throwable) {
                counter.increment();
                throwable.printStackTrace();
            }
        });
        System.out.println("request returns");
        return models.keySet();
    }
    private RemoteBankModel readModel(String text) throws IOException {
        return objectMapper.readValue(text, RemoteBankModel.class);
    }
}
