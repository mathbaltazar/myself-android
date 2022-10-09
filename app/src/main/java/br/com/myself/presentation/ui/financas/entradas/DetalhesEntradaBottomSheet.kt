package br.com.myself.presentation.ui.financas.entradas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.myself.application.viewmodel.DetalhesEntradaViewModel
import br.com.myself.databinding.BottomSheetDetalhesEntradaBinding
import br.com.myself.infrastructure.injectors.longSnackBar
import br.com.myself.infrastructure.injectors.provideEntradaRepo
import br.com.myself.presentation.ui.confirmation.CONFIRMATION_DIALOG_RESULT
import br.com.myself.presentation.ui.confirmation.ConfirmationDialogDirections
import br.com.myself.presentation.util.FRAGMENT_RESULT_IS_ENTRADA_EDITING
import br.com.myself.presentation.util.REQUEST_KEY_CARD_DETAILS_DELETE
import br.com.myself.presentation.util.REQUEST_KEY_ENTRADA_DETAILS_CLOSE
import br.com.myself.presentation.util.Utils
import br.com.myself.presentation.util.Utils.Companion.formattedDate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetalhesEntradaBottomSheet : BottomSheetDialogFragment() {
    
    private val viewModel: DetalhesEntradaViewModel
        by viewModels { DetalhesEntradaViewModel.Factory(provideEntradaRepo()) }
    private var isEditing: Boolean = false
    
    private var _binding: BottomSheetDetalhesEntradaBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetDetalhesEntradaBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        binding.buttonEditar.setOnClickListener {
            viewModel.onEditarClick()
        }
    
        binding.buttonExcluir.setOnClickListener {
            viewModel.onDeleteClick()
        }
        
        subscribeObservers()
    }
    
    private fun subscribeObservers() {
        
        viewModel.selectedEntrada.observe(viewLifecycleOwner) { entrada ->
            if (entrada != null) { // Retorno é null, quando deletado
                binding.textviewDescricao.text = entrada.descricao
                binding.textviewValor.text = Utils.formatCurrency(entrada.valor)
                binding.textviewData.text = entrada.data.formattedDate()
            }
        }
        
        viewModel.eventStreamLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                is DetalhesEntradaViewModel.Events.Edit -> {
                    isEditing = true
                    val title = "Editar Entrada"
                    val direction = DetalhesEntradaBottomSheetDirections.toEntradaFormDest(event.entrada, title)
                    findNavController().navigate(direction)
                }
                is DetalhesEntradaViewModel.Events.Delete -> {
                    val requestKey = REQUEST_KEY_CARD_DETAILS_DELETE
                    val msg = "Excluir entrada ?"
                    val direction = ConfirmationDialogDirections.toConfirm(requestKey, msg)
                    findNavController().navigate(direction)
                    setFragmentResultListener(requestKey) { _, bundle ->
                        val resultOk = bundle.getBoolean(CONFIRMATION_DIALOG_RESULT, false)
                        if (resultOk) { viewModel.excluirEFechar() }
                        clearFragmentResultListener(requestKey)
                    }
                }
                is DetalhesEntradaViewModel.Events.Message -> longSnackBar(event.text.toString())
                is DetalhesEntradaViewModel.Events.CloseDetails -> findNavController().popBackStack()
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressIndicator.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }
    
    override fun dismiss() {
        setFragmentResult(
            REQUEST_KEY_ENTRADA_DETAILS_CLOSE, bundleOf(
            FRAGMENT_RESULT_IS_ENTRADA_EDITING to isEditing )
        )
        super.dismiss()
    }
    
    
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    
}
