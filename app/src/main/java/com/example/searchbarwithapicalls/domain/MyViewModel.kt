package com.example.searchbarwithapicalls.domain

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TransformedText
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchbarwithapicalls.model.Address
import com.example.searchbarwithapicalls.model.AddressResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val canadaPostApi: CanadaPostApi,
    private val canadaPostRepo: CanadaPostRepo
) :
    ViewModel() {

    private val _selectionText = mutableStateOf("")

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // Show progress of the API request
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchList: MutableStateFlow<List<Address>> = MutableStateFlow(emptyList())

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchList = searchText
        .debounce(400)
        .onEach { _isSearching.update { true } }
        .combine(_searchList) { searchText, addressList ->
            if (searchText.isNotEmpty()) {
                try {
                    val apiData = getApiData()
                    apiData.items.filter {
                        it.next.contentEquals("Retrieve")
                    }
                } catch (e: Exception) {
                    Log.e("ApiCall", "Error making API call: ${e.message}")
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _searchList.value
        )


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Address>> =
        searchText
            .onEach { _isSearching.update { true } }
            .debounce(300)
            .distinctUntilChanged()
            .filter { it.isNotEmpty() }
            .flatMapLatest { searchTerm ->
                canadaPostRepo.searchAddress(searchTerm)
            }
            .catch { e ->
                Log.e("AddressSearch", "Error searching for addresses: ${e.message}")
            }
            .onEach { _isSearching.update { false } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                _searchList.value
            )


//        .flatMapLatest {
//        flow {
//            val apiData = getApiData()
//            emit(apiData)
//        }
//    }.zip(searchText) { apiData, otherData ->
//        // Combine the API data with the other data
//        "Response : ${apiData.items}, $otherData"
//    }.catch { e ->
//        // Handle any errors that occur during the API call
//        Log.e("ApiCall", "Error making API call: ${e.message}")
//    }.flowOn(Dispatchers.Main)


    /**
     * @param text Provide text and that type in field and update SearchText state
     */
    fun onSearchText(text: String) {
        _searchText.value = text
    }

    // Create a function to make the API call
    suspend fun getApiData(): AddressResponse = withContext(Dispatchers.Main) {
        canadaPostApi.findAddress(searchTerm = searchText.value, lastId = "1")
    }
}