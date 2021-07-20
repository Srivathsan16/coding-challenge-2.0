package io.bankbridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteBankModel {
    private String bic;
    private String name;
    private String countryCode;
    private String auth;
}
