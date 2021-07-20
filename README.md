# Fintech Innovation Coding Test

##Solution: 
I have added Endpoint as path groups
Recommed to use Postman for testing
Request : http://localhost:8080/v1/banks/all
           http://localhost:8080/v1/banks/all/{page} :  for page
           http://localhost:8080/v1/banks/all/{page}/{pageSize} : to specify size
Request Body can have search Params as :
    {
        "id": "CUPIDATATSP1XXX",
        "countryCode" : "GB"
    } 
          
The same applies for remote bank calls, just replace v1 by v2
Response sample : 
For local calls from cache:
```json
[
{"id":"PARIATURDEU0XXX","name":"Banco de espiritu
santo","countryCode":"GB","products":["accounts","payments"]},{"id":"CUPIDATATSP1XXX","name":"Credit
Sweets","countryCode":"CH","products":["accounts","payments"]},{"id":"DOLORENOR2XXX","name":"Royal Bank Of
Fun","countryCode":"GB","products":["accounts"]}]
```

For Remote Calls :
```json
[{"id":"CUPIDATATSP1XXX","name":"Credit
Sweets","countryCode":"CH","auth":"open-id"},{"id":"PARIATURDEU0XXX","name":"Banco de espiritu
santo","countryCode":"GB","auth":"oauth"},{"id":"DOLORENOR2XXX","name":"Royal Bank of
Fun","countryCode":"GB","auth":"oauth"}]
```

#Also provided inline comments as well 

## Intro:
We have an API towards application developers, which returns information about all the banks which are available for the application.

The response from the API looks like this:
```json
[
  {
    "bic":"DOLORENOR9XXX",
    "name":"Bank Dolores",
    "countryCode":"NO",
    "auth":"ssl-certificate",
    "products":[
      "accounts",
      "payments"
    ]   
  },
  {
    ...
  }
]
```
There are two version of the API:

- `/v1/banks/all` - implementation is based on the static file, which is locally available
- `/v2/banks/all` - new version of the API, which will need to read the data from the remote servers

## Challenge:
1. Add unit tests for both API versions.

2. API response:
    - v1 should return: name, id, countryCode and product
    - v2 should return: name, id, countryCode and auth

3. Complete the implementation of the `/v2/banks/all` endpoint, by implementing `BanksRemoteCalls.handle(Request request, Response response)` method.
The respective configuration file is `banks-v2.json`. Implementation needs to use the data from the configuration file,
and for each bank retrieve the data from the remote URL specified. You will need to add HTTP client of your choice to the project. 
You can find the mock implementation for the remote URLs in the MockRemotes class. 

4. Add pagination and filtering to v1 and v2 of the banks APIs.
- Non-Functional requirements
  - The client should be able to define the page size, default being 5.
  - A filter for countrycode, product and other values should be added.

5. Refactor the existing code base.
    - Feel free to add comments to the code to clarify the changes you are making.
## Sending in the assignment
- You may send in the assignment on a git repository or as a zip.


