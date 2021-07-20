package io.bankbridge.providers;
import java.io.IOException;
import java.util.*;

import io.bankbridge.handler.RequestController;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.bankbridge.model.BankModel;
import io.bankbridge.model.BankModelList;
import spark.Request;
import spark.Response;

public class BanksCacheBased implements ResourceProvider {


	private final Set<BankModel> cache;

	public BanksCacheBased() throws IOException {
		BankModelList models = new ObjectMapper().readValue(
				Thread.currentThread().getContextClassLoader().getResource("banks-v1.json"), BankModelList.class);

		cache = models.getBanks();

	}

	@Override
	public Set<BankModel> getBanks() {
		return cache;
	}
}


