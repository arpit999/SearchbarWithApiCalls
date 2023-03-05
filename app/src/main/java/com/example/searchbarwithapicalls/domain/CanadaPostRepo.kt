package com.example.searchbarwithapicalls.domain

import com.example.searchbarwithapicalls.model.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CanadaPostRepo @Inject constructor(private val canadaPostApi: CanadaPostApi) {

    suspend fun searchAddress(searchTerm: String): Flow<List<Address>> = flow {
        val response = canadaPostApi.findAddress(searchTerm = searchTerm, lastId = "1")
        // Only complete address will display on list
        emit(response.items.filter { it.next.contentEquals("Retrieve") })
    }

}