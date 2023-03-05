package com.example.searchbarwithapicalls

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        val searchValue by viewModel.searchText.collectAsState()
        val searchText by viewModel.searchText.collectAsState()
        val isSearching by viewModel.isSearching.collectAsState()
        val searchList by viewModel.searchResults.collectAsState()

        SearchField(
            optionList = searchList,
            label = "Address",
            searchText = searchText,
            isSearching = isSearching,
            viewModel::onSearchText
        )

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
    var expanded by remember { mutableStateOf(false) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

//    val icon = if (expanded)
//        Icons.Filled.KeyboardArrowUp
//    else
//        Icons.Filled.KeyboardArrowDown


    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .clickable { expanded = !expanded },
            label = { Text(label) },
//            trailingIcon = {
//                Icon(icon, "Drop Down Icon",
//                    Modifier.clickable { expanded = !expanded })
//            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            if (optionList.isNotEmpty()) {
                optionList.forEach { address ->
                    DropdownMenuItem(onClick = {
//                    searchText = address.addressLine1
                        println("Address Selected: " + address.addressLine1)
                        expanded = !expanded
                    }) {
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