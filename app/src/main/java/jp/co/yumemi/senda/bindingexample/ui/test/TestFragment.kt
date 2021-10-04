package jp.co.yumemi.senda.bindingexample.ui.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import jp.co.yumemi.senda.bindingexample.R
import jp.co.yumemi.senda.bindingexample.databinding.FragmentTestBinding
import jp.co.yumemi.senda.bindingexample.ui.useViewBinding
import jp.co.yumemi.senda.bindingexample.ui.viewBinding

class TestFragment : Fragment(R.layout.fragment_test) {

    private val testViewModel by activityViewModels<TestViewModel>()

    private var binding1: FragmentTestBinding? = null
    private val binding2 by viewBinding<FragmentTestBinding>()
    private val binding3 by useViewBinding<FragmentTestBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TestFragment", "onViewCreated")
        binding1 = FragmentTestBinding.bind(view).also {
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
