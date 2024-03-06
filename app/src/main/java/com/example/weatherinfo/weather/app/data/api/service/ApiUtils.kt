package com.example.weatherinfo.weather.app.data.api.service

import com.example.weatherinfo.weather.app.utils.DISCONNECTED
import com.example.weatherinfo.weather.app.utils.SOCKET_TIMEOUT
import com.example.weatherinfo.weather.app.utils.SOMETHING_WENT_WRONG
import com.example.weatherinfo.weather.app.utils.TIMEOUT
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException

object ApiUtils {

    suspend fun <T> makeApiCall(
        apiCall: suspend () -> Response<T>,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) response.body()?.let { onSuccess(it) }
            else onError(SOMETHING_WENT_WRONG)
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is SocketException -> DISCONNECTED
                is SocketTimeoutException -> SOCKET_TIMEOUT
                is InterruptedIOException -> TIMEOUT
                else -> SOMETHING_WENT_WRONG
            }
            onError(errorMessage)
        }
    }

    suspend fun <T, R> makeParallelApiCalls(
        apiCall1: suspend () -> Response<T>,
        apiCall2: suspend () -> Response<R>,
        onSuccess1: (T) -> Unit,
        onSuccess2: (R) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            coroutineScope {
                val response1 = async { apiCall1.invoke() }.await()
                val response2 = async { apiCall2.invoke() }.await()

                if (response1.isSuccessful && response2.isSuccessful) {
                    response1.body()?.let { onSuccess1(it) }
                    response2.body()?.let { onSuccess2(it) }
                } else onError(SOMETHING_WENT_WRONG)

            }
        } catch (e: Exception) {
            when (e) {
                is SocketException -> onError(DISCONNECTED)
                is SocketTimeoutException -> onError(SOCKET_TIMEOUT)
                is InterruptedIOException -> onError(TIMEOUT)
                else -> onError(SOMETHING_WENT_WRONG)
            }
        }
    }
}

