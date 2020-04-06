package com.codingwithmitch.cleannotes.notes.framework.datasource.mappers

import com.codingwithmitch.cleannotes.notes.framework.datasource.model.NoteEntity
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.core.business.DateUtil
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import java.lang.Exception
import javax.inject.Inject

/**
 * Maps Note to NoteEntity or NoteEntity to Note.
 */
@FeatureScope
class NoteEntityMapper
@Inject
constructor(
    private val dateUtil: DateUtil
)
{

    fun noteToEntity(note: Note): NoteEntity {
        var id: Int? = null
        if(note.id > 0){
            id = note.id
        }
        return NoteEntity(
            id = id,
            title = note.title,
            body = note.body,
            updated_at = dateUtil.convertServerStringDateToLong(note.updated_at),
            created_at = dateUtil.convertServerStringDateToLong(note.created_at)
        )
    }

    fun entityToNote(noteEntity: NoteEntity): Note{
        if(noteEntity.id == null){
            throw Exception(NoteEntity.nullIdError())
        }
        return Note(
            id = noteEntity.id!!,
            title = noteEntity.title,
            body = noteEntity.body,
            updated_at = dateUtil.convertLongToStringDate(noteEntity.updated_at),
            created_at = dateUtil.convertLongToStringDate(noteEntity.created_at)
        )
    }

    fun entityListToNoteList(entities: List<NoteEntity>): List<Note>{
        val list: ArrayList<Note> = ArrayList()
        for(entity in entities){
            list.add(entityToNote(entity))
        }
        return list
    }

}