package io.bankbridge;

import io.bankbridge.model.BankModel;
import io.bankbridge.providers.BanksRemoteCalls;
import io.bankbridge.services.BankService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class BankServiceTest {


    //Comment Client api testing can be done using Rest Asured or with normal Closable in Java
    private WebTarget banks;
    @Before
    public void setUp() {
        banks = ClientBuilder.newClient().target("http://localhost:1234/rbf");
    }

    //Keep the MockRemote Running

    @Ignore
    @Test
    public void testGet() {
        Response response = banks.request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

}
