package com.randev.kmmgmaps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bumble.appyx.navigation.integration.NodeActivity
import com.bumble.appyx.navigation.integration.NodeHost
import com.bumble.appyx.navigation.platform.AndroidLifecycle
import com.randev.kmmgmaps.navigation.appyx.RootNode

class MainActivity : NodeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NodeHost(
                lifecycle = AndroidLifecycle(lifecycle),
                integrationPoint = appyxIntegrationPoint,
            ) {
                RootNode(it)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}