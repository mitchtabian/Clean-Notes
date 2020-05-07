package com.codingwithmitch.cleannotes.framework.datasource.network.mappers

import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.codingwithmitch.cleannotes.business.domain.util.DateUtil
import com.codingwithmitch.cleannotes.business.domain.util.EntityMapper
import com.codingwithmitch.cleannotes.framework.datasource.network.model.NoteNetworkEntity
import javax.inject.Inject

/**
 * Maps Note to NoteNetworkEntity or NoteNetworkEntity to Note.
 */
class NetworkMapper
@Inject
constructor(
    private val dateUtil: DateUtil
): EntityMapper<NoteNetworkEntity, Note>
{

    fun entityListToNoteList(entities: List<NoteNetworkEntity>): List<Note>{
        val list: ArrayList<Note> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun noteListToEntityList(notes: List<Note>): List<NoteNetworkEntity>{
        val entities: ArrayList<NoteNetworkEntity> = ArrayList()
        for(note in notes){
            entities.add(mapToEntity(note))
        }
        return entities
    }

    override fun mapFromEntity(entity: NoteNetworkEntity): Note {
        return Note(
            id = entity.id,
            title = entity.title,
            body = entity.body,
            updated_at = dateUtil.convertFirebaseTimestampToStringData(entity.updated_at),
            created_at = dateUtil.convertFirebaseTimestampToStringData(entity.created_at)
        )
    }

    override fun mapToEntity(domainModel: Note): NoteNetworkEntity {
        return NoteNetworkEntity(
            id = domainModel.id,
            title = domainModel.title,
            body = domainModel.body,
            updated_at = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.updated_at),
            created_at = dateUtil.convertStringDateToFirebaseTimestamp(domainModel.created_at)
        )
    }


}