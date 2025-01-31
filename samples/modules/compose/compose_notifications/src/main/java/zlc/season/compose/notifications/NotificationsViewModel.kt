package zlc.season.compose.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    init {
        viewModelScope.launch {
            delay(1500)
            _text.value = "This is text from notification viewModel"
        }
    }
}