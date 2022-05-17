package com.solita.devnotary.feature_notes.presentation

import com.solita.devnotary.feature_notes.domain.model.Note


enum class SortOptions(val sort: Sort,val sortName : String){
    BY_NAME_ASC(ByNameAscending,"By name ascending"),
    BY_NAME_DESC(ByNameDescending,"By name descending"),
    BY_DATE_ASC(ByDateAscending,"By date ascending"),
    BY_DATE_DESC(ByDateDescending,"By date descending")
}

sealed interface Sort {
    fun sort(list : List<Note>): List<Note>
}

object ByNameAscending : Sort {
    override fun sort(list: List<Note>): List<Note> {
        return list.sortedBy { it.title.lowercase() }
    }
}
object ByNameDescending : Sort {
    override fun sort(list: List<Note>): List<Note> {
        return list.sortedBy { it.title.lowercase() }.reversed()
    }
}

object ByDateAscending : Sort {
    override fun sort(list: List<Note>): List<Note> {
        return list.sortedBy { it.dateTime }
    }
}

object ByDateDescending : Sort {
    override fun sort(list: List<Note>): List<Note> {
        return list.sortedBy { it.dateTime }.reversed()
    }
}

