package com.randev.kmmgmaps.navigation.appyx

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.components.backstack.ui.parallax.BackStackParallax
import com.bumble.appyx.interactions.gesture.GestureFactory
import com.bumble.appyx.navigation.composable.AppyxNavigationContainer
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.node
import com.randev.kmmgmaps.feature.googlemaps.GoogleMapsScreen
import com.randev.kmmgmaps.feature.googlesignin.GoogleSignInScreen
import com.randev.kmmgmaps.feature.main.MainScreen
import com.randev.kmmgmaps.feature.maps.MapsScreen
import com.randev.kmmgmaps.feature.reverse.ReversedLocationScreen
import com.randev.kmmgmaps.feature.search.SearchLocation
import com.randev.kmmgmaps.isAndroid
import com.randev.kmmgmaps.screen.Screen2
import com.randev.kmmgmaps.screen.Screen3

/**
 * @author Raihan Arman
 * @date 27/08/24
 */
class RootNode(
    nodeContext: NodeContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        model = BackStackModel(
            initialTarget = NavTarget.Main,
            savedStateMap = nodeContext.savedStateMap
        ),
        visualisation = {
            if (isAndroid) {
                BackStackFader(it, defaultAnimationSpec = spring())
            } else {
                BackStackParallax(it)
            }
        },
        gestureFactory = {
            if (isAndroid) {
                GestureFactory.Noop()
            } else {
                BackStackParallax.Gestures(it)
            }
        }
    )
): Node<NavTarget>(
    nodeContext = nodeContext,
    appyxComponent = backStack
) {
    @Composable
    override fun Content(modifier: Modifier) {
        val navigator = remember {
            object : Navigator {
                override fun navigate(navTarget: NavTarget) {
                    backStack.push(navTarget)
                }

                override fun back() {
                    backStack.pop()
                }
            }
        }

        CompositionLocalProvider(
            LocalNavigator provides navigator
        ) {
            AppyxNavigationContainer(
                appyxComponent = backStack
            )
        }
    }

    override fun buildChildNode(navTarget: NavTarget, nodeContext: NodeContext): Node<*> {
        return when(navTarget) {
            is NavTarget.Screen1 -> node(nodeContext) {
                BoxBackground {
//                    SearchLocation()
//                    Screen1()
                    ReversedLocationScreen()
                }
            }
            is NavTarget.Screen2 -> node(nodeContext) {
                BoxBackground {
                    Screen2(navTarget.name)
                }
            }
            is NavTarget.Screen3 -> node(nodeContext) {
                BoxBackground {
                    Screen3()
                }
            }

            NavTarget.GoogleMaps -> node(nodeContext) {
                BoxBackground {
                    GoogleMapsScreen()
                }
            }

            NavTarget.Main -> node(nodeContext) {
                BoxBackground {
                    MainScreen()
                }
            }

            NavTarget.ReservedLocation -> node(nodeContext) {
                BoxBackground {
                    ReversedLocationScreen()
                }
            }
            is NavTarget.FeatureMaps -> node(nodeContext) {
                BoxBackground {
                    MapsScreen()
                }
            }

            NavTarget.FeatureGoogleSignIn -> node(nodeContext) {
                BoxBackground {
                    GoogleSignInScreen()
                }
            }
        }
    }

    @Composable
    private fun BoxBackground(
        content: @Composable () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            content.invoke()
        }
    }
}