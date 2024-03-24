package com.example.api.spring_boot_kotlin_service.dto

data class UserSearchReqDto(

    val filterKey : SearchFilterKey,
    val filterValues: List<String>,
    val operator: FiltrationOperator,
)

enum class FiltrationOperator(private val strategy: String) {

    EQUALS("Equals"),
    GREATER_THAN_AND_EQUAL("GreaterThanAndEqual");

    fun getStrategyFunction(): String {
        return "selectWhereValue${this.strategy}"
    }
}
enum class SearchFilterKey {

    firstName,
    age

}
