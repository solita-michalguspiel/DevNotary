package com.solita.devnotary.android.utils

import com.solita.devnotary.android.utils.Constants.LOCAL_NOTE
import com.solita.devnotary.android.utils.Constants.NEW_NOTE
import com.solita.devnotary.android.utils.Constants.SHARED_NOTE

sealed class NoteType {

    object NewNote : NoteType()
    object SharedNote : NoteType()
    object LocalNote : NoteType()
}

fun getNoteType(type:String):NoteType{
    return when(type){
        NEW_NOTE -> NoteType.NewNote
        SHARED_NOTE -> NoteType.SharedNote
        LOCAL_NOTE -> NoteType.LocalNote
        else -> NoteType.NewNote
    }
}