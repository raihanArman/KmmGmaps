package com.randev.kmmgmaps.base

import androidx.lifecycle.ViewModel
import com.randev.kmmgmaps.AppIntent
import com.randev.kmmgmaps.AppModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author Raihan Arman
 * @date 24/08/24
 */
abstract class BaseViewModel<MODEL, INTENT>(
    private val defaultModel: MODEL
): ViewModel() {
    private val mutableStateData: MutableStateFlow<MODEL> = MutableStateFlow(defaultModel)
    val stateData = mutableStateData.asStateFlow()

    abstract fun handleIntent(appIntent: INTENT)

    fun updateModel(block: (MODEL) -> MODEL) {
        mutableStateData.update(block)
    }

    fun restartState() {
        mutableStateData.value = defaultModel
    }

    fun data(check: (a: String) -> Unit) {

    }

    fun checl() {
        data { a ->
            val c = a
        }
    }
}