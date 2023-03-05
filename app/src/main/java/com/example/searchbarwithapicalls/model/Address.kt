package com.example.searchbarwithapicalls.model

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("Id")
    val id: String,
    @SerializedName("Highlight")
    val highlight: String,
    @SerializedName("Text")
    val addressLine1: String,
    @SerializedName("Cursor")
    val cursor: Int,
    @SerializedName("Description")
    val description: String,
    @SerializedName("Next")
    val next: String
)

data class AddressResponse(
    @SerializedName("Items")
    val items: List<Address>
)

 enum class IsEligible(val next: String){
    FIND("Find"),
    RETRIEVE("Retrieve")
}