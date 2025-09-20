package com.example.myapplicationlab6

import SettingsViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.myapplicationlab6.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val vm: SettingsViewModel by activityViewModels {
        SettingsViewModel.factory(
            RetrofitClient.createApiService(requireContext())
        )
    }

    // Регистрируем селектор
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            uri?.let { vm.updateAvatar(it, requireContext().contentResolver) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Подгружаем текущий аватар
        vm.avatarUrl.observe(viewLifecycleOwner) { url ->
            if (url.isNullOrEmpty()) {
                binding.imageAvatar.setImageResource(R.drawable.ic_placeholder_avatar)
            } else {
                Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_placeholder_avatar)
                    .error(R.drawable.ic_placeholder_avatar)
                    .circleCrop()
                    .into(binding.imageAvatar)
            }
        }
        vm.loadCurrentUser()

        // Клик по аватару — выбор изображения
        binding.imageAvatar.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(PickVisualMedia.ImageOnly)
            )
        }

        // Тёмная тема
        val prefs = requireActivity()
            .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        binding.themeSwitcher.isChecked = prefs.getBoolean("is_dark_theme", false)
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (activity as? MainActivity)?.applyTheme(checked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
