import android.app.Application
import android.content.Context
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplicationlab6.ApiService
import com.example.myapplicationlab6.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull      // <— этот импорт важен
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.util.UUID

class SettingsViewModel(
    application: Application,
    private val api: ApiService
) : AndroidViewModel(application) {

    private val _avatarUrl = MutableLiveData<String?>()
    val avatarUrl: LiveData<String?> = _avatarUrl

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val response: Response<UserResponse> = withContext(Dispatchers.IO) {
                    api.getUser().execute()
                }
                if (response.isSuccessful) {
                    _avatarUrl.postValue(response.body()?.avatar)
                } else {
                    _avatarUrl.postValue(null)
                }
            } catch (t: Throwable) {
                Log.e("SettingsVM", "load user failed", t)
                _avatarUrl.postValue(null)
            }
        }
    }

    fun updateAvatar(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            try {
                val mime = contentResolver.getType(uri) ?: return@launch
                val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: return@launch

                // преобразуем в RequestBody
                val requestBody = bytes.toRequestBody(mime.toMediaTypeOrNull())
                val ext = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(mime) ?: "jpg"

                val part = MultipartBody.Part.createFormData(
                    name = "file",
                    filename = "${UUID.randomUUID()}.$ext",
                    body = requestBody
                )

                val resp: Response<String> = withContext(Dispatchers.IO) {
                    api.updateAvatar(part)
                }

                if (resp.isSuccessful) {
                    resp.body()?.let { url ->
                        _avatarUrl.postValue(url)
                        getApplication<Application>()
                            .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("user_avatar_url", url)
                            .apply()
                    }
                } else {
                    Log.e("SettingsVM", "avatar upload error ${resp.code()}")
                }

                // и можно ещё раз обновить профиль
                loadCurrentUser()

            } catch (t: Throwable) {
                Log.e("SettingsVM", "avatar upload failed", t)
            }
        }
    }

    companion object {
        fun factory(api: ApiService): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as Application
                SettingsViewModel(app, api)
            }
        }
    }
}
