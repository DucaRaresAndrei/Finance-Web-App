# ExpenseControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createExpense**](ExpenseControllerApi.md#createExpense) | **POST** /expense |  |


<a name="createExpense"></a>
# **createExpense**
> PairExpenseIncome createExpense(expenseRequestDTO)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ExpenseControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    ExpenseControllerApi apiInstance = new ExpenseControllerApi(defaultClient);
    ExpenseRequestDTO expenseRequestDTO = new ExpenseRequestDTO(); // ExpenseRequestDTO | 
    try {
      PairExpenseIncome result = apiInstance.createExpense(expenseRequestDTO);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExpenseControllerApi#createExpense");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **expenseRequestDTO** | [**ExpenseRequestDTO**](ExpenseRequestDTO.md)|  | |

### Return type

[**PairExpenseIncome**](PairExpenseIncome.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

