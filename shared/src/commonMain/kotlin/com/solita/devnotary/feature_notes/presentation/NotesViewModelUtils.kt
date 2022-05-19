package com.solita.devnotary.feature_notes.presentation

import com.benasher44.uuid.Uuid
import com.solita.devnotary.database.Local_note
import com.solita.devnotary.feature_notes.domain.model.Note
import com.solita.devnotary.feature_notes.domain.model.SharedNote
import com.solita.devnotary.utils.Crypto
import kotlinx.datetime.Clock
import kotlin.random.Random

fun randomUUID():String {
    return Uuid(
        Random(Clock.System.now().epochSeconds).nextLong(),
        Random(Clock.System.now().epochSeconds).nextLong()
    ).toString()
}

fun createNewLocalNote(providedId : String?, title :String, content: String, color: String): Local_note{
    val id = providedId ?: randomUUID()
    return Local_note(
        id,
        title,
        content,
        Clock.System.now().toString(),
        color
    )
}
fun Local_note.changeToNote(): Note {
    return Note(
        noteId = this.note_id,
        title = this.title,
        content = this.content,
        dateTime = this.date_time,
        color = this.color
    )
}

fun SharedNote.changeToNote(): Note {
    val crypto = Crypto()
    return Note(
        noteId = noteId,
        ownerUserId = ownerUserId,
        title = crypto.decryptMessage(noteId,title),
        content = crypto.decryptMessage(noteId,content),
        dateTime = dateTime,
        color = color
    )
}


fun prepareLists(
    localNotes: List<Note>,
    notesSharedByOtherUsers: List<Note>,
    selectedSort: Sort,
    searchedPhrase: String
): List<Note> {
    val joinedNotes = localNotes + notesSharedByOtherUsers
    val sorted = selectedSort.sort(joinedNotes)
    return sorted.filter { it.title.lowercase().contains(searchedPhrase.lowercase()) }
}
