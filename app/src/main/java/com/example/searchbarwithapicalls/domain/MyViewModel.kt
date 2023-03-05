package com.example.searchbarwithapicalls.domain

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(val canadaPostApi: CanadaPostApi) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // Show progress of the API request
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    /**
     * @param text Provide text and that type in field and update SearchText state
     */
    fun onSearchText(text: String) {
        _searchText.value = text
    }

    fun searchAddressList() {

    }
}