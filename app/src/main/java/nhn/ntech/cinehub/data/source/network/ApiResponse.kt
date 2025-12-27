package nhn.ntech.cinehub.data.source.network

sealed class ApiResponse<T>(
    val code: Int? = null,
    val data: T? = null,
    val message: String? = null
) {
    class Loading <T>: ApiResponse<T>()
    class Success<T>(code: Int, data: T): ApiResponse<T>(code = code, data = data)
    class Failure<T>(code: Int, message: String?): ApiResponse<T>(code = code, message = message)
}
