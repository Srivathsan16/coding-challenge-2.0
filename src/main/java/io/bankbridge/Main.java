package io.bankbridge;
import static spark.Spark.get;
import static spark.Spark.port;

import io.bankbridge.providers.BanksCacheBased;
import io.bankbridge.providers.BanksRemoteCalls;
import io.bankbridge.handler.RequestController;

public class Main {

	public static void main(String[] args) throws Exception {
		
		port(8080);

		/*BanksCacheBased.init();
		//BanksRemoteCalls.init();
		BanksRemoteCalls obj =new BanksRemoteCalls();*/
		RequestController localProvider = new RequestController(new BanksCacheBased());
		RequestController remoteProvider = new RequestController(new BanksRemoteCalls());

		get("/v1/banks/all", localProvider::handle);
		get("/v2/banks/all/:page/:pageSize", remoteProvider::handle);
		//get("/v1/banks/all", (request, response) -> BanksCacheBased.handle(request, response));

		//Ideally this should be post since we are doing the search
		//get("/v2/banks/all/:page/:pageSize", (request, response) -> obj.handle(request, response));
	}
}