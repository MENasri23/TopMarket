package ir.jatlin.data.source.remote

import com.google.gson.Gson
import ir.jatlin.topmarket.core.data.source.remote.model.Error
import ir.jatlin.topmarket.core.data.source.remote.model.asError
import ir.jatlin.webservice.model.response.NetworkError
import retrofit2.Response
import javax.inject.Inject

class ResponseConverter @Inject constructor(
    private val gson: Gson
) {

    @Throws()
    operator fun <T> invoke(
        response: Response<T>
    ): T {
        return if (response.isSuccessful) {
            val body = response.body()
            when {
                response.code() in 200..201 && body != null -> body
                body == null -> throw NoBodyException()
                else -> throw Exception()
            }
        } else {
            val errorBody = response.errorBody()
            val networkError = gson.fromJson(errorBody?.charStream(), NetworkError::class.java)

            throw NetworkException(error = networkError.asError())
        }
    }

}


class NoBodyException(msg: String? = null) : Exception(msg)

class NetworkException(val error: Error, msg: String? = null) : Exception(msg)
