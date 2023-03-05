package com.example.searchbarwithapicalls

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.searchbarwithapicalls.domain.MyViewModel
import com.example.searchbarwithapicalls.ui.theme.SearchbarWithApiCallsTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchbarWithApiCallsTheme {

//                val viewModel = hiltViewModel<MyViewModel>()

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    SearchAddressApp("Android")
                }
            }
        }
    }
}

@Composable
fun SearchAddressApp(name: String) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//        OutlinedTextField(value =, onValueChange =)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SearchbarWithApiCallsTheme {
        SearchAddressApp("Android")
    }
}