package com.example.bar.ui.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.bar.Component
import com.example.bar.ComponentCardUtils
import com.example.bar.R
import com.example.bar.ComponentType
import com.example.bar.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Список компонентов и адаптер для Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Инициализируем Spinner


        // Создаем компонентную карточку
        ComponentCardUtils.createComponentCard(
            binding.button2, // Кнопка добавления
            binding.processorSpinner,         // Spinner
            binding.textView4, // TextView для отображения цены
            binding.imageView2, // Кнопка редактирования
            ComponentType.MB, // Тип компонента
            mutableListOf<Component>(),       // Список компонентов
            ComponentCardUtils.createAdapter(requireContext(), binding.processorSpinner)    // Адаптер Spinner
        )

        ComponentCardUtils.createComponentCard(
            binding.PButton2, // Кнопка добавления
            binding.PProcessorSpinner,         // Spinner
            binding.PTextView4, // TextView для отображения цены
            binding.PImageView2, // Кнопка редактирования
            ComponentType.CPU, // Тип компонента
            mutableListOf<Component>(),       // Список компонентов
            ComponentCardUtils.createAdapter(requireContext(), binding.PProcessorSpinner)    // Адаптер Spinner
        )

        ComponentCardUtils.createComponentCard(
            binding.GButton2, // Кнопка добавления
            binding.GProcessorSpinner,         // Spinner
            binding.GTextView4, // TextView для отображения цены
            binding.GImageView2, // Кнопка редактирования
            ComponentType.GPU, // Тип компонента
            mutableListOf<Component>(),       // Список компонентов
            ComponentCardUtils.createAdapter(requireContext(), binding.GProcessorSpinner)    // Адаптер Spinner
        )

        ComponentCardUtils.createComponentCard(
            binding.RButton2, // Кнопка добавления
            binding.RProcessorSpinner,         // Spinner
            binding.RTextView4, // TextView для отображения цены
            binding.RImageView2, // Кнопка редактирования
            ComponentType.RAM, // Тип компонента
            mutableListOf<Component>(),       // Список компонентов
            ComponentCardUtils.createAdapter(requireContext(), binding.RProcessorSpinner)    // Адаптер Spinner
        )

        ComponentCardUtils.createComponentCard(
            binding.FButton2, // Кнопка добавления
            binding.FProcessorSpinner,         // Spinner
            binding.FTextView4, // TextView для отображения цены
            binding.FImageView2, // Кнопка редактирования
            ComponentType.COOL, // Тип компонента
            mutableListOf<Component>(),       // Список компонентов
            ComponentCardUtils.createAdapter(requireContext(), binding.FProcessorSpinner)    // Адаптер Spinner
        )

        ComponentCardUtils.createComponentCard(
            binding.SButton2, // Кнопка добавления
            binding.SProcessorSpinner,         // Spinner
            binding.STextView4, // TextView для отображения цены
            binding.SImageView2, // Кнопка редактирования
            ComponentType.DISK, // Тип компонента
            mutableListOf<Component>(),       // Список компонентов
            ComponentCardUtils.createAdapter(requireContext(), binding.SProcessorSpinner)    // Адаптер Spinner
        )

        ComponentCardUtils.createComponentCard(
            binding.CASButton2, // Кнопка добавления
            binding.CASProcessorSpinner,         // Spinner
            binding.CASTextView4, // TextView для отображения цены
            binding.CASImageView2, // Кнопка редактирования
            ComponentType.CASE, // Тип компонента
            mutableListOf<Component>(),       // Список компонентов
            ComponentCardUtils.createAdapter(requireContext(), binding.CASProcessorSpinner)    // Адаптер Spinner
        )

        ComponentCardUtils.createComponentCard(
            binding.BPButton2, // Кнопка добавления
            binding.BPProcessorSpinner,         // Spinner
            binding.BPTextView4, // TextView для отображения цены
            binding.BPImageView2, // Кнопка редактирования
            ComponentType.BP, // Тип компонента
            mutableListOf<Component>(),       // Список компонентов
            ComponentCardUtils.createAdapter(requireContext(), binding.BPProcessorSpinner)    // Адаптер Spinner
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
