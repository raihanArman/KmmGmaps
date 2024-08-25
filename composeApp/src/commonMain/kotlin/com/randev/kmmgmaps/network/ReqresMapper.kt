package com.randev.kmmgmaps.network

import com.randev.kmmgmaps.network.data.User
import com.randev.kmmgmaps.network.response.ReqresResponse

/**
 * @author Raihan Arman
 * @date 24/08/24
 */
object ReqresMapper {
    fun mapResponseToUser(reqresResponse: ReqresResponse): User {
        return User(
            name = reqresResponse.data?.firstOrNull()?.firstName.orEmpty()
        )
    }
}