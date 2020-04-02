package com.codingwithmitch.cleannotes.notes.domain.model

data class Note(
    val id: Int,
    val title: String,
    val body: String,
    val updated_at: String,
    val created_at: String
)