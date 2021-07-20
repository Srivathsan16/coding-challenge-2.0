package io.bankbridge.providers;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.RemoteCallBankDto;
import io.bankbridge.model.SearchParams;
import io.bankbridge.services.BankService;
import spark.Request;
import spark.Response;

public class BanksRemoteCalls implements ResourceProvider {

    private static Map config;
    private final BankService service;

    public BanksRemoteCalls() throws IOException {
        this.service = new BankService();
        config = new ObjectMapper()
                .readValue(Thread.currentThread().getContextClassLoader().getResource("banks-v2.json"), Map.class);

    }

    @Override
    public Set<BankModel> getBanks() {
        Map<BankModel, Object> models = new ConcurrentHashMap<>(config.size());
        Set<BankModel> result = getBanks(config, new Counter(), models);
        return result;

    }

    private Set<BankModel> getBanks(Map<String, String> config, Counter counter, Map<BankModel, Object> models) {
        Set<BankModel> result = null;
        long start = System.currentTimeMillis();
        for (Map.Entry<String, String> entry : config.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //Set<BankModel> result =
            //	service.getBankDetails(key, value, models,config.size());
            result = service.getBankDetailsAsync(key, value, models, config.size(), counter);
        }
        System.out.println("time taken: " + (System.currentTimeMillis() - start));
        while (counter.getValue() != config.size()) {
        }

        return result;
    }


    public static class Counter {
        private final AtomicInteger counter = new AtomicInteger(0);

        public int getValue() {
            return counter.get();
        }

        public void increment() {
            while (true) {
                int existingValue = getValue();
                int newValue = existingValue + 1;
                if (counter.compareAndSet(existingValue, newValue)) {
                    return;
                }
            }
        }
    }

}
