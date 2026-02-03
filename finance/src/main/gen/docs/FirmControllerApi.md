# FirmControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listFirms**](FirmControllerApi.md#listFirms) | **POST** /firms |  |
| [**upgradeToFirm**](FirmControllerApi.md#upgradeToFirm) | **POST** /firms/upgrade |  |


<a name="listFirms"></a>
# **listFirms**
> List&lt;FirmDTO&gt; listFirms(firmListRequestDTO)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FirmControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FirmControllerApi apiInstance = new FirmControllerApi(defaultClient);
    FirmListRequestDTO firmListRequestDTO = new FirmListRequestDTO(); // FirmListRequestDTO | 
    try {
      List<FirmDTO> result = apiInstance.listFirms(firmListRequestDTO);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling FirmControllerApi#listFirms");
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
| **firmListRequestDTO** | [**FirmListRequestDTO**](FirmListRequestDTO.md)|  | |

### Return type

[**List&lt;FirmDTO&gt;**](FirmDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="upgradeToFirm"></a>
# **upgradeToFirm**
> Object upgradeToFirm(upgradeFirmRequestDTO)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FirmControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FirmControllerApi apiInstance = new FirmControllerApi(defaultClient);
    UpgradeFirmRequestDTO upgradeFirmRequestDTO = new UpgradeFirmRequestDTO(); // UpgradeFirmRequestDTO | 
    try {
      Object result = apiInstance.upgradeToFirm(upgradeFirmRequestDTO);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling FirmControllerApi#upgradeToFirm");
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
| **upgradeFirmRequestDTO** | [**UpgradeFirmRequestDTO**](UpgradeFirmRequestDTO.md)|  | |

### Return type

**Object**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

