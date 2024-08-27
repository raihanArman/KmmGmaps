package com.randev.kmmgmaps.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.randev.kmmgmaps.navigation.LocalNavigationComponent
import com.randev.kmmgmaps.navigation.LocalNavigationController
import com.randev.kmmgmaps.navigation.Router
import com.randev.kmmgmaps.navigation.Routers
import com.randev.kmmgmaps.navigation.appyx.LocalNavigator
import com.randev.kmmgmaps.navigation.appyx.NavTarget

/**
 * @author Raihan Arman
 * @date 26/08/24
 */
@androidx.compose.runtime.Composable
fun Screen1() {
//    val navigationComponent = LocalNavigationComponent.current
    var textName by remember { mutableStateOf("") }
    val navigator = LocalNavigator.current
    Column {
        Button(
            onClick = {}
        ) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text("Text screen 1")

        TextField(
            value = textName,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                textName = it
            }
        )

        Button(
            onClick = {
//                navigationComponent.navigate(Router.Screen2)
                navigator.navigate(
                    NavTarget.Screen2(textName)
                )
            }
        ) {
            Text("Navigate to screen 2")
        }
    }
}