package com.example.searchbarwithapicalls

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.example.searchbarwithapicalls.domain.MyViewModel
import com.example.searchbarwithapicalls.model.Address
import com.example.searchbarwithapicalls.ui.theme.SearchbarWithApiCallsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchbarWithApiCallsTheme {

                val viewModel by viewModels<MyViewModel>()

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    SearchAddressApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun SearchAddressApp(viewModel: MyViewModel) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        val searchValue by viewModel.searchText.collectAsState()
        val searchText by viewModel.searchText.collectAsState()
        val isSearching by viewModel.isSearching.collectAsState()
        val searchList by viewModel.searchResults.collectAsState()

        Box(Modifier.fillMaxWidth()) {
            SearchField(
                optionList = searchList,
                label = "Address",
                searchText = searchText,
                isSearching = isSearching,
                viewModel::onSearchText
            )
        }

        OutlinedTextField(value = "Field 1", onValueChange = {})
        OutlinedTextField(value = "Field 1", onValueChange = {})
        OutlinedTextField(value = "Field 1", onValueChange = {})

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Button(onClick = {

        }) {
            Text(text = "Get API Data")
        }
    }
}

@Composable
fun SearchField(
    optionList: List<Address>,
    label: String,
    searchText: String,
    isSearching: Boolean,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(true) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    var selectedText by remember { mutableStateOf("") }

    val focusRequester = FocusRequester()

    Column() {

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            expanded = true,
            onDismissRequest = {
            }
        ) {

            OutlinedTextField(
                value = searchText,
                enabled = true,
                onValueChange = {
                    selectedText = it
                    onValueChange(it)
                    expanded = true
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
//                .focusRequester(focusRequester)
//                .onFocusChanged { state ->
//                    if (state.isFocused) {
//                        // Open the keyboard when the TextField is focused
//                        focusRequester.requestFocus()
//                        expanded = true
//                    }
//                },
                label = { Text(label) },
            )

            if (expanded) {
                if (isSearching) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                optionList.forEach { address ->
                    DropdownMenuItem(
                        onClick = {
                            println("Selected Address : ${address.addressLine1}")
                            selectedText = address.addressLine1
                            expanded = !expanded
                        }
                    ) {
                        Text(text = address.addressLine1)
                    }
                }
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SearchbarWithApiCallsTheme {

    }
}