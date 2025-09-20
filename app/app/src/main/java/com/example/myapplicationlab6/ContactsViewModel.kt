package com.example.myapplicationlab6


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplicationlab6.Contact // или укажи актуальный пакет
import com.example.myapplicationlab6.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


class ContactsViewModel(private val apiService: ApiService) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Contact>>()
    val searchResults: LiveData<List<Contact>> = _searchResults

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    // Debounce по вводу
    val onSearchQuery: (String) -> Unit = debounce(300L, viewModelScope) { q ->
        Log.d("ContactsVM", "Debounced query: $q")
        viewModelScope.launch {
            _isSearching.value = true
            try {
                _searchResults.postValue(apiService.searchUsers(q))
            } catch (e: Exception) {
                _searchResults.postValue(emptyList())
            } finally {
                _isSearching.postValue(false)
            }
        }
    }

    // Отправка запроса — throttle
    private val sendReqThrottle = throttleFirst<String>(1000L, viewModelScope) { userId ->
        viewModelScope.launch {
            try {
                apiService.sendContactRequest(AcceptDeclineRequest(userId))
            }
            catch (e: Exception) {

            }
        }
    }
    fun sendContactRequest(user: Contact) {
        sendReqThrottle(user.id)
    }

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun updateContacts() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getContacts()
                _contacts.postValue(response)
            } catch (e: Exception) {
                _contacts.postValue(emptyList()) // можно добавить логгирование
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private val _requests = MutableLiveData<List<ContactRequest>>()
    val requests: LiveData<List<ContactRequest>> get() = _requests

    fun loadIncomingRequests() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = apiService.getIncomingRequests()
                _requests.postValue(result)
            } catch (e: Exception) {
                _requests.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private var lastRequestTime = 0L
    private val throttleMs = 1000L

    private fun canProceed(): Boolean {
        val now = System.currentTimeMillis()
        if (now - lastRequestTime >= throttleMs) {
            lastRequestTime = now
            return true
        }
        return false
    }

    fun acceptRequest(request: String) {
        if (!canProceed()) {
            return
        }

        viewModelScope.launch {
            try {

                apiService.acceptRequest(AcceptDeclineRequest(request))
                loadIncomingRequests()
                updateContacts()
            } catch (e: Exception) {
                Log.e("ContactsViewModel", "Ошибка принятия", e)
            }
        }

    }



    fun declineRequest(request: String) {
        if (!canProceed()) return

        viewModelScope.launch {
            try {
                apiService.declineRequest(AcceptDeclineRequest(request))
                loadIncomingRequests()
            } catch (e: Exception) {
                // логирование/ошибка
            }
        }
    }

    private fun <T> debounce(
        waitMs: Long,
        coroutineScope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }

    private fun <T> throttleFirst(
        skipMs: Long,
        coroutineScope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var lastJob: Job? = null
        return { param: T ->
            if (lastJob?.isCompleted != false) {
                lastJob = coroutineScope.launch {
                    destinationFunction(param)
                    delay(skipMs)
                }
            }
        }
    }


    companion object {
        fun getViewModelFactory(apiService: ApiService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { ContactsViewModel(apiService) }
            }
    }
}
