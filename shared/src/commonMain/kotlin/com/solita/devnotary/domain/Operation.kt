package com.solita.devnotary.domain

interface Message{
    val message : String
}

sealed class Operation : Message {

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