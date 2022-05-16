package com.solita.devnotary.feature_notes.domain.use_case.users_use_cases

import com.solita.devnotary.feature_notes.domain.repository.UsersRepository

class GetUsersWithAccess(private val repository: UsersRepository) {
    suspend operator fun invoke( noteId: String) = repository.getUsersWithAccess(noteId)
}