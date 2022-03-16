package br.com.leandro.parking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.leandro.parking.data.OperationCallback
import br.com.leandro.parking.model.Historic
import br.com.leandro.parking.network.RemoteDataSource
import br.com.leandro.parking.util.VoidCallback
import java.io.Serializable

class HistoricViewModel : ViewModel() {

    val listHistoric = MutableLiveData<List<Historic>>().apply { value = null }
    var plate = MutableLiveData<String>().apply { value = "" }

    private val dataSource = RemoteDataSource()
    var registeredUserCallback: VoidCallback? = null
    var notRegisteredUserCallback: VoidCallback? = null
    var error = MutableLiveData<String>().apply { value = "" }
    val registered = MutableLiveData<Boolean>().apply { value = false }
    val nonBlockingLoading = MutableLiveData<Boolean>().apply { value = false }

    fun bound(serializable: Serializable) {
        plate.postValue(serializable as String)
    }

    fun swipe() {
        getListHistory(plate.value!!)
    }

    fun getListHistory(plate: String) {
        nonBlockingLoading.postValue(true)
        dataSource.getHistory(plate, object : OperationCallback<List<Historic>> {
            override fun onSuccess(data: List<Historic>) {
                if (data.isNullOrEmpty()) {
                    notRegisteredUserCallback?.invoke()
                } else {
                    listHistoric.postValue(data)
                    nonBlockingLoading.postValue(false)
                    registeredUserCallback?.invoke()
                }
            }

            override fun onError(error: String?) {
                error?.let { er ->
                    registered.postValue(false)
                    nonBlockingLoading.postValue(false)
                    this@HistoricViewModel.error.postValue(er)
                }
            }
        })
    }
}