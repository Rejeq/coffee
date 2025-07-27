package com.rejeq.sws.data.source.network

import android.util.Log
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.rejeq.sws.domain.repository.NetworkErrorKind
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.SerializationException
import okio.IOException
import retrofit2.HttpException

suspend inline fun <T> tryNetworkCall(
    block: suspend () -> T,
): Result<T, NetworkErrorKind> = try {
    Ok(block())
} catch (e: IOException) {
    Log.w(NETWORK_TAG, "Failed to execute network call", e)
    when (e) {
        is SocketTimeoutException -> Err(
            NetworkErrorKind.ConnectTimeoutException,
        )
        is UnknownHostException -> Err(NetworkErrorKind.UnresolvedAddress)
        is SSLException -> Err(NetworkErrorKind.ConnectionRefused)
        else -> Err(NetworkErrorKind.UnexpectedError)
    }
} catch (e: HttpException) {
    Log.w(NETWORK_TAG, "Failed to execute network call", e)
    Err(NetworkErrorKind.UnexpectedHttpError(e.code(), e.message()))
} catch (e: SerializationException) {
    Log.w(NETWORK_TAG, "Failed to execute network call", e)
    Err(NetworkErrorKind.MalformedData)
} catch (e: Exception) {
    when (e) {
        is CancellationException -> throw e
        else -> {
            Log.e(NETWORK_TAG, "Failed to execute network call", e)
            Err(NetworkErrorKind.UnexpectedError)
        }
    }
}

const val NETWORK_TAG = "NetworkUtils"
