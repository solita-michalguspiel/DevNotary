package com.solita.devnotary.feature_notes.domain

sealed interface OperationContent{
    val message : String
}

sealed class Operation : OperationContent {

    class Edit : Operation() {
    override val message = "Note edit saved!"
    }

    class Delete : Operation(){
        override val message = "Note deleted!"
    }

    class Add: Operation(){
        override val message: String = "Note added!"
    }

    class Share: Operation(){
        override val message: String = "Note shared!"
    }

}