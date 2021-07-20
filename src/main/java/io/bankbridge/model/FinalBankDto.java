package io.bankbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import io.bankbridge.providers.BanksCacheBased;
import io.bankbridge.providers.BanksRemoteCalls;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalBankDto {

    @JsonView(View.Public.class)
    private String id;
    @JsonView(View.Public.class)
    private String name;
    @JsonView(View.Public.class)
    private String countryCode;
    @JsonView(View.ExtendedPublic.class)
    private String auth;
    @JsonView(View.Internal.class)
    public ArrayList products;
}
