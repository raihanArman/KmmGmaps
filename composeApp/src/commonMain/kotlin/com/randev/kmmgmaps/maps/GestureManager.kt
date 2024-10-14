package com.randev.kmmgmaps.maps

import com.randev.kmmgmaps.network.data.Coordinate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author Raihan Arman
 * @date 07/10/24
 */
class GestureManager(
    private val debounce: Long = 600
) {
    private val coordinate = MutableStateFlow(Coordinate())

    private val _gesture: MutableStateFlow<MoveGesture> = MutableStateFlow(MoveGesture.MoveStart)
    val gesture: MutableStateFlow<MoveGesture> get() = _gesture

    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.IO

    }

    init {
        scope.launch {
            coordinate
                .debounce(debounce)
                .filter { it.latitude != 0.0 }
                .collectLatest { coordinate ->
                    val prevGesture = _gesture.value
                    if (prevGesture == MoveGesture.MoveStart) {
                        _gesture.update {
                            MoveGesture.MoveStop(coordinate)
                        }
                    }
                }
        }
    }

    fun setIsMoving(isMoving: Boolean) {
        if (isMoving) {
            _gesture.update {
                MoveGesture.MoveStart
            }
        }
    }

    fun setCoordinate(coordinate: Coordinate) {
        this.coordinate.update {
            coordinate
        }
    }
}