package com.codingwithmitch.cleannotes.notes.framework.datasource.mappers

import com.codingwithmitch.cleannotes.notes.framework.datasource.model.NoteEntity
import com.codingwithmitch.cleannotes.notes.business.domain.model.Note
import com.codingwithmitch.cleannotes.core.business.DateUtil
import com.codingwithmitch.cleannotes.core.business.EntityMapper
import com.codingwithmitch.cleannotes.core.di.scopes.FeatureScope
import java.lang.Exception
import javax.inject.Inject

/**
 * Maps Note to NoteEntity or NoteEntity to Note.
 */
@FeatureScope
class NoteMapper
@Inject
constructor(
    private val dateUtil: DateUtil
): EntityMapper<NoteEntity, Note>
{

    fun entityListToNoteList(entities: List<NoteEntity>): List<Note>{
        val list: ArrayList<Note> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun noteListToEntityList(notes: List<Note>): List<NoteEntity>{
        val entities: ArrayList<NoteEntity> = ArrayList()
        for(note in notes){
            entities.add(mapToEntity(note))
        }
        return entities
    }

    override fun mapFromEntity(entity: NoteEntity): Note {
        if(entity.id == null){
            throw Exception(NoteEntity.nullIdError())
        }
        return Note(
            id = entity.id!!,
            title = entity.title,
            body = entity.body,
            updated_at = dateUtil.convertLongToStringDate(entity.updated_at),
            created_at = dateUtil.convertLongToStringDate(entity.created_at)
        )
    }

    override fun mapToEntity(domainModel: Note): NoteEntity {
        var id: Int? = null
        if(domainModel.id > 0){
            id = domainModel.id
        }
        return NoteEntity(
            id = id,
            title = domainModel.title,
            body = domainModel.body,
            updated_at = dateUtil.convertServerStringDateToLong(domainModel.updated_at),
            created_at = dateUtil.convertServerStringDateToLong(domainModel.created_at)
        )
    }
}







