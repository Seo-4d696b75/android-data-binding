# android-data-binding
DataBindingを安全・簡潔に利用するための拡張関数の実装と検証

## 既存の問題
- メモリリークの可能性  
  [FragmentでViewの参照を持つとメモリリークする話と実装](https://satoshun.github.io/2020/01/fragment-view-memory-leak/)  
  [Avoiding memory leaks when using Data Binding and View Binding](https://proandroiddev.com/avoiding-memory-leaks-when-using-data-binding-and-view-binding-3b91d571c150)
- BindingへのLifecycleOwner登録忘れ

## 拡張関数の利用

以下のような簡潔な記述のみで安全に利用できるようになる
```kotlin
private val binding: FragmentHogeBinding by dataBinding()
```

`Fragment#onDestroyView`のタイミング以降でBindingへの参照を保持し続けるのが問題となるので,  
Observerを登録して参照をnullクリアする、property delegateを利用して参照を保持しないようにするなどの実装が考えられる
