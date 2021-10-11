# android-data-binding
DataBindingを安全・簡潔に利用するための拡張関数の実装と検証

## 既存の問題
- メモリリークの可能性  
  [FragmentでViewの参照を持つとメモリリークする話と実装](https://satoshun.github.io/2020/01/fragment-view-memory-leak/)  
  [Avoiding memory leaks when using Data Binding and View Binding](https://proandroiddev.com/avoiding-memory-leaks-when-using-data-binding-and-view-binding-3b91d571c150)
- BindingへのLifecycleOwner登録忘れ

問題の本質は異なるスコープ（ライフサイクル）を持つFragment(Activity)とViewが参照を持つため  
[Fragments, ViewBinding and memory leaks](https://lordraydenmk.github.io/2020/view-binding/)  
> The problem here is: a component with a larger scope (the fragment) keeps a reference to a component with a smaller scope (the binding).

## 単純な解決方法
### **Solution 1**  
`Fragment#onDestroyView`でnullにクリアする  
[Android Developer でも紹介されている方法](https://developer.android.com/topic/libraries/view-binding?hl=ja#fragments)

## 拡張関数の利用

以下のような簡潔な記述のみで安全に利用できるようになる
```kotlin
private val binding: FragmentHogeBinding by dataBinding()
```

`Fragment#onDestroyView`のタイミング以降でBindingへの参照を保持し続けるのが問題となるので,  

### **Solutin 2**  
Observerを登録して参照をnullクリアする

```kotlin
class BindingHolder<T : ViewDataBinding>(
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                binding = null
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
            value
        }

    }
}
```

### **Solution 3**  
property delegateを利用して参照を保持しないようにする  

delegateを通じて`getValue`が呼ばれる度にbindする
```kotlin
fun <T : ViewDataBinding> Fragment.useDataBinding() =
    ReadOnlyProperty<Fragment, T> { thisRef, property ->
        val view = thisRef.view
            ?: throw IllegalStateException("view not inflated yet, or already destroyed")
        val value = requireNotNull(DataBindingUtil.bind<T>(view))
        value.lifecycleOwner = thisRef.viewLifecycleOwner
        value
    }
```


### 実験
`app`モジュールに簡単なアプリを作成  
<img src="https://user-images.githubusercontent.com/25225028/136740191-54bb906b-51b5-4f3f-9560-e11e86f81028.png" width="200">

ログを確認すると`DataBindingUtil#bind`によって同一のBindingインスタンスが返ってくるのが分かる

Logcat
```txt
2021-10-11 14:40:29.228 6296-6296/jp.co.yumemi.senda.bindingexample D/solution1: init binding@141780053 in onViewCreated
2021-10-11 14:40:29.235 6296-6296/jp.co.yumemi.senda.bindingexample D/solution2: binding@141780053 assigned
2021-10-11 14:40:29.238 6296-6296/jp.co.yumemi.senda.bindingexample D/solution3: binding@141780053 bound and returned
2021-10-11 14:40:30.608 6296-6296/jp.co.yumemi.senda.bindingexample D/solution3: binding@141780053 bound and returned
2021-10-11 14:40:31.487 6296-6296/jp.co.yumemi.senda.bindingexample D/solution3: binding@141780053 bound and returned
2021-10-11 14:40:33.248 6296-6296/jp.co.yumemi.senda.bindingexample D/solution1: binding reference cleared in onDestroyView
2021-10-11 14:40:33.258 6296-6296/jp.co.yumemi.senda.bindingexample D/solution2: binding released
```
