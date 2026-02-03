# FriendControllerApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addFriend**](FriendControllerApi.md#addFriend) | **POST** /friends/add |  |
| [**getFriends**](FriendControllerApi.md#getFriends) | **GET** /friends |  |
| [**searchFriends**](FriendControllerApi.md#searchFriends) | **POST** /friends/search |  |


<a name="addFriend"></a>
# **addFriend**
> FriendResponseDTO addFriend(addFriendRequestDTO)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FriendControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FriendControllerApi apiInstance = new FriendControllerApi(defaultClient);
    AddFriendRequestDTO addFriendRequestDTO = new AddFriendRequestDTO(); // AddFriendRequestDTO | 
    try {
      FriendResponseDTO result = apiInstance.addFriend(addFriendRequestDTO);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling FriendControllerApi#addFriend");
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
| **addFriendRequestDTO** | [**AddFriendRequestDTO**](AddFriendRequestDTO.md)|  | |

### Return type

[**FriendResponseDTO**](FriendResponseDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="getFriends"></a>
# **getFriends**
> List&lt;FriendResponseDTO&gt; getFriends()



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FriendControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FriendControllerApi apiInstance = new FriendControllerApi(defaultClient);
    try {
      List<FriendResponseDTO> result = apiInstance.getFriends();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling FriendControllerApi#getFriends");
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

[**List&lt;FriendResponseDTO&gt;**](FriendResponseDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="searchFriends"></a>
# **searchFriends**
> List&lt;FriendResponseDTO&gt; searchFriends(searchFriendRequestDTO)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FriendControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FriendControllerApi apiInstance = new FriendControllerApi(defaultClient);
    SearchFriendRequestDTO searchFriendRequestDTO = new SearchFriendRequestDTO(); // SearchFriendRequestDTO | 
    try {
      List<FriendResponseDTO> result = apiInstance.searchFriends(searchFriendRequestDTO);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling FriendControllerApi#searchFriends");
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
| **searchFriendRequestDTO** | [**SearchFriendRequestDTO**](SearchFriendRequestDTO.md)|  | |

### Return type

[**List&lt;FriendResponseDTO&gt;**](FriendResponseDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

