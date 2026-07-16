package com.alemicode.amromovies.core.network

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SafeApiCallTest {

    @Test
    fun `successful call returns Success with data`() = runTest {
        val result = safeApiCall { "ok" }

        assertThat(result).isEqualTo(Result.Success("ok"))
    }

    @Test
    fun `UnknownHostException maps to NO_INTERNET`() = runTest {
        val result = safeApiCall<String> { throw UnknownHostException() }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.NO_INTERNET))
    }

    @Test
    fun `SocketTimeoutException maps to REQUEST_TIMEOUT`() = runTest {
        val result = safeApiCall<String> { throw SocketTimeoutException() }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.REQUEST_TIMEOUT))
    }

    @Test
    fun `SerializationException maps to SERIALIZATION`() = runTest {
        val result = safeApiCall<String> { throw SerializationException() }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.SERIALIZATION))
    }

    @Test
    fun `generic IOException maps to NO_INTERNET`() = runTest {
        val result = safeApiCall<String> { throw IOException() }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.NO_INTERNET))
    }

    @Test
    fun `unexpected exception maps to UNKNOWN`() = runTest {
        val result = safeApiCall<String> { throw IllegalStateException("boom") }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.UNKNOWN))
    }

    @Test
    fun `401 http error maps to UNAUTHORIZED`() = runTest {
        val result = safeApiCall<String> { throw httpException(401) }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.UNAUTHORIZED))
    }

    @Test
    fun `404 http error maps to NOT_FOUND`() = runTest {
        val result = safeApiCall<String> { throw httpException(404) }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.NOT_FOUND))
    }

    @Test
    fun `429 http error maps to TOO_MANY_REQUESTS`() = runTest {
        val result = safeApiCall<String> { throw httpException(429) }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.TOO_MANY_REQUESTS))
    }

    @Test
    fun `500 http error maps to SERVER_ERROR`() = runTest {
        val result = safeApiCall<String> { throw httpException(500) }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.SERVER_ERROR))
    }

    @Test
    fun `503 http error maps to SERVICE_UNAVAILABLE`() = runTest {
        val result = safeApiCall<String> { throw httpException(503) }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.SERVICE_UNAVAILABLE))
    }

    @Test
    fun `unmapped http error maps to UNKNOWN`() = runTest {
        val result = safeApiCall<String> { throw httpException(418) }

        assertThat(result).isEqualTo(Result.Error(DataError.Network.UNKNOWN))
    }

    private fun httpException(code: Int): HttpException {
        val body = "".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<String>(code, body))
    }
}
