package com.example.bar.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    // Устанавливаем ID пользователя
    fun setUserId(id: String) {
        _userId.value = id
    }
}