package jp.co.yumemi.senda.bindingexample.ui.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class TestViewModel : ViewModel() {

    private val cnt = MutableLiveData<Int>(0)

    fun increment() {
        cnt.value?.let {
            cnt.value = it + 1
        }
    }

    val text: LiveData<String> = cnt.map { "This is TestFragment $it" }
}
