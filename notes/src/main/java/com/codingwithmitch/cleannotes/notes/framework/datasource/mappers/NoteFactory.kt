package com.codingwithmitch.cleannotes.notes.framework.datasource.mappers

import com.codingwithmitch.cleannotes.core.business.DateUtil
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import javax.inject.Inject

@FeatureScope
class NoteFactory
@Inject
constructor(
    private val dateUtil: DateUtil
) {

    fun create(
        id: Int = -1,
        title: String,
        body: String? = null
    ): Note{
        return Note(
            id = id,
            title = title,
            body = body ?: "",
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

}









