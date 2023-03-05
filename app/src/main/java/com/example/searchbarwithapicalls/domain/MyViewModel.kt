package com.example.searchbarwithapicalls.domain

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TransformedText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchbarwithapicalls.model.Address
import com.example.searchbarwithapicalls.model.AddressResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(val canadaPostApi: CanadaPostApi) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // Show progress of the API request
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchList: MutableStateFlow<List<Address>> = MutableStateFlow(emptyList())

    @OptIn(FlowPreview::class)
    val searchList = searchText
        .debounce(400)
        .onEach { _isSearching.update { true } }
        .combine(_searchList) { searchText, addressList ->
            if (searchText.isNotEmpty()) {
              withContext(Dispatchers.Main){
                  getApiData().items.filter {
                      it.next.contentEquals("Retrieve")
                  }
              }
            } else {
                emptyList()
            }
        }
        .onEach { _isSearching.update { false } }
        .flowOn(Dispatchers.Main)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _searchList.value
        )


    /**
     * @param text Provide text and that type in field and update SearchText state
     */
    fun onSearchText(text: String) {
        _searchText.value = text
    }

    // Create a function to make the API call
    private suspend fun getApiData(): AddressResponse = withContext(Dispatchers.IO) {
        canadaPostApi.findAddress(searchTerm = searchText.value, lastId = "1")
    }
}