package br.com.leandro.parking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.leandro.parking.databinding.ActivityHistoricBinding
import br.com.leandro.parking.model.Historic
import java.io.Serializable

class HistoricActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricBinding
    private lateinit var viewModel: HistoricViewModel
    private lateinit var adapter: HistoricAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel = ViewModelProvider(this)[HistoricViewModel::class.java]

        binding = ActivityHistoricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        viewModel.bound(intent.extras!!["historic"] as Serializable)

        viewModel.listHistoric.observe(this) {
            val isEmpty = it.isNullOrEmpty()
            binding.errorHistoric.visibility = if (isEmpty) View.VISIBLE else View.GONE

            it?.let {
                setAdapter(it)
            }
        }

        viewModel.plate.observe(this) {
            it?.let {
                viewModel.getListHistory(it)
                binding.toolbar.title = "Placa $it"
            }
        }

        viewModel.error.observe(this) {
            it?.let {
                binding.errorHistoric.text = it
            }
        }

        viewModel.nonBlockingLoading.observe(this) {
            it?.let {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.swipe()
        }
    }

    private fun setAdapter(list: List<Historic>) {
        adapter = HistoricAdapter(list)
        adapter.onItemClickListener = { item -> startPlateDetails(item) }
        layoutManager = LinearLayoutManager(Activity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewHistoric.layoutManager = layoutManager
        binding.recyclerViewHistoric.adapter = adapter
    }

    private fun startPlateDetails(details: Historic) {
        val intent = Intent(this, PlateDetailsActivity::class.java)
        intent.putExtra("details", details)
        this.startActivity(intent)
    }
}