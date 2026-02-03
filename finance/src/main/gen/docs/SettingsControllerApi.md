# SettingsControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getSettings**](SettingsControllerApi.md#getSettings) | **GET** /settings |  |
| [**updateSettings**](SettingsControllerApi.md#updateSettings) | **PUT** /settings |  |


<a name="getSettings"></a>
# **getSettings**
> SettingsRespDTO getSettings()



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsControllerApi apiInstance = new SettingsControllerApi(defaultClient);
    try {
      SettingsRespDTO result = apiInstance.getSettings();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsControllerApi#getSettings");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsRespDTO**](SettingsRespDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="updateSettings"></a>
# **updateSettings**
> SettingsRespDTO updateSettings(updateSettingsReqDTO)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsControllerApi apiInstance = new SettingsControllerApi(defaultClient);
    UpdateSettingsReqDTO updateSettingsReqDTO = new UpdateSettingsReqDTO(); // UpdateSettingsReqDTO | 
    try {
      SettingsRespDTO result = apiInstance.updateSettings(updateSettingsReqDTO);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsControllerApi#updateSettings");
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
| **updateSettingsReqDTO** | [**UpdateSettingsReqDTO**](UpdateSettingsReqDTO.md)|  | |

### Return type

[**SettingsRespDTO**](SettingsRespDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

