package com.example.bar

import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView

data class CardUIElements(
    val cardId: String, // ID карточки
    val addButton: Button,
    val spinner: Spinner,
    val priceTextView: TextView,
    val editButton: ImageView
)