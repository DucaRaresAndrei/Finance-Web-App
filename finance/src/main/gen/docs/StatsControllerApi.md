# StatsControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getDashboardStats**](StatsControllerApi.md#getDashboardStats) | **GET** /stats |  |
| [**getDashboardStats1**](StatsControllerApi.md#getDashboardStats1) | **HEAD** /stats |  |
| [**getDashboardStats2**](StatsControllerApi.md#getDashboardStats2) | **POST** /stats |  |
| [**getDashboardStats3**](StatsControllerApi.md#getDashboardStats3) | **PUT** /stats |  |
| [**getDashboardStats4**](StatsControllerApi.md#getDashboardStats4) | **PATCH** /stats |  |
| [**getDashboardStats5**](StatsControllerApi.md#getDashboardStats5) | **DELETE** /stats |  |
| [**getDashboardStats6**](StatsControllerApi.md#getDashboardStats6) | **OPTIONS** /stats |  |


<a name="getDashboardStats"></a>
# **getDashboardStats**
> DashboardStatsDTO getDashboardStats(offset)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StatsControllerApi apiInstance = new StatsControllerApi(defaultClient);
    Integer offset = 0; // Integer | 
    try {
      DashboardStatsDTO result = apiInstance.getDashboardStats(offset);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatsControllerApi#getDashboardStats");
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
| **offset** | **Integer**|  | [optional] [default to 0] |

### Return type

[**DashboardStatsDTO**](DashboardStatsDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getDashboardStats1"></a>
# **getDashboardStats1**
> DashboardStatsDTO getDashboardStats1(offset)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StatsControllerApi apiInstance = new StatsControllerApi(defaultClient);
    Integer offset = 0; // Integer | 
    try {
      DashboardStatsDTO result = apiInstance.getDashboardStats1(offset);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatsControllerApi#getDashboardStats1");
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
| **offset** | **Integer**|  | [optional] [default to 0] |

### Return type

[**DashboardStatsDTO**](DashboardStatsDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getDashboardStats2"></a>
# **getDashboardStats2**
> DashboardStatsDTO getDashboardStats2(offset)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StatsControllerApi apiInstance = new StatsControllerApi(defaultClient);
    Integer offset = 0; // Integer | 
    try {
      DashboardStatsDTO result = apiInstance.getDashboardStats2(offset);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatsControllerApi#getDashboardStats2");
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
| **offset** | **Integer**|  | [optional] [default to 0] |

### Return type

[**DashboardStatsDTO**](DashboardStatsDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getDashboardStats3"></a>
# **getDashboardStats3**
> DashboardStatsDTO getDashboardStats3(offset)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StatsControllerApi apiInstance = new StatsControllerApi(defaultClient);
    Integer offset = 0; // Integer | 
    try {
      DashboardStatsDTO result = apiInstance.getDashboardStats3(offset);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatsControllerApi#getDashboardStats3");
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
| **offset** | **Integer**|  | [optional] [default to 0] |

### Return type

[**DashboardStatsDTO**](DashboardStatsDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getDashboardStats4"></a>
# **getDashboardStats4**
> DashboardStatsDTO getDashboardStats4(offset)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StatsControllerApi apiInstance = new StatsControllerApi(defaultClient);
    Integer offset = 0; // Integer | 
    try {
      DashboardStatsDTO result = apiInstance.getDashboardStats4(offset);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatsControllerApi#getDashboardStats4");
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
| **offset** | **Integer**|  | [optional] [default to 0] |

### Return type

[**DashboardStatsDTO**](DashboardStatsDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getDashboardStats5"></a>
# **getDashboardStats5**
> DashboardStatsDTO getDashboardStats5(offset)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StatsControllerApi apiInstance = new StatsControllerApi(defaultClient);
    Integer offset = 0; // Integer | 
    try {
      DashboardStatsDTO result = apiInstance.getDashboardStats5(offset);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatsControllerApi#getDashboardStats5");
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
| **offset** | **Integer**|  | [optional] [default to 0] |

### Return type

[**DashboardStatsDTO**](DashboardStatsDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getDashboardStats6"></a>
# **getDashboardStats6**
> DashboardStatsDTO getDashboardStats6(offset)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StatsControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StatsControllerApi apiInstance = new StatsControllerApi(defaultClient);
    Integer offset = 0; // Integer | 
    try {
      DashboardStatsDTO result = apiInstance.getDashboardStats6(offset);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StatsControllerApi#getDashboardStats6");
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
| **offset** | **Integer**|  | [optional] [default to 0] |

### Return type

[**DashboardStatsDTO**](DashboardStatsDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

