package com.randev.kmmgmaps.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
@Composable
fun Screen2(name: String) {
//    val navigationComponent = LocalNavigationComponent.current
    val navigator = LocalNavigator.current
    Column {
        Button(
            onClick = {
//                navigationComponent.back()
                navigator.back()
            }
        ) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text("Text screen 2: $name")

        Button(
            onClick = {
//                navigationComponent.navigate(Router.Screen3)
                navigator.navigate(NavTarget.Screen3)
            }
        ) {
            Text("Navigate to screen 3")
        }
    }
}