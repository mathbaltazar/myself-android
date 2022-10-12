package br.com.myself.presentation.ui.financas.registros

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
import br.com.myself.databinding.FragmentRegistrosBinding
import br.com.myself.infrastructure.injectors.provideRegistroRepo
import br.com.myself.presentation.adapter.RegistroAdapter
import br.com.myself.presentation.ui.financas.state.RegistrosFragmentUIState
import br.com.myself.presentation.util.KEY_IS_REGISTRO_DETAILS_SHOWN
import br.com.myself.presentation.util.KEY_REGISTRO_ID
import br.com.myself.presentation.util.Utils.Companion.observe

class RegistrosFragment : Fragment(R.layout.fragment_registros) {

    private val viewModel: RegistrosViewModel by viewModels { RegistrosViewModel.Factory(provideRegistroRepo()) }

    private var _binding: FragmentRegistrosBinding? = null
    private val binding  get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistrosBinding.bind(view)

        // MONTH PAGE
        binding.buttonPageMesAnterior.setOnClickListener {
            viewModel.mesAnterior()
        }
        binding.buttonPageProximoMes.setOnClickListener {
            viewModel.proximoMes()
        }

        // AÇÃO BOTÃO PESQUISAR
        binding.buttonPesquisar.setOnClickListener {
            val direction = RegistrosFragmentDirections.toPesquisarRegistroDest()
            findNavController().navigate(direction)
        }

        // AÇÃO BOTÃO ADICIONAR (+)
        binding.buttonAdicionar.setOnClickListener {
            val direction = RegistrosFragmentDirections.toRegistroFormDest()
            findNavController().navigate(direction)
        }

        // RECYCLER VIEW
        binding.recyclerviewRegistros.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewRegistros.adapter = RegistroAdapter()

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
        binding.textViewTotalMes.text = state.totalGastos
        binding.textViewQuantidadeRegistros.text = state.quantidadeGastos
        binding.textViewMesAnoSelecionado.text = state.labelMesAnoSelecionado
        binding.textviewNenhumRegistroEncontrado.visibility = if (state.isEmpty) View.VISIBLE else View.GONE

        (binding.recyclerviewRegistros.adapter as RegistroAdapter).submitList(state.registros)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}