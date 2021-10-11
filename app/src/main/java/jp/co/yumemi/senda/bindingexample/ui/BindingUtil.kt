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
class BindingHolder<T : ViewDataBinding>(
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                binding = null
                Log.d("solution2", "binding released")
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding ?: kotlin.run {
            val view = thisRef.view
                ?: throw IllegalStateException("view not inflated yet, or already destroyed")
            val value = requireNotNull(DataBindingUtil.bind<T>(view))
            value.lifecycleOwner = thisRef.viewLifecycleOwner
            binding = value
            Log.d("solution2", "binding@${System.identityHashCode(value)} assigned")
            value
        }

    }
}

/**
 * Solution 2
 *
 * Observerを登録して `Fragment#onDestroyView`のタイミングでBindingへの参照を切る
 */
fun <T : ViewDataBinding> Fragment.dataBinding() = BindingHolder<T>(this)

/**
 * Solution 3
 *
 * 参照は保持セスアクセスの度に bind して返す
 * `DataBindingUtil#bind` 内部でbindingがキャッシュされているので毎回インスタンスを生成するわけではない
 */
fun <T : ViewDataBinding> Fragment.useDataBinding() =
    ReadOnlyProperty<Fragment, T> { thisRef, property ->
        val view = thisRef.view
            ?: throw IllegalStateException("view not inflated yet, or already destroyed")
        val value = requireNotNull(DataBindingUtil.bind<T>(view))
        value.lifecycleOwner = thisRef.viewLifecycleOwner
        Log.d("solution3", "binding@${System.identityHashCode(value)} bound and returned")
        value
    }
