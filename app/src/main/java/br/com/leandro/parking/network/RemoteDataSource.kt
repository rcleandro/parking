package br.com.leandro.parking.network

import br.com.leandro.parking.data.OperationCallback
import br.com.leandro.parking.model.Entrance
import br.com.leandro.parking.model.Historic
import br.com.leandro.parking.util.ErrorAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    fun addRegistration(plate: String, onCompleteCallback: OperationCallback<Entrance>) {
        val client = services()
        val call: Call<Entrance> = client.addRegistration(plate)

        call.enqueue(object : Callback<Entrance> {
            override fun onResponse(call: Call<Entrance>, response: Response<Entrance>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onCompleteCallback.onSuccess(it)
                        return
                    }
                } else {
                    response.errorBody()?.let {
                        val message = response.errorBody()?.string()
                        onCompleteCallback.onError(ErrorAdapter.adapt(message ?: ""))
                    }
                }
            }

            override fun onFailure(call: Call<Entrance>, t: Throwable) {
                onCompleteCallback.onError(t.message)
            }
        })
    }

    fun registerPayment(plate: String, onCompleteCallback: OperationCallback<Int>) {
        val client = services()
        val call: Call<Int> = client.registerPayment(plate)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    onCompleteCallback.onSuccess(1)
                    return
                } else {
                    response.errorBody()?.let {
                        val message = response.errorBody()?.string()
                        onCompleteCallback.onError(ErrorAdapter.adapt(message ?: ""))
                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                onCompleteCallback.onError(t.message)
            }
        })
    }

    fun registerDeparture(plate: String, onCompleteCallback: OperationCallback<Int>) {
        val client = services()
        val call: Call<Int> = client.registerDeparture(plate)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    onCompleteCallback.onSuccess(1)
                    return
                } else {
                    response.errorBody()?.let {
                        val message = response.errorBody()?.string()
                        onCompleteCallback.onError(ErrorAdapter.adapt(message ?: ""))
                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                onCompleteCallback.onError(t.message)
            }
        })
    }

    fun getHistory(plate: String, onCompleteCallback: OperationCallback<List<Historic>>) {
        val client = services()
        val call: Call<List<Historic>> = client.getHistory(plate)

        call.enqueue(object : Callback<List<Historic>> {
            override fun onResponse(
                call: Call<List<Historic>>,
                response: Response<List<Historic>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onCompleteCallback.onSuccess(it)
                        return
                    }
                } else {
                    response.errorBody()?.let {
                        val message = response.errorBody()?.string()
                        onCompleteCallback.onError(ErrorAdapter.adapt(message ?: ""))
                    }
                }
            }

            override fun onFailure(call: Call<List<Historic>>, t: Throwable) {
                onCompleteCallback.onError(t.message)
            }
        })
    }
}