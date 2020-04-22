package com.codingwithmitch.cleannotes.business.domain.model

import com.codingwithmitch.cleannotes.business.util.DateUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteFactory
@Inject
constructor(
    private val dateUtil: DateUtil
) {

    fun create(
        id: Int = -1,
        title: String,
        body: String? = null
    ): Note {
        return Note(
            id = id,
            title = title,
            body = body ?: "",
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

}









