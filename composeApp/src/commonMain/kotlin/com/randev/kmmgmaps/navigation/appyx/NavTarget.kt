package com.randev.kmmgmaps.navigation.appyx

import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize

/**
 * @author Raihan Arman
 * @date 27/08/24
 */
sealed class NavTarget: Parcelable {
    @Parcelize
    data object Screen1: NavTarget()
    @Parcelize
    data class Screen2(val name: String): NavTarget()
    @Parcelize
    data object Screen3: NavTarget()

    @Parcelize
    data object GoogleMaps: NavTarget()

    @Parcelize
    data object Main: NavTarget()

    @Parcelize
    data object ReservedLocation: NavTarget()

    @Parcelize
    data object FeatureMaps: NavTarget()
}