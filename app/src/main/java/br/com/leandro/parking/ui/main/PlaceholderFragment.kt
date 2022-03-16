package br.com.leandro.parking.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.leandro.parking.HistoricActivity
import br.com.leandro.parking.R
import br.com.leandro.parking.databinding.FragmentMainBinding

class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.let {
            it.editText.filters = arrayOf(InputFilter.AllCaps())
            it.editText.doOnTextChanged { text, _, _, _ ->
                pageViewModel.validatePlate(text.toString())
            }
        }

        pageViewModel.enableButton.observe(viewLifecycleOwner) {
            binding.buttonOne.isEnabled = it
            binding.buttonExit.isEnabled = it
            binding.historic.isEnabled = it
        }

        pageViewModel.visibility.observe(viewLifecycleOwner) {
            binding.buttonExit.visibility = if (it) View.VISIBLE else View.GONE
            binding.historic.visibility = if (it) View.VISIBLE else View.INVISIBLE

            if (it) {
                binding.buttonOne.setOnClickListener { showDialog(Type.PAYMENT) }
                binding.buttonExit.setOnClickListener { showDialog(Type.EXIT) }
                binding.historic.setOnClickListener { startHistoric() }
            } else {
                binding.buttonOne.setOnClickListener {
                    setLoading()

                    val plate = binding.editText.text.toString()
                    pageViewModel.addRegistration(plate)
                }
            }

            binding.buttonOne.background =
                if (it) ContextCompat.getDrawable(requireContext(), R.drawable.button_background_purple)
                else ContextCompat.getDrawable(requireContext(), R.drawable.button_background_green)
        }

        pageViewModel.textButton.observe(viewLifecycleOwner) {
            binding.buttonOne.text = it
        }

        pageViewModel.textImage.observe(viewLifecycleOwner) {
            binding.textImage.text = it
        }

        pageViewModel.registered.observe(viewLifecycleOwner) {
            if (it) {
                setView(R.drawable.ic_check, R.string.registered)
            } else {
                pageViewModel.isLoading.postValue(false)
            }
        }

        pageViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.fragmentLayout.visibility = View.GONE
                binding.imageLayout.visibility = View.VISIBLE
            } else {
                binding.fragmentLayout.visibility = View.VISIBLE
                binding.imageLayout.visibility = View.GONE
            }
        }

        pageViewModel.error.observe(viewLifecycleOwner) {
            if (it.isNullOrBlank()) {
                binding.errorLayout.visibility = View.GONE
            } else {
                binding.errorLayout.visibility = View.VISIBLE
                binding.errorText.text = pageViewModel.error.value
            }
        }

        return root
    }

    private fun setView(image: Int, text: Int) {
        val drawable = AppCompatResources.getDrawable(requireContext(), image)
        binding.image.setImageDrawable(drawable)
        binding.textImage.text = requireActivity().getString(text)
    }

    private fun setLoading() {
        setView(R.drawable.ic_loading, R.string.registering)
        pageViewModel.error.postValue("")
        pageViewModel.isLoading.postValue(true)
    }

    private fun showDialog(type: Type) {
        setLoading()

        val alert = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.dialog_view, null, false) as View
        val plate = binding.editText.text.toString()
        val textDialog: TextView = view.findViewById(R.id.textDialog)
        val plateDialog: TextView = view.findViewById(R.id.plateDialog)
        val buttonOne: Button = view.findViewById(R.id.buttonOneDialog)
        val buttonBack: TextView = view.findViewById(R.id.buttonBack)

        plateDialog.text = plate
        textDialog.text = pageViewModel.getDialogText(type)
        buttonOne.text = pageViewModel.getButtonDialogText(type)

        alert.setView(view)

        val alertShow = alert.show()

        buttonOne.setOnClickListener {
            when (type) {
                Type.PAYMENT -> {
                    pageViewModel.registerPayment(plate)
                    alertShow.dismiss()
                }
                Type.EXIT -> {
                    pageViewModel.registerDeparture(plate)
                    alertShow.dismiss()}
            }
        }
        buttonBack.setOnClickListener {
            pageViewModel.registered.postValue(false)
            alertShow.dismiss()
        }
    }

    enum class Type {
        PAYMENT, EXIT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startHistoric() {
        val intent = Intent(activity, HistoricActivity::class.java)
        val plate = binding.editText.text.toString()
        intent.putExtra("historic", plate)
        requireActivity().startActivityForResult(intent, 50)
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}