package com.codingwithmitch.cleannotes.notes.datasource.mappers

import com.codingwithmitch.cleannotes.notes.datasource.model.NoteEntity
import com.codingwithmitch.cleannotes.notes.domain.model.Note
import com.codingwithmitch.cleannotes.util.DateUtil
import com.codingwithmitch.notes.di.FeatureScope
import javax.inject.Inject

@FeatureScope
class NoteEntityMapper
@Inject
constructor(
    private val dateUtil: DateUtil
)
{

    fun noteToEntity(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id,
            title = note.title,
            body = note.body,
            updated_at = dateUtil.convertServerStringDateToLong(note.updated_at),
            created_at = dateUtil.convertServerStringDateToLong(note.created_at)
        )
    }

    fun entityToNote(noteEntity: NoteEntity): Note{
        return Note(
            id = noteEntity.id,
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