package su.elibrio.mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookActivityViewModel : ViewModel() {
    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> get() = _inProgress

    private val _isTitleVisible = MutableLiveData(false)
    val isTitleVisible: LiveData<Boolean> get() = _isTitleVisible

    fun hideTitle() = _isTitleVisible.postValue(false)

    fun showTitle() = _isTitleVisible.postValue(true)
}
