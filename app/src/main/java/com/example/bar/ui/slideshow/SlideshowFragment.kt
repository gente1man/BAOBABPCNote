package com.example.bar.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bar.FirebaseManager.getAndCreateCardsByViewName
import com.example.bar.FirebaseManager.getAndCreateCardsByViewNamePUB
import com.example.bar.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val message = arguments?.getString("message")

        // Отображение сообщения в Toast, если оно не пустое
        message?.let {
            getAndCreateCardsByViewName(message, requireContext(), binding.cards)
        }

        fun setupNameInputListener(context: Context, parentLayout: ConstraintLayout, editText: EditText) {
            // Устанавливаем слушатель на клавиатуру (когда пользователь нажимает Enter)
            editText.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val viewName = editText.text.toString().trim()
                    if (viewName.isNotEmpty()) {
                        // Вызов функции для загрузки карточек из публичных сборок
                        getAndCreateCardsByViewNamePUB(viewName, context, parentLayout)
                    } else {
                        Toast.makeText(context, "Имя не может быть пустым", Toast.LENGTH_SHORT).show()
                    }
                    true
                } else {
                    false
                }
            }
        }

        setupNameInputListener(requireContext(), binding.cards, binding.editTextText)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}