package com.randev.kmmgmaps

import androidx.lifecycle.viewModelScope
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
 * @date 24/08/24
 */
sealed class AppIntent {
    data object GetApi: AppIntent()
}

data class AppModel(
    val reqresState: State<User> = State.Idle
)

class AppViewModel(
    private val networkRepository: ReqresUserRepository = ReqresUserRepository()
): BaseViewModel<AppModel, AppIntent>(AppModel()) {
    override fun handleIntent(appIntent: AppIntent) {
        when(appIntent) {
            AppIntent.GetApi -> {
                getApi()
            }
        }
    }

    private fun getApi() = viewModelScope.launch {
        networkRepository.getUser()
            .stateIn(this)
            .collectLatest { state ->
                updateModel { model ->
                    model.copy(
                        reqresState = state
                    )
                }
            }
    }

}