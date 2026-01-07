package nhn.ntech.cinehub.callbacks

interface DeleteCallBack {
    fun onSuccess()
    fun onFailed(message: String)
}