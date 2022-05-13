package com.solita.devnotary.DatabaseTest

import com.solita.devnotary.database.Local_note
import com.solita.devnotary.database.NotesQueries
import com.solita.devnotary.dev_notary_db
import io.kotest.matchers.shouldBe
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class NotesQueriesTests: RobolectricTests() {

    private lateinit var noteQueries : NotesQueries

    private val firstNote = Local_note(
        "fidhsbagoipngöklsagagew",
        "Fresh note!",
        "Description",
        "dateAndTime",
        "Pink"
    )
    private val secondNote  = firstNote.copy("fou23", color = "purple")
    private val  thirdNote = firstNote.copy("bue2", color = "purple")
    private val noteList = listOf(firstNote,secondNote,thirdNote)


    private fun addNote(note: Local_note){
        noteQueries.insert(
            note_id = note.note_id,
            title = note.title,
            content = note.content,
            date_time = note.date_time,
            color = note.color
        )
    }

    val getNotes get() = noteQueries.selectAll().executeAsList()

    @BeforeTest
    fun setup(){
        val driver = createTestDriver()
        val database = dev_notary_db(driver)
        noteQueries = database.notesQueries
    }

    @AfterTest
    fun tearDown(){
        noteQueries.deleteAll_TEST()
    }


    @Test
    fun givenIAddedOneItemToDb_ItShouldAppearInSelectAll(){
        addNote(firstNote)
        val notes = noteQueries.selectAll().executeAsList()
        notes.size shouldBe 1
    }

    @Test
    fun givenItemItemIsAddedToDB_AndThenDeleted_DB_ShouldBe_empty(){
        getNotes.size shouldBe 0
        addNote(firstNote)
        getNotes.size shouldBe 1
        noteQueries.delete(firstNote.note_id)
        getNotes.size shouldBe 0
    }

    @Test
    fun givenItemWasAddedToDB_AndThenItsContentWasUpdated_GetNoteItemContentShouldChange(){
        val differentContent = "Different content!"
        getNotes.size shouldBe 0
        addNote(firstNote)
        getNotes.size shouldBe 1
        noteQueries.update(firstNote.title,differentContent,firstNote.color,firstNote.note_id)
        getNotes.size shouldBe 1
        getNotes.first().content shouldBe differentContent
        getNotes.first().title shouldBe firstNote.title
        getNotes.first().note_id shouldBe firstNote.note_id
        getNotes.first().color shouldBe firstNote.color
    }

    @Test
    fun given3DifferentItemsWasAddedToDB_AndColorOfOneWasChanged_OtherTwoShouldNotChange(){
        getNotes.size shouldBe 0
        noteList.forEach {
            addNote(it)
        }
        getNotes.size shouldBe 3
        noteQueries.update(secondNote.title,secondNote.content,"Yellow",secondNote.note_id)
        getNotes.contains(secondNote) shouldBe false
        getNotes.contains(firstNote) shouldBe true
        getNotes.contains(thirdNote) shouldBe true
    }

    @Test
    fun given3NotesWereAddedToDB_AndOneWasErased_CorrectOneShouldDisappearFromDB(){
        getNotes.size shouldBe 0
        noteList.forEach {
            addNote(it)
        }
        getNotes.size shouldBe 3
        noteQueries.delete(firstNote.note_id)
        getNotes.size shouldBe 2
        getNotes.contains(firstNote) shouldBe false
        getNotes.contains(secondNote) shouldBe true
        getNotes.contains(thirdNote) shouldBe true
    }

}