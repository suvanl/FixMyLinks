package com.suvanl.fixmylinks.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.suvanl.fixmylinks.ui.theme.FixMyLinksTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Greeting(name = "Android")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FixMyLinksTheme {
        Greeting("Android")
    }
}