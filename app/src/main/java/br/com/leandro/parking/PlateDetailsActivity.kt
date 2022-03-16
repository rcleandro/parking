package br.com.leandro.parking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.leandro.parking.databinding.ActivityPlateDetailsBinding
import br.com.leandro.parking.model.Historic

class PlateDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlateDetailsBinding
    private lateinit var historic: Historic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityPlateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historic = intent.extras!!["details"] as Historic

        binding.let {
            it.toolbarDetails.setNavigationOnClickListener { finish() }
            it.plateDetails.text = historic.plate
            it.statusDetails.text = if (historic.left) "—" else "Estacionado"
            it.paymentDetails.text = if (historic.paid) "Pago" else "—"
            it.currentTimeDetails.text = historic.time
                .replace("days", "dias ")
                .replace("hours", "h ")
                .replace("minutes", "min ")
                .replace("seconds", "seg ")
        }
    }
}