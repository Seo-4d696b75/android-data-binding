package jp.co.yumemi.senda.bindingexample.ui.test

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import jp.co.yumemi.senda.bindingexample.R
import jp.co.yumemi.senda.bindingexample.databinding.FragmentProblemBinding

/**
 * @author Seo-4d696b75
 * @version 2021/10/11.
 */
class ProblemFragment : Fragment(R.layout.fragment_problem) {

    private val viewModel by activityViewModels<TestViewModel>()
    private lateinit var binding: FragmentProblemBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProblemBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ProblemFragment", "onDestroyView")
    }
}
