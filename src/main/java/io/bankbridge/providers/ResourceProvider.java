package io.bankbridge.providers;

import io.bankbridge.model.BankModel;

import java.util.Set;

public interface ResourceProvider {
    Set<BankModel> getBanks();
}
