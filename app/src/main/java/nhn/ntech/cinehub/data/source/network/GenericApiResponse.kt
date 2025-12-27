package nhn.ntech.cinehub.data.source.network

import android.util.Log
import retrofit2.Response

open class GenericApiResponse {
    private val TAG = "GenericApiResponse"

    suspend fun <T> apiCall(call: suspend () -> Response<T>): ApiResponse<T>{
        return try {
            val response = call()
            if (response.isSuccessful){
                val body = response.body()
                if (body != null){
                    ApiResponse.Success(code = response.code(), data = body)
                }else{
                    ApiResponse.Failure(code = response.code(), message = response.message())
                }
            }else{
                ApiResponse.Failure(code = response.code(), message = response.message())
            }
        }catch (e: Exception){
            Log.d(TAG, "apiCall: ${e.message}")
            ApiResponse.Failure(code = 500, message = e.message ?: "Unknown Error")
        }
    }
}