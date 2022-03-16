package br.com.leandro.parking.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.leandro.parking.data.OperationCallback
import br.com.leandro.parking.model.Entrance
import br.com.leandro.parking.network.RemoteDataSource
import br.com.leandro.parking.util.VoidCallback

class PageViewModel : ViewModel() {

    private val index = MutableLiveData<Int>().apply { value = 0 }
    val enableButton = MutableLiveData<Boolean>().apply { value = false }
    val visibility = MutableLiveData<Boolean>().apply { value = false }
    val textButton = MutableLiveData<String>().apply { value = "" }
    val textImage = MutableLiveData<String>().apply { value = "" }

    private val dataSource = RemoteDataSource()
    var registeredUserCallback: VoidCallback? = null
    var notRegisteredUserCallback: VoidCallback? = null
    var error = MutableLiveData<String>().apply { value = "" }
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
    val registered = MutableLiveData<Boolean>().apply { value = false }

    var bundle = MutableLiveData<Bundle>().apply { value = null }

    var countdownTimer: CountDownTimer = object : CountDownTimer(3000L, 3000L) {
        override fun onFinish() {
            isLoading.postValue(false)
        }

        override fun onTick(p0: Long) {}
    }

    fun validatePlate(plate: String) {
        val platePattern = "[a-zA-Z]+-[0-9]+[a-zA-Z0-9]+[0-9]+[0-9]"
        val isValid = plate.matches(platePattern.toRegex())

        when {
            plate.length != 8 -> {
                enableButton.postValue(false)
                error.postValue("")
            }
            plate.length == 8 && isValid -> {
                enableButton.postValue(true)
                error.postValue("")
            }
            plate.length == 8 && !isValid -> {
                enableButton.postValue(false)
                error.postValue("Placa inválida")
            }
        }
    }

    fun setIndex(pageIndex: Int) {
        index.postValue(pageIndex)

        when (pageIndex) {
            1 -> {
                textButton.postValue("CONFIRMAR ENTRADA")
                visibility.postValue(false)
            }
            2 -> {
                textButton.postValue("PAGAMENTO")
                visibility.postValue(true)
            }
        }
    }

    fun getDialogText(type: PlaceholderFragment.Type): String {
        return when (type) {
            PlaceholderFragment.Type.PAYMENT -> "Confima o pagamento da placa abaixo?"
            PlaceholderFragment.Type.EXIT -> "Confirma a saída do veiculo da placa abaixo?"
        }
    }

    fun getButtonDialogText(type: PlaceholderFragment.Type): String {
        return when (type) {
            PlaceholderFragment.Type.PAYMENT -> "Confirmar"
            PlaceholderFragment.Type.EXIT -> "Liberar Saída"
        }
    }

    fun addRegistration(plate: String) {
        dataSource.addRegistration(plate, object : OperationCallback<Entrance> {
            override fun onSuccess(data: Entrance) {
                if (data.reservation == null) {
                    registered.postValue(false)
                    notRegisteredUserCallback?.invoke()
                } else {
                    registered.postValue(true)
                    textImage.postValue("REGISTRADO!")
                    registeredUserCallback?.invoke()
                    countdownTimer.start()
                }
            }

            override fun onError(error: String?) {
                error?.let { er ->
                    registered.postValue(false)
                    this@PageViewModel.error.postValue(er)
                }
            }
        })
    }

    fun registerPayment(plate: String) {
        dataSource.registerPayment(plate, object : OperationCallback<Int> {
            override fun onSuccess(data: Int) {
                if (data != 1) {
                    registered.postValue(false)
                    notRegisteredUserCallback?.invoke()
                } else {
                    registered.postValue(true)
                    textImage.postValue("PAGO!")
                    registeredUserCallback?.invoke()
                    countdownTimer.start()
                }
            }

            override fun onError(error: String?) {
                error?.let { er ->
                    registered.postValue(false)
                    this@PageViewModel.error.postValue(er)
                }
            }
        })
    }

    fun registerDeparture(plate: String) {
        dataSource.registerDeparture(plate, object : OperationCallback<Int> {
            override fun onSuccess(data: Int) {
                if (data != 1) {
                    registered.postValue(false)
                    notRegisteredUserCallback?.invoke()
                } else {
                    registered.postValue(true)
                    textImage.postValue("SAÍDA LIBERADA")
                    registeredUserCallback?.invoke()
                    countdownTimer.start()
                }
            }

            override fun onError(error: String?) {
                error?.let { er ->
                    registered.postValue(false)
                    this@PageViewModel.error.postValue(er)
                }
            }
        })
    }
}