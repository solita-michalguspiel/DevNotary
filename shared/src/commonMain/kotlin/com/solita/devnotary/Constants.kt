package com.solita.devnotary

object Constants {

    const val APP_URL = "https://dev-notary.web.app"
    const val ANDROID_PACKAGE_NAME = "com.solita.devnotary.android"
    const val CURRENT_EMAIL_KEY = "email_key"

    const val RESEND_EMAIL_TIME = 60

    const val ERROR_MESSAGE = "Oops.. something went wrong"

    /**FIREBASE*/
    const val SHARED_NOTES_FIREBASE = "shared_notes_firebase_reference"
    const val SHARED_NOTES_REF_FIREBASE = "shared_notes_ref_firebase_reference"
    const val USERS_FIREBASE = "user_firebase_reference"

    /**USERS*/
    const val USER_ID = "userId"

    /**NOTES*/
    const val NOTE_ID = "noteId"
    const val OWNER_USER_ID = "ownerUserId"
    const val SHARED_USER_ID = "sharedUserId"


    const val WHITE_COLOR = "white"
    const val NO_TITLE_ERROR = "Can't add note without title"
    const val BLANK_NOTE_ERROR = "Can't add blank note"
}