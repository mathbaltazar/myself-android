package br.com.myself.presentation.ui.financas.registros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.myself.presentation.viewmodel.DetalhesRegistroViewModel
import br.com.myself.databinding.BottomSheetDetalhesRegistroBinding
import br.com.myself.infrastructure.injectors.provideRegistroRepo
import br.com.myself.presentation.ui.confirmation.CONFIRMATION_DIALOG_RESULT
import br.com.myself.presentation.util.KEY_IS_REGISTRO_DETAILS_SHOWN
import br.com.myself.presentation.util.REQUEST_KEY_CARD_DETAILS_DELETE
import br.com.myself.presentation.util.Utils
import br.com.myself.presentation.util.Utils.Companion.formattedDate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetalhesRegistroBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: DetalhesRegistroViewModel by viewModels {
        DetalhesRegistroViewModel.Factory(provideRegistroRepo())
    }
    private var isEditing = false

    private var _binding: BottomSheetDetalhesRegistroBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDetalhesRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.buttonEditar.setOnClickListener {
            viewModel.onEditButtonClick()
        }
        binding.buttonExcluir.setOnClickListener {
            viewModel.onDeleteButtonClick()
        }
    
        subscribeObservers()
        isEditing = false
    }
    
    private fun subscribeObservers() {
        viewModel.registro.observe(viewLifecycleOwner) { registro ->
            if (registro != null) {
                binding.textviewDescricao.text = registro.descricao
                binding.textviewData.text = registro.data.formattedDate()
                binding.textviewValor.text = Utils.formatCurrency(registro.valor)
                binding.textviewOutros.text = registro.outros
            }
        }
    
        viewModel.eventStream.observe(viewLifecycleOwner) { event ->
            when (event) {
                is DetalhesRegistroViewModel.Event.OnEdit -> {
                    isEditing = true
                    val title = "Editar Registro"
                    val direction = RegistrosFragmentDirections.toRegistroFormDest(event.registro, title)
                    findNavController().navigate(direction)
                }
                is DetalhesRegistroViewModel.Event.OnDelete -> {
                    val requestKey = REQUEST_KEY_CARD_DETAILS_DELETE
                    val msg = "Excluir o Registro ?"
                    val direction = DetalhesRegistroBottomSheetDirections.toConfirm(requestKey, msg)
                    findNavController().navigate(direction)
                    setFragmentResultListener(requestKey) { _, bdl ->
                        val confirm = bdl.getBoolean(CONFIRMATION_DIALOG_RESULT, false)
                        if (confirm) {
                            viewModel.deleteRegistro()
                            findNavController().navigateUp()
                        }
                        clearFragmentResult(CONFIRMATION_DIALOG_RESULT)
                    }
                }
            }
        }
    }
    
    override fun onDestroyView() {
        _binding = null
        setFragmentResult(
            KEY_IS_REGISTRO_DETAILS_SHOWN,
            bundleOf(KEY_IS_REGISTRO_DETAILS_SHOWN to !isEditing))
        super.onDestroyView()
    }
}
