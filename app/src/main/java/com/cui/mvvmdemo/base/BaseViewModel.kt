package com.cui.mvvmdemo.base


import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel

/**
 * @author xujiangang
 * @date 2017/6/16
 * MVVM BaseViewModel (ViewModel 不再持有 View，而是 store and manage UI-related data)
 * ViewModel objects are scoped to the Lifecycle passed to the ViewModelProvider when getting the ViewModel.
 * The ViewModel stays in memory until the Lifecycle it’s scoped to goes away permanently
 * —in the case of an activity, when it finishes;
 * in the case of a fragment, when it’s detached.
 * @see [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html)
 */
abstract class BaseViewModel : ViewModel(), IViewModel, LifecycleObserver {
    val TAG: String = this.javaClass.simpleName
    override fun onStart() {
        Log.i(this.javaClass.name,"onStart")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(this.javaClass.name,"onCleared")
    }

}
