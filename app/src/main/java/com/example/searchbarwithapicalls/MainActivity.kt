package com.example.searchbarwithapicalls

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        val searchValue by viewModel.searchText.collectAsState()
        val searchText by viewModel.searchText.collectAsState()
        val isSearching by viewModel.isSearching.collectAsState()
        val searchList by viewModel.searchResults.collectAsState()

        OutlinedTextField(value = "Field 1", onValueChange = {})
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(value = "Field 1", onValueChange = {})
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(value = "Field 1", onValueChange = {})
        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        SearchField(
            optionList = searchList,
            label = "Address",
            searchText = searchText,
            isSearching = isSearching,
            viewModel::onSearchText
        )

        OutlinedTextField(value = "Field 1", onValueChange = {})
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(value = "Field 1", onValueChange = {})
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(value = "Field 1", onValueChange = {})

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Button(onClick = {

        }) {
            Text(text = "Get API Data")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchField(
    optionList: List<Address>,
    label: String,
    searchText: String,
    isSearching: Boolean,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

//    val focusRequester = FocusRequester()

//    DropdownMenu(
//        modifier = Modifier
//            .fillMaxWidth()
//            .onGloballyPositioned { coordinates ->
//                //This value is used to assign to the DropDown the same width
//                textfieldSize = coordinates.size.toSize()
//            },
//        expanded = true,
////            properties = PopupProperties(usePlatformDefaultWidth = true),
//        onDismissRequest = {
//        }
//    ) {

    ExposedDropdownMenuBox(expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        OutlinedTextField(
            value = searchText,
            enabled = true,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text(label) },
        )

        if (expanded) {
//            if (isSearching) {
//                CircularProgressIndicator()
//            }

            if (optionList.isNotEmpty()) {

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    optionList.forEach { address ->
                        DropdownMenuItem(
                            onClick = {
                                println("Selected Address : ${address.addressLine1}")
//                        selectedText = address.addressLine1
                                onValueChange(address.addressLine1)
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

//    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyUI() {
    val contextForToast = LocalContext.current.applicationContext
    val listItems = arrayOf("Favorites", "Options", "Settings", "Share")

    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    // remember the selected item
    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }

    // box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = selectedItem,
            onValueChange = { selectedItem = it },
            label = { Text(text = "Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // filter options based on text field value
        val filteringOptions =
            listItems.filter { it.contains(selectedItem, ignoreCase = true) }

        if (filteringOptions.isNotEmpty()) {
            // menu
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // this is a column scope
                // all the items are added vertically
                filteringOptions.forEach { selectionOption ->
                    // menu item
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = selectionOption
                            Toast.makeText(contextForToast, selectedItem, Toast.LENGTH_SHORT).show()
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption)
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
        MyUI()
    }
}