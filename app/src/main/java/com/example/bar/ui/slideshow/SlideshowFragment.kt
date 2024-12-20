package com.example.bar.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.bar.FirebaseManager.getAndCreateCardsByViewName
import com.example.bar.databinding.FragmentSlideshowBinding
import com.example.bar.ui.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    // Используем ViewModel для доступа к Firebase
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)

        // Получение сообщения из аргументов
        val viewName = arguments?.getString("message") ?: ""

        // Используем полученное сообщение
        getAndCreateCardsByViewName(viewName, requireContext(), binding.cards)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
