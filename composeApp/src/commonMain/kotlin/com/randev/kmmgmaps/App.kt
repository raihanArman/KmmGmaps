package com.randev.kmmgmaps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.randev.kmmgmaps.network.data.User
import com.randev.kmmgmaps.network.response.ReqresResponse
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    viewModel: AppViewModel = viewModel { AppViewModel() },
) {
    MaterialTheme {
        var resultText by remember { mutableStateOf("") }
        val response by viewModel.stateData.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    viewModel.handleIntent(AppIntent.GetApi)
                }
            ) {
                Text("Click aku")
            }
            Spacer(modifier = Modifier.height(12.dp))

            with(response.reqresState) {
                onIdle {

                }
                onLoading {
                    LoadingContent()
                }
                onSuccess { response ->
                    ResponseContent(response)
                }
                onError { throwable ->
                    FailureContent(throwable)
                }
            }

            Text(resultText)
        }
    }
}

@Composable
fun LoadingContent() {
    CircularProgressIndicator()
}

@Composable
fun ResponseContent(user: User) {
    Text(
        text = user.name
    )
}

@Composable
fun FailureContent(throwable: Throwable) {
    Text(
        text = throwable.message.orEmpty(),
        color = Color.Red
    )
}