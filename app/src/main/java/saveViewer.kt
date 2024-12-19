package com.example.bar

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object LibraryManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    // Функция для импорта всех сборок
    fun importLibrary(context: Context, cardLayout: ConstraintLayout, firstAnchorView: View) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.getReference("users").child(userId).child("cardLibraries")

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var previousCardId: Int = firstAnchorView.id // Используем ID первого View как точку привязки
                    val cardViews = mutableListOf<View>()

                    for (record in snapshot.children) {
                        val recordName = record.key
                        val recordData = record.value

                        if (recordName != null && recordData != null) {
                            val cardView = createCardView(context, cardLayout, recordName, recordData.toString())
                            cardLayout.addView(cardView)

                            // Создаем LayoutParams для привязки
                            val layoutParams = ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                topToBottom = previousCardId
                                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                                topMargin = 20.dpToPx(context) // Отступ 20dp сверху
                            }

                            cardView.layoutParams = layoutParams
                            previousCardId = cardView.id // Обновляем привязку для следующей карточки

                            // Добавляем карточку в список
                            cardViews.add(cardView)
                        }
                    }

                    // При удалении карточки обновляем привязки
                    for (cardView in cardViews) {
                        val deleteButton = cardView.findViewById<ImageView>(R.id.imageView6)
                        deleteButton.setOnClickListener {
                            showDeleteConfirmationDialog(context, cardView, cardLayout)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Ошибка загрузки данных: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(context, "Ошибка: Пользователь не авторизован", Toast.LENGTH_LONG).show()
        }
    }

    // Функция для создания карточки
    private fun createCardView(context: Context, cardLayout: ConstraintLayout, recordName: String, recordData: String): View {
        val inflater = LayoutInflater.from(context)
        val cardView = inflater.inflate(R.layout.view_card, cardLayout, false)

        cardView.id = View.generateViewId()
        val editText = cardView.findViewById<EditText>(R.id.build)
        val deleteButton = cardView.findViewById<ImageView>(R.id.imageView6)

        // Устанавливаем имя сборки
        editText.setText(recordName)

        return cardView
    }

    // Функция для показа диалогового окна подтверждения
    private fun showDeleteConfirmationDialog(context: Context, cardView: View, cardLayout: ConstraintLayout) {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.is_ok, null)

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("Да") { _, _ ->
                // Удаляем карточку из layout
                cardLayout.removeView(cardView)
                // Обновляем привязки оставшихся карточек
                updateCardViewPositions(cardLayout)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    // Обновление привязок карточек
    private fun updateCardViewPositions(cardLayout: ConstraintLayout) {
        val allCardViews = mutableListOf<View>()

        // Собираем все карточки
        for (i in 0 until cardLayout.childCount) {
            allCardViews.add(cardLayout.getChildAt(i))
        }

        var previousCardId: Int = cardLayout.id // Начальная привязка (к родительскому контейнеру)

        // Для каждой карточки обновляем LayoutParams
        for (cardView in allCardViews) {
            val layoutParams = cardView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topToBottom = previousCardId
            layoutParams.topMargin = 20.dpToPx(cardLayout.context)
            cardView.layoutParams = layoutParams

            // Обновляем предыдущую карточку
            previousCardId = cardView.id
        }
    }

    // Удаление записи из Firebase
    private fun deleteRecord(recordName: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val recordRef = database.getReference("users").child(userId).child("cardLibraries").child(recordName)

            recordRef.removeValue()
                .addOnSuccessListener {
                    println("Запись '$recordName' успешно удалена.")
                }
                .addOnFailureListener { e ->
                    println("Ошибка при удалении записи: ${e.message}")
                }
        }
    }

    // Расширение для конвертации dp в px
    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}
