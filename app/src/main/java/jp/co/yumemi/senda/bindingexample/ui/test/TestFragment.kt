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

class TestFragment : Fragment(R.layout.fragment_test) {

    private val testViewModel by activityViewModels<TestViewModel>()

    private var binding: FragmentTestBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TestFragment", "onViewCreated")
        binding = FragmentTestBinding.bind(view).also {
            it.viewModel = testViewModel
            it.lifecycleOwner = this
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TestFragment", "onDestroyView")
        binding = null
    }
}
