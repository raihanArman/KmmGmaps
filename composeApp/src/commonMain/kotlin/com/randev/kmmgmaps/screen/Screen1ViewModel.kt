package com.randev.kmmgmaps.screen

import androidx.lifecycle.viewModelScope
import com.randev.kmmgmaps.AppIntent
import com.randev.kmmgmaps.base.BaseViewModel
import com.randev.kmmgmaps.network.ReqresUserRepository
import com.randev.kmmgmaps.network.State
import com.randev.kmmgmaps.network.data.User
import com.randev.kmmgmaps.network.response.ReqresResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author Raihan Arman
 * @date 24/09/24
 */

data class Screen1Data(
    val reqresState: State<User> = State.Idle
)

sealed class Screen1Intent {
    data object GetUser: Screen1Intent()
}

class Screen1ViewModel: BaseViewModel<Screen1Data, Screen1Intent>(Screen1Data()) {
    private val repository = ReqresUserRepository()

    override fun handleIntent(appIntent: Screen1Intent) {
        when(appIntent) {
            Screen1Intent.GetUser ->  {
//                getUser()
            }
        }
    }

    private fun getUser() = viewModelScope.launch {
        repository.getUser()
            .stateIn(this)
            .collectLatest { newRequestModel ->
                updateModel { model ->
                    model.copy(
                        reqresState = newRequestModel
                    )
                }
            }
    }


}