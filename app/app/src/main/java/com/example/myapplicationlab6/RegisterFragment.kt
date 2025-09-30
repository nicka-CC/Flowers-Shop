package com.example.myapplicationlab6

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplicationlab6.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val password = binding.etPassword.text.toString()
            val passwordRepeat = binding.etPasswordRepeat.text.toString()
            val name = binding.etName.text.toString()
            val surname = binding.etSurname.text.toString()
            val phone = binding.etPhone.text.toString()
            val email = binding.etEmail.text.toString()
            if (password != passwordRepeat) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            performRegistration(email, password,name,surname, phone)
        }
    }
//TODO:confirm password
    private fun performRegistration(email: String, password: String, name: String, surname:String, phone:String) {

        val request = RegisterRequest(email, password,name,surname, phone)

        RetrofitClient.createApiService(requireContext()).register(request)
            .enqueue(object : Callback<RegisterResponse> {

                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    Log.d("RegisterResponse", "Code: ${response.code()}, Body: ${response.body()}, Error: ${response.errorBody()?.string()}")

                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
//                    if (response.isSuccessful) {
//                        response.body()?.message?.let { token ->
//                            val sharedPrefs = requireContext().getSharedPreferences(
//                                "auth_prefs",
//                                Context.MODE_PRIVATE
//                            )
//                            sharedPrefs.edit().putString("jwt_token", token).apply()
//                            performGetUser(sharedPrefs)
//                        }
//                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    val errorMessage = "Ошибка сети: ${t.localizedMessage}"
                    Log.e("RegisterError", errorMessage, t)
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun performGetUser(sharedPrefs: SharedPreferences) {
        RetrofitClient.createApiService(requireContext()).getUser()
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { user ->
                            with(sharedPrefs.edit()) {
                                putString("id", user.id)
                                putString("name", user.name)
                                putString("avatar", user.avatar)
                                apply()
                            }
                            (activity as? MainActivity)?.onLoginSuccess()

                        }
                    } else {
                        context?.let { ctx ->
                            Toast.makeText(
                                ctx,
                                "Ошибка получения данных пользователя",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    context?.let { ctx ->
                        Toast.makeText(ctx, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}
