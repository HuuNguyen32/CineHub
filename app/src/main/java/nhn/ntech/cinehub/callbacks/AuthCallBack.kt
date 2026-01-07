package nhn.ntech.cinehub.callbacks

interface AuthCallBack {
    fun onSuccess()
    fun onFailed(message: String)
}
