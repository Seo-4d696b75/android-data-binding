package jp.co.yumemi.senda.bindingexample.ui

import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Bindingを保持するラッパークラス
 *
 * Fragment#onDestroyView に合わせて binding を null クリアする
 */
class BindingHolder<T: ViewDataBinding>(
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T>{

    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object: DefaultLifecycleObserver{
            override fun onDestroy(owner: LifecycleOwner) {
                binding = null
                Log.d("BindingHolder", "binding released")
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding ?: kotlin.run {
            val view = thisRef.view ?: throw IllegalStateException("view not inflated yet")
            val value = DataBindingUtil.bind<T>(view)!!
            value.lifecycleOwner = thisRef.viewLifecycleOwner
            binding = value
            Log.d("BindingHolder", "binding assigned")
            value
        }

    }
}

fun <T: ViewDataBinding> Fragment.viewBinding() = BindingHolder<T>(this)

/**
 * Binding への getter
 *
 * アクセスの度に binding を生成して返す（もしくは既に bind されているオブジェクトを返す）
 */
fun <T: ViewDataBinding> useViewBinding() = ReadOnlyProperty<Fragment, T> { thisRef, property ->
    val view = thisRef.view ?: throw IllegalStateException("view not inflated yet")
    val value = DataBindingUtil.bind<T>(view)!!
    value.lifecycleOwner = thisRef.viewLifecycleOwner
    Log.d("BindingUse", "created")
    value
}
