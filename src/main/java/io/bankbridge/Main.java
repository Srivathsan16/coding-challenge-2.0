package io.bankbridge;

import io.bankbridge.providers.BanksCacheBased;
import io.bankbridge.providers.BanksRemoteCalls;
import io.bankbridge.handler.RequestController;

import static spark.Spark.*;

public class Main {

	public static void main(String[] args) throws Exception {
		
		port(8080);

		/*BanksCacheBased.init();
		//BanksRemoteCalls.init();
		BanksRemoteCalls obj =new BanksRemoteCalls();*/
		RequestController localProvider = new RequestController(new BanksCacheBased());
		RequestController remoteProvider = new RequestController(new BanksRemoteCalls());

		//Using Path groups
		//Ideally this should be post since we are doing the search
		path("/v1/banks/all",()-> {
			get("/:page", localProvider::handle);
			get("/:page/:pageSize", localProvider::handle);
			get("", localProvider::handle);
				});
		path("/v2/banks/all",()-> {
			get("/:page", remoteProvider::handle);
			get("/:page/:pageSize", remoteProvider::handle);
			get("", remoteProvider::handle);
		});

	}
}