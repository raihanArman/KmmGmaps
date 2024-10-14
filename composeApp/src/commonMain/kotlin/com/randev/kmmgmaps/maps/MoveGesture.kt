package com.randev.kmmgmaps.maps

import com.randev.kmmgmaps.network.data.Coordinate

/**
 * @author Raihan Arman
 * @date 07/10/24
 */
sealed class MoveGesture {
    data object MoveNotStarted: MoveGesture()
    data object MoveStart: MoveGesture()
    data class MoveStop(val coordinate: Coordinate): MoveGesture()
}