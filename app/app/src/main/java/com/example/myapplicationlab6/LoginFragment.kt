package com.example.myapplicationlab6

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplicationlab6.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val api by lazy { RetrofitClient.createApiService(requireContext()) }
    private val prefs: SharedPreferences
        get() = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener {
            val identifier = binding.etLogin.text.toString().trim()
            val password = binding.etPassword.text.toString()
            if (identifier.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите логин и пароль", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(identifier, password)
            }
        }
    }

    private fun performLogin(identifier: String, password: String) {
        val request = LoginRequest(identifier, password)

        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // фрагмент мог быть уже отсоединён — проверяем
                if (!isAdded) return

                if (response.isSuccessful) {
                    val token = response.body()?.access_token
                    if (token != null) {
                        prefs.edit().putString("jwt_token", token).apply()
//                        fetchCurrentUser()
                        Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
                        (activity as? MainActivity)?.onLoginSuccess()
                    } else {
                        Toast.makeText(requireContext(), "Пустой токен", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(requireContext(), "Ошибка входа: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                if (!isAdded) return
                Toast.makeText(requireContext(), "Ошибка сети: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCurrentUser() {
        api.getUser().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (!isAdded) return

                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        prefs.edit()
                            .putString("id", user.id)
                            .putString("name", user.name)
                            .putString("avatar", user.avatar)
                            .apply()
                        (activity as? MainActivity)?.onLoginSuccess()
                    }
                } else {
                    Toast.makeText(requireContext(), "Не удалось получить профиль", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                if (!isAdded) return
                Toast.makeText(requireContext(), "Ошибка сети: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
