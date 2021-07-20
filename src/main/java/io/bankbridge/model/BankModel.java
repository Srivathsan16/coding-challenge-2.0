package io.bankbridge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankModel {
	
	public String bic;
	public String name;
	public String countryCode;
	public String auth;
	public ArrayList products;

	public BankModel(String bic, String name, String countryCode, String auth) {
		this.bic = bic;
		this.name = name;
		this.countryCode = countryCode;
		this.auth = auth;
	}

}
