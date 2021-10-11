package jp.co.yumemi.senda.bindingexample.ui.test

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import jp.co.yumemi.senda.bindingexample.R
import jp.co.yumemi.senda.bindingexample.databinding.FragmentSolutionBinding
import jp.co.yumemi.senda.bindingexample.ui.useViewBinding
import jp.co.yumemi.senda.bindingexample.ui.viewBinding

class SolutionFragment : Fragment(R.layout.fragment_solution) {

    private val testViewModel by activityViewModels<TestViewModel>()

    private var binding1: FragmentSolutionBinding? = null
    private val binding2 by viewBinding<FragmentSolutionBinding>()
    private val binding3 by useViewBinding<FragmentSolutionBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TestFragment", "onViewCreated")
        binding1 = FragmentSolutionBinding.bind(view).also {
            it.viewModel = testViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        testViewModel.text.observe(viewLifecycleOwner){
            binding1?.textBinding1?.text = it
            binding2.textBinding2.text = it
            binding3.textBinding3.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TestFragment", "onDestroyView")
        binding1 = null
    }
}
