package br.com.leandro.parking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.leandro.parking.databinding.ItemHistoricBinding
import br.com.leandro.parking.model.Historic

class HistoricAdapter(
    private var historic: List<Historic>
) : RecyclerView.Adapter<HistoricAdapter.Holder>() {

    lateinit var onItemClickListener: (item: Historic) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemHistoricBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(historic[position])
    }

    override fun getItemCount(): Int {
        return historic.count()
    }

    class Holder(
        private val binding: ItemHistoricBinding,
        private val onItemClickListener: ((item: Historic) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var historic: Historic

        @SuppressLint("SetTextI18n")
        fun bind(historic: Historic) {
            this.historic = historic

            binding.root.setOnClickListener { onItemClickListener?.invoke(historic) }

            binding.paymentText.text = if (historic.paid) "Pago" else "—"
            binding.currentTimeText.text = historic.time
                .replace("days", "dias ")
                .replace("hours", "h ")
                .replace("minutes", "min ")
                .replace("seconds", "seg ")
        }
    }
}