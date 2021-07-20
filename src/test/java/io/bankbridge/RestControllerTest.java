package io.bankbridge;



import io.bankbridge.handler.RequestController;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.FinalBankDto;
import io.bankbridge.model.SearchParams;
import io.bankbridge.providers.BanksCacheBased;
import io.bankbridge.providers.BanksRemoteCalls;
import io.bankbridge.providers.ResourceProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

public class RestControllerTest {

    @Test
    public void test_PaginationForPageSize() throws Exception {

        final List<FinalBankDto> banks = new ArrayList<>(6);
        for(int i = 0 ; i < 6;i++){
           banks.add(any(FinalBankDto.class));
       }
        RequestController requestController = new RequestController(new BanksRemoteCalls());
        Assert.assertEquals(3, requestController.getPaginated(banks,1,3).size());

    }

    @Test
    public void test_PaginationForTwoPages() throws Exception {

        int page = 1;
        int pageSize = 2;

        RequestController requestController = new RequestController(new BanksRemoteCalls());
        Assert.assertEquals(2, requestController.getPaginated(getBanksListMock(),page++,pageSize).size());
        Assert.assertEquals(1, requestController.getPaginated(getBanksListMock(),page++,pageSize).size());
        //Empty List Test
        Assert.assertEquals(0, requestController.getPaginated(getBanksListMock(),page++,pageSize).size());

    }


    @Test
    public void test_localProvider() throws IOException {
        ResourceProvider local = new BanksCacheBased();
        Set<BankModel> modelSet = local.getBanks();
        for(BankModel b : modelSet){
            System.out.println("Model " + b);
        }
        //assertEquals(1, modelSet.size());
        //assertEquals("Name", modelSet.stream().findFirst().get().getName());
    }


    @Test
    public void test_PaginationValueEqualToList() throws Exception {

        int page = 1;
        int pageSize = 3;
        RequestController requestController = new RequestController(new BanksRemoteCalls());
        Assert.assertEquals(3, requestController.getPaginated(getBanksListMock(),page++,pageSize).size());
        //Empty List Test
        Assert.assertEquals(Collections.emptyList(), requestController.getPaginated(getBanksListMock(),page++,pageSize));
    }

    @Test
    public void test_PaginationValueCheck() throws Exception {

        int page = 1;
        int pageSize = 2;
        RequestController requestController = new RequestController(new BanksRemoteCalls());
        Assert.assertEquals("NULLASP17XXX", requestController.getPaginated(getBanksListMock(),page,pageSize).get(0).getId());

    }

    @Test
    public void test_SearchParamsWithOnlyCountryCodeReturnSingleElementInList() throws Exception {

        SearchParams searchParams = new SearchParams();
        searchParams.setCountryCode("DE");
        RequestController requestController = new RequestController(new BanksRemoteCalls());
        Assert.assertEquals(Arrays.asList("DE","DE"), requestController.getFilteredBanks(getBanksListMock(),searchParams)
                .stream().map(FinalBankDto::getCountryCode).collect(Collectors.toList()));

    }

    @Test
    public void test_SearchParamsWithCountryCodeAndId() throws Exception {

        SearchParams searchParams = new SearchParams();
        searchParams.setAuth("oauth");
        searchParams.setId("MOLLITSP13XXX");
        RequestController requestController = new RequestController(new BanksRemoteCalls());
        Assert.assertEquals(Arrays.asList("MOLLITSP13XXX","CUPIDATATSP1XXX"), requestController.getFilteredBanks(getBanksListMock(),searchParams)
                .stream().map(FinalBankDto::getId).collect(Collectors.toList()));

    }

    @Test
    public void test_SearchParamsWithCountryCodeReturnAllElementWithCountryCodeInList() throws Exception {

        RequestController requestController = new RequestController(new BanksRemoteCalls());
        Assert.assertEquals("NULLASP17XXX", requestController.getFilteredBanks(getBanksListMock(),getSearchParamsMock()).get(0).getId());
        Assert.assertEquals(1, requestController.getFilteredBanks(getBanksListMock(),getSearchParamsMock()).size());

    }

    private List<FinalBankDto> getBanksListMock() {
        List<FinalBankDto> paginatedbanks = new ArrayList<>();
        FinalBankDto banks = new FinalBankDto();
        banks.setId("NULLASP17XXX");
        banks.setName("Last National Bank");
        banks.setAuth("ssl-certificate");
        banks.setCountryCode("NO");

        FinalBankDto banks1 = new FinalBankDto();
        banks1.setId("MOLLITSP13XXX");
        banks1.setName("Bank Nulla");
        banks1.setAuth("ssl-certificate");
        banks1.setCountryCode("DE");

        FinalBankDto banks2 = new FinalBankDto();
        banks2.setId("CUPIDATATSP1XXX");
        banks2.setName("Credit Sweets");
        banks2.setAuth("oauth");
        banks2.setCountryCode("DE");
        paginatedbanks.add(0,banks);
        paginatedbanks.add(1,banks1);
        paginatedbanks.add(2,banks2);
        return paginatedbanks;
    }

    private SearchParams getSearchParamsMock() {
        SearchParams searchParams = new SearchParams();
        //searchParams.setAuth("oauth");
        searchParams.setCountryCode("NO");
        return searchParams;
    }
}
