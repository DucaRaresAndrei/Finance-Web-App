

# TransactionDTO


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**type** | [**TypeEnum**](#TypeEnum) |  |  |
|**amount** | **BigDecimal** |  |  |
|**description** | **String** |  |  [optional] |
|**ibanSender** | **String** |  |  [optional] |
|**ibanReceiver** | **String** |  |  [optional] |
|**date** | **OffsetDateTime** |  |  |
|**fromOrTo** | **String** |  |  |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| INCOME | &quot;INCOME&quot; |
| EXPENSE | &quot;EXPENSE&quot; |



