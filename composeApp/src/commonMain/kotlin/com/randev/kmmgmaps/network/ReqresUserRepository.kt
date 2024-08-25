package com.randev.kmmgmaps.network

import com.randev.kmmgmaps.base.BaseRepository
import com.randev.kmmgmaps.network.data.User
import com.randev.kmmgmaps.network.response.ReqresResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Raihan Arman
 * @date 24/08/24
 */
class ReqresUserRepository: BaseRepository() {
    fun getUser(): Flow<State<User>> {
        return suspend {
            getHttpResponse("https://reqres.in/api/users?page=2")
        }.reduce<ReqresResponse, User> {
            State.Success(ReqresMapper.mapResponseToUser(it))
        }

    }
}

