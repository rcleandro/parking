package br.com.leandro.parking.data

interface OperationCallback<T> {
    fun onSuccess(data: T)
    fun onError(error: String?)
}