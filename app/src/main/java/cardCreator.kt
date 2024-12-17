package com.example.bar

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.*

import com.example.bar.Component
import com.example.bar.R
import com.example.bar.ComponentType

object ComponentCardUtils {

    // Функция для создания карточки компонента
    fun createComponentCard(
        addButton: Button,         // Кнопка для добавления нового компонента
        spinner: Spinner,          // Spinner для отображения списка компонентов
        priceTextView: TextView,   // TextView для отображения цены
        editButton: ImageView,     // Кнопка редактирования компонента
        componentType: ComponentType, // Тип компонента (например, MB, CPU)
        components: MutableList<Component>,  // Список всех компонентов
        spinnerAdapter: ArrayAdapter<String>  // Адаптер для Spinner
    ) {
        // Обработчик кнопки "Добавить"
        addButton.setOnClickListener {
            showInputDialog(componentType, null, components, spinner, spinnerAdapter, priceTextView)
        }

        // Обработчик изменения выбранного компонента в Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedName = parent.getItemAtPosition(position) as String
                val selectedComponent = components.find { it.name == selectedName }

                // Обновляем TextView с ценой
                selectedComponent?.let {
                    val priceText = "${it.price} $"
                    priceTextView.text = priceText
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                priceTextView.text = ""
            }
        }

        // Обработчик нажатия на кнопку редактирования
        editButton.setOnClickListener {
            if (spinner.adapter == null || spinner.adapter.count == 0) {
                Toast.makeText(spinner.context, "Список компонентов пуст", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPosition = spinner.selectedItemPosition
            if (selectedPosition >= 0) {
                val selectedComponent = components[selectedPosition]
                showInputDialog(componentType, selectedComponent, components, spinner, spinnerAdapter, priceTextView)
            } else {
                Toast.makeText(spinner.context, "Выберите компонент для редактирования", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Метод для отображения диалога с вводом данных
    private fun showInputDialog(
        componentType: ComponentType,
        componentToEdit: Component?,
        components: MutableList<Component>,
        spinner: Spinner,
        spinnerAdapter: ArrayAdapter<String>,
        priceTextView: TextView
    ) {
        val dialogView = LayoutInflater.from(priceTextView.context).inflate(R.layout.dialog_input, null)

        val nameEditText: EditText = dialogView.findViewById(R.id.editTextName)
        val linkEditText: EditText = dialogView.findViewById(R.id.editTextLink)
        val priceEditText: EditText = dialogView.findViewById(R.id.editTextPrice)
        val saveTextView: TextView = dialogView.findViewById(R.id.button4) // "Сохранить"
        val cancelTextView: TextView = dialogView.findViewById(R.id.button5) // "Отмена"

        if (componentToEdit != null) {
            // Заполняем поля данными из компонента
            nameEditText.setText(componentToEdit.name)
            linkEditText.setText(componentToEdit.link)
            priceEditText.setText(componentToEdit.price)
        }

        // Создаем диалог с кастомным стилем
        val dialog = AlertDialog.Builder(priceTextView.context, R.style.CustomAlertDialog)
            .setView(dialogView)
            .create()

        // Обработчик кнопки "Сохранить"
        saveTextView.setOnClickListener {
            val name = nameEditText.text.toString()
            val link = linkEditText.text.toString()
            val price = priceEditText.text.toString()

            if (name.isNotEmpty() && link.isNotEmpty() && price.isNotEmpty()) {
                if (componentToEdit != null) {
                    // Удаляем старый компонент
                    components.remove(componentToEdit)
                    spinnerAdapter.remove(componentToEdit.name)

                    // Обновляем данные компонента
                    componentToEdit.name = name
                    componentToEdit.link = link
                    componentToEdit.price = price

                    // Добавляем обновленный компонент в список и адаптер
                    components.add(componentToEdit)
                    spinnerAdapter.add(componentToEdit.name)

                    Toast.makeText(priceTextView.context, "Запись обновлена", Toast.LENGTH_SHORT).show()
                } else {
                    // Создаем новый компонент
                    val newComponent = Component(name, link, price, componentType)
                    components.add(newComponent)
                    spinnerAdapter.add(newComponent.name)

                    Toast.makeText(priceTextView.context, "Запись добавлена", Toast.LENGTH_SHORT).show()
                }

                // Обновляем адаптер
                spinnerAdapter.notifyDataSetChanged()

                // Найдем индекс компонента в списке
                val position = components.indexOf(componentToEdit ?: components.last())

                // Устанавливаем новый элемент в Spinner
                spinner.setSelection(position)

                // Обновляем цену в priceTextView после сохранения изменений
                val updatedComponent = componentToEdit ?: components.last()
                val updatedPriceText = "${updatedComponent.price} $"
                priceTextView.text = updatedPriceText

                dialog.dismiss() // Закрыть диалог
            } else {
                Toast.makeText(priceTextView.context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик кнопки "Отмена"
        cancelTextView.setOnClickListener {
            dialog.dismiss() // Закрыть диалог
        }

        dialog.show()
    }



    fun createAdapter(context: Context, spinner: Spinner): ArrayAdapter<String> {
        // Создаем адаптер с кастомным макетом
        val adapter = ArrayAdapter<String>(context, R.layout.colored_spinner, mutableListOf())

        // Устанавливаем кастомный макет для выпадающего списка
        adapter.setDropDownViewResource(R.layout.colored_spinner_dropdown)

        // Применяем адаптер к Spinner (всегда создаем новый адаптер для каждого Spinner)
        spinner.adapter = adapter

        return adapter
    }

    /*fun createAdapter(context: Context, spinner: Spinner): ArrayAdapter<String> {
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mutableListOf())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        return adapter
    }*/
}
