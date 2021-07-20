package io.bankbridge.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.FinalBankDto;
import io.bankbridge.model.SearchParams;
import io.bankbridge.model.View;
import io.bankbridge.providers.BanksCacheBased;
import io.bankbridge.providers.ResourceProvider;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RequestController {

    private final io.bankbridge.providers.ResourceProvider resourceProvider;
    private final ObjectMapper objectMapper;

    public RequestController(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
        this.objectMapper = new ObjectMapper();
    }



    public String handle(Request request, Response response) {
        SearchParams criteria = new Gson().fromJson(request.body(), SearchParams.class);
        //I am setting by default page size as 5 and page as 1, even if it is all banks
        int page = request.params(":page")!=null?Integer.parseInt(request.params(":page")):1;
        int pageSize = request.params(":pageSize")!=null?Integer.parseInt(request.params(":pageSize")):5;
         try {
            List<FinalBankDto> allBanks = new ArrayList<>();
            //Comment "i" is just to Keep track when doing Pagination
            int i=0;
            for (BankModel b : resourceProvider.getBanks()) {
                FinalBankDto finalBankDto = new FinalBankDto();
                finalBankDto.setId(b.getBic());
                if ((resourceProvider.getClass() != BanksCacheBased.class)) {
                    finalBankDto.setAuth(b.getAuth());
                } else {
                    finalBankDto.setProducts(b.getProducts());
                }
                finalBankDto.setCountryCode(b.getCountryCode());
                finalBankDto.setName(b.getName());
                allBanks.add(i, finalBankDto);
                i++;
            }
            return getResultBanks(criteria, page, pageSize, allBanks);

        } catch (JsonProcessingException e) {
            response.status(503);
            return "Service unavailable";
        }

    }


    //Adds Criteria and Pagination
    private String getResultBanks(SearchParams criteria, int page, int pageSize, List<FinalBankDto> allBanks) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (criteria != null) {
            List<FinalBankDto> result = getPaginated(getFilteredBanks(allBanks, criteria), page, pageSize);
            //TODO: Remove Finally after testing all scenarios
            for (FinalBankDto x : result) {
                System.out.println("Lists : " + x.getId());
            }
            return getFinalJsonBasedOnObjectCalls(mapper, result);
        } else {
            List<FinalBankDto> result = getPaginated(allBanks, page, pageSize);
            for (FinalBankDto x : result) {
                System.out.println("Lists in else:: " + x.getId());
            }
            return getFinalJsonBasedOnObjectCalls(mapper, result);
        }
    }
    //Get the final Sting based on from where it is called
    public String getFinalJsonBasedOnObjectCalls(ObjectMapper mapper, List<FinalBankDto> result) throws JsonProcessingException {
        if (this.resourceProvider.getClass() != BanksCacheBased.class)
            return mapper.writerWithView(View.ExtendedPublic.class).writeValueAsString(result);
        else {
            return mapper.writerWithView(View.Internal.class).writeValueAsString(result);
        }
    }

    //Filtering based on Search Params
    public List<FinalBankDto> getFilteredBanks(List<FinalBankDto> allBanks, SearchParams criteria) {

        Predicate<FinalBankDto> idPredicate = d-> (criteria.getId()!=null) && (criteria.getId().equals(d.getId()));
        Predicate<FinalBankDto> authPredicate = d-> (criteria.getAuth()!=null) && (criteria.getAuth().equals(d.getAuth()));
        Predicate<FinalBankDto> countryCodePredicate = d-> (criteria.getCountryCode()!=null ) && (criteria.getCountryCode().equals(d.getCountryCode()));
        Predicate<FinalBankDto> namePredicate = d-> (criteria.getName()!=null) && (criteria.getName().equals(d.getName()));
        return allBanks.stream().filter(idPredicate.or(namePredicate).or(countryCodePredicate).or(authPredicate)).collect(Collectors.toList());
    }

    public  List<FinalBankDto> getPaginated(List<FinalBankDto> allBanks, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if(allBanks == null || allBanks.size() < fromIndex){
            return Collections.emptyList();
        }
        // index "TO" is inclusive
        return allBanks.subList(fromIndex, Math.min(fromIndex + pageSize, allBanks.size()));
    }
}
