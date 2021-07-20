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

import javax.swing.text.View;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteCallBankDto {

    private String id;
    private String name;
    private String countryCode;
    private String auth;
    public ArrayList products;
}
