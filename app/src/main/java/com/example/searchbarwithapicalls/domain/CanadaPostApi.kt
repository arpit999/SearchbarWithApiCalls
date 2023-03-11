package com.example.searchbarwithapicalls.domain

import com.example.searchbarwithapicalls.Constant
import com.example.searchbarwithapicalls.model.AddressResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CanadaPostApi {

    @GET("/AddressComplete/Interactive/Find/v2.10/json3ex.ws?")
    suspend fun findAddress(
        @Query("Country") country: String = "CAN",
        @Query("OrderBy") orderBy: String = "UserLocation",
        @Query("Key") key: String = Constant.CanadaPostKey,
        @Query("SearchTerm") searchTerm: String,
        @Query("SearchFor") searchFor: String = "Everything",
        @Query("LastId") lastId: String
    ): AddressResponse

    @GET("/AddressComplete/Interactive/RetrieveFormatted/v2.10/json3ex.ws?")
    suspend fun addressDetails(
        @Query("LanguagePreference") country: String = "en",
        @Query("Key") key: String = Constant.CanadaPostKey,
        @Query("Id") lastId: String
    )


}