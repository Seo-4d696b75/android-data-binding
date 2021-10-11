package jp.co.yumemi.senda.bindingexample.ui.test

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import jp.co.yumemi.senda.bindingexample.R
import jp.co.yumemi.senda.bindingexample.databinding.FragmentSolutionBinding
import jp.co.yumemi.senda.bindingexample.ui.dataBinding
import jp.co.yumemi.senda.bindingexample.ui.useDataBinding

class SolutionFragment : Fragment(R.layout.fragment_solution) {

    private val testViewModel by activityViewModels<TestViewModel>()

    // solution 1: clear binding null manually when onDestroyView called
    private var binding1: FragmentSolutionBinding? = null

    // solution 2: clear binding reference when onDestroyView called by adding observer
    private val binding2 by dataBinding<FragmentSolutionBinding>()

    // solution 3: Not keep reference to binding by using property delegate
    private val binding3 by useDataBinding<FragmentSolutionBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSolutionBinding.bind(view)
        Log.d("solution1", "init binding@${System.identityHashCode(binding)} in onViewCreated")
        binding1 = binding
        binding.let {
            // other solution: Not keep reference to binding by not accessing to any view directly via binding
            it.viewModel = testViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        testViewModel.text.observe(viewLifecycleOwner) {
            binding1?.textBinding1?.text = it
            binding2.textBinding2.text = it
            binding3.textBinding3.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("solution1", "binding reference cleared in onDestroyView")
        binding1 = null
    }
}
