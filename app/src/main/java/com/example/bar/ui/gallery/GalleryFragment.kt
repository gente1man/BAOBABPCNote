package com.example.bar.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bar.databinding.FragmentGalleryBinding
import com.example.bar.databinding.FragmentSlideshowBinding
import com.example.bar.ui.slideshow.SlideshowViewModel
import com.example.bar.LibraryManager
import com.example.bar.R
import com.example.bar.ui.home.HomeFragment

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cardLayout = binding.cardLayout
        val firstAnchorView  = binding.build // ID первого объекта для привязки
        LibraryManager.importLibrary(requireContext(), cardLayout, firstAnchorView)

        val btn = binding.btn
        btn.setOnClickListener {
            // Создаем экземпляр HomeFragment
            val navController = findNavController()
            navController.navigate(R.id.nav_home)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}