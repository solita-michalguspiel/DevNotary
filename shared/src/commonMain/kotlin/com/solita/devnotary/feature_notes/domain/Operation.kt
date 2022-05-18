package com.solita.devnotary.feature_notes.domain

import com.solita.devnotary.feature_notes.domain.model.Note

sealed interface OperationContent{
    val message : String
    val note : Note?
}

sealed class Operation : OperationContent {

    class Edit : Operation() {
    override val message = "Note edit saved!"
        override val note: Nothing? = null
    }

    class Delete : Operation(){
        override val message = "Note deleted!"
        override val note: Nothing? = null

    }

    class Add(override val note: Note?): Operation(){
        override val message: String = "Note added!"
    }

    class Share: Operation(){
        override val message: String = "Note shared!"
        override val note: Nothing? = null
    }

}