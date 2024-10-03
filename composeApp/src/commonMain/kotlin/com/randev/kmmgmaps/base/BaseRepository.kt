package com.randev.kmmgmaps.base

import com.randev.kmmgmaps.network.State
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json

/**
 * @author Raihan Arman
 * @date 24/08/24
 */
abstract class BaseRepository {
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    suspend fun getHttpResponse(urlString: String): HttpResponse {
        return client.get(urlString)
    }

    inline fun <reified T, U>(suspend () -> HttpResponse).reduce (
        crossinline block: (T) -> State<U>
    ): Flow<State<U>> {
        return flow {
            val httpResponse = invoke()
            if (httpResponse.status.isSuccess()) {
                val data = httpResponse.body<T>()
                val dataState = block.invoke(data)
                emit(dataState)
            } else {
                val throwable = Throwable(httpResponse.bodyAsText())
                val state = State.Failure(throwable)
                emit(state)
            }
        }.onStart {
            emit(State.Loading)
        }.catch {
            val state = State.Failure(it)
            emit(state)
        }
    }
}