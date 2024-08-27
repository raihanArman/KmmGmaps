package com.randev.kmmgmaps

import androidx.compose.animation.AnimatedContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.randev.kmmgmaps.navigation.LocalNavigationComponent
import com.randev.kmmgmaps.navigation.LocalNavigationController
import com.randev.kmmgmaps.navigation.NavigationComponent
import com.randev.kmmgmaps.navigation.Router
import com.randev.kmmgmaps.navigation.Routers
import com.randev.kmmgmaps.network.data.User
import com.randev.kmmgmaps.network.response.ReqresResponse
import com.randev.kmmgmaps.screen.Screen1
import com.randev.kmmgmaps.screen.Screen2
import com.randev.kmmgmaps.screen.Screen3
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    viewModel: AppViewModel = viewModel { AppViewModel() },
) {
//    val navigationComponent = remember { NavigationComponent() }
//    val router by navigationComponent.stateRouter.collectAsState()
//    val navigationController = rememberNavController()

    MaterialTheme {


//        CompositionLocalProvider(
//            LocalNavigationController provides navigationController
//        ) {
//            NavHost(navController = navigationController, startDestination = Routers.SCREEN_1) {
//                composable(
//                    route = Routers.SCREEN_1
//                ) {
//                    Screen1()
//                }
//
//                composable(
//                    route = Routers.SCREEN_2,
//                    arguments = listOf(
//                        navArgument("name") {
//                            this.defaultValue = ""
//                        }
//                    )
//                ) {
//                    val name = it.arguments?.getString("name").orEmpty()
//                    Screen2(name)
//                }
//
//                composable(
//                    route = Routers.SCREEN_3
//                ) {
//                    Screen3()
//                }
//            }
//        }

//        CompositionLocalProvider(
////            LocalNavigationComponent provides navigationComponent
//        ) {
//            AnimatedContent(
//                targetState = router
//            ) {
//                when (router) {
//                    is Router.Screen1 -> {
//                        Screen1()
//                    }
//
//                    is Router.Screen2 -> {
//                        Screen2()
//                    }
//
//                    is Router.Screen3 -> {
//                        Screen2()
//                    }
//
//                    else -> {}
//                }
//            }
//        }

//        var resultText by remember { mutableStateOf("") }
//        val response by viewModel.stateData.collectAsState()
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Button(
//                onClick = {
//                    viewModel.handleIntent(AppIntent.GetApi)
//                }
//            ) {
//                Text("Click aku")
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//
//            with(response.reqresState) {
//                onIdle {
//
//                }
//                onLoading {
//                    LoadingContent()
//                }
//                onSuccess { response ->
//                    ResponseContent(response)
//                }
//                onError { throwable ->
//                    FailureContent(throwable)
//                }
//            }
//
//            Text(resultText)
//        }
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