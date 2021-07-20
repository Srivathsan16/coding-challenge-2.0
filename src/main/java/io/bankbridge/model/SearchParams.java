package io.bankbridge.model;

import lombok.Data;

@Data
public class SearchParams {

    private String id;
    private String name;
    private String countryCode;
    private String auth;
}
