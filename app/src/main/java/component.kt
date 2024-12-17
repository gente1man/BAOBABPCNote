package com.example.bar

data class Component(
    var name: String,        // Название компонента
    var link: String,        // Ссылка
    var price: String,       // Цена
    val type: ComponentType  // Тип компонента (MB, CPU, GPU и т.д.)
)

enum class ComponentType {
    MB,   // Материнская плата
    CPU,  // Процессор
    GPU,  // Видеокарта
    RAM,  // Оперативная память
    COOL, // Охлаждение
    DISK, // Диск
    CASE, // Корпус
    BP,    // Блок питания
    OTHER
}
