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
import com.randev.kmmgmaps.navigation.appyx.LocalNavigator

/**
 * @author Raihan Arman
 * @date 26/08/24
 */
@Composable
fun Screen3() {
//    val navigationComponent = LocalNavigationComponent.current
    val navigator = LocalNavigator.current
    Column {
        Button(
            onClick = {
//                navigationComponent.back
                navigator.back()
            }
        ) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text("Text screen 3")
    }
}