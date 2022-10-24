package br.com.myself.presentation.ui.financas.expenses

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.myself.R
import br.com.myself.application.viewmodel.RegistrosViewModel
import br.com.myself.databinding.FragmentExpensesBinding
import br.com.myself.infrastructure.injectors.provideRegistroRepo
import br.com.myself.presentation.adapter.RegistroAdapter
import br.com.myself.presentation.ui.financas.state.RegistrosFragmentUIState
import br.com.myself.presentation.util.KEY_IS_REGISTRO_DETAILS_SHOWN
import br.com.myself.presentation.util.KEY_REGISTRO_ID
import br.com.myself.presentation.util.Utils.Companion.observe

class ExpensesFragment : Fragment(R.layout.fragment_expenses) {

    private val viewModel: RegistrosViewModel by viewModels { RegistrosViewModel.Factory(provideRegistroRepo()) }

    private var _binding: FragmentExpensesBinding? = null
    private val binding  get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExpensesBinding.bind(view)

        binding.buttonPreviousMonth.setOnClickListener {
            viewModel.mesAnterior()
        }
        binding.buttonNextMonth.setOnClickListener {
            viewModel.proximoMes()
        }

        /*// AÇÃO BOTÃO PESQUISAR
        binding.buttonPesquisar.setOnClickListener {
            val direction = RegistrosFragmentDirections.toPesquisarRegistroDest()
            findNavController().navigate(direction)
        }*/

        binding.resumeBoard.buttonAddExpense.setOnClickListener {
            val direction = ExpensesFragmentDirections.toRegistroFormDest()
            findNavController().navigate(direction)
        }

        binding.recyclerViewExpenses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExpenses.adapter = RegistroAdapter()

        registerObservers()
    }

    private fun registerObservers() {
        viewModel.uiBehaviorState.observe(viewLifecycleOwner) { currentState ->
            currentState.registroId?.let {
                findNavController().navigate(R.id.to_card_detalhes_registro_dest,
                        bundleOf(KEY_REGISTRO_ID to it))
                setFragmentResultListener(KEY_IS_REGISTRO_DETAILS_SHOWN) { _, bdl ->
                    if (bdl.getBoolean(KEY_IS_REGISTRO_DETAILS_SHOWN, false)) {
                        viewModel.detailsShown()
                    }
                }
                clearFragmentResult(KEY_IS_REGISTRO_DETAILS_SHOWN)
            }
        }

        viewModel.observeRegistroUIState(viewLifecycleOwner, ::setupView)
    }

    private fun setupView(state: RegistrosFragmentUIState) {
        binding.resumeBoard.textViewCurrentMonthAmount.text = state.totalGastos
        binding.resumeBoard.textViewCurrentMonthSettled.text = "$ 0,00" //state.totalPagoNoMes
        binding.resumeBoard.textViewMonthYearReference.text = state.labelMesAnoSelecionado
        binding.textViewNotFound.visibility = if (state.isEmpty) View.VISIBLE else View.GONE

        (binding.recyclerViewExpenses.adapter as RegistroAdapter).submitList(state.registros)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}