package com.solita.devnotary.feature_notes.presentation

import com.solita.devnotary.feature_notes.domain.model.Note


enum class Order {
    ASCENDING,
    DESCENDING
}

sealed interface SortOrder {
    val order: Order
}

sealed interface Sort : SortOrder {
    fun sort(list : List<Note>): List<Note>
}

class ByName(override val order: Order) : Sort, SortOrder {
    override fun sort(list: List<Note>): List<Note> {
        return when (order) {
            Order.ASCENDING -> list.sortedBy { it.title }
            Order.DESCENDING -> list.sortedBy { it.title }.reversed()
        }
    }
}

class ByDate(override val order: Order) : Sort, SortOrder {

    override fun sort(list: List<Note>): List<Note> {
        return when (order) {
            Order.ASCENDING -> list.sortedBy { it.dateTime }
            Order.DESCENDING -> list.sortedBy { it.dateTime }.reversed()
        }
    }
}