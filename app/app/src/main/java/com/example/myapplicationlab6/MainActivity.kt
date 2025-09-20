package com.example.myapplicationlab6

import SettingsViewModel
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.myapplicationlab6.databinding.ActivityMainBinding
import androidx.activity.viewModels



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val settingsVm: SettingsViewModel by viewModels {
        SettingsViewModel.factory(RetrofitClient.createApiService(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isDarkTheme = prefs.getBoolean("is_dark_theme", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val sharedPrefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("jwt_token", null)

        if (token != null) {
                onLoginSuccess()

        }
        else {
            navController.setGraph(R.navigation.navigation_graph, Bundle().apply {
                putBoolean("isAuth", false)
            })
            navController.navigate(R.id.authFragment)
        }

    }

    override fun onResume() {
        super.onResume()
    }


    fun onLoginSuccess() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val sharedPrefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val name = sharedPrefs.getString("name", "noname")


        settingsVm.loadCurrentUser()
        settingsVm.avatarUrl.observe(this) { url ->
            if (url.isNullOrEmpty()) {
                binding.userAvatar.setImageResource(R.drawable.ic_placeholder_avatar)
            } else {
                Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_placeholder_avatar)
                    .error(R.drawable.ic_placeholder_avatar)
                    .circleCrop()
                    .into(binding.userAvatar)
            }
        }
        val avatarUrl = sharedPrefs.getString("user_avatar_url", null)
        if (!avatarUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_placeholder_avatar)
                .error(R.drawable.ic_placeholder_avatar)
                .circleCrop()
                .into(binding.userAvatar)
        }

        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.userInfo.visibility = View.VISIBLE
        binding.userName.text = name

        val currentDestination = navController.currentDestination?.id

        binding.bottomNavigationView.setupWithNavController(navController)

        if (currentDestination == R.id.authFragment) {
            navController.navigate(R.id.action_authFragment_to_chatsFragment)
            }

        binding.logout.setOnClickListener() {
            val sharedPrefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().clear().apply()

            binding.bottomNavigationView.visibility = View.GONE
            binding.userInfo.visibility = View.GONE

            navController.navigate(R.id.authFragment)
        }
    }

    fun applyTheme(isDarkMode: Boolean) {
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("is_dark_theme", isDarkMode)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        recreate() // Перезапускаем активность для применения изменений
    }
}


