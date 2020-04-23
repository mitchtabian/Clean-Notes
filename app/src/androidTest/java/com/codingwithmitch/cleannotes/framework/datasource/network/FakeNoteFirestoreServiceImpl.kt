package com.codingwithmitch.cleannotes.framework.datasource.network

//class FakeNoteFirestoreServiceImpl
//constructor(
//    private val networkMapper: NetworkMapper,
//    private val dateUtil: DateUtil
//): NoteFirestoreService {
//
//    private val data: ArrayList<NoteNetworkEntity> = ArrayList()
//
//    override suspend fun insertNote(note: Note): Task<Void> {
//        val entity = networkMapper.mapToEntity(note)
//        val index = doesDataContainEntity(entity)
//        if( index != null){
//            data.removeAt(index)
//            data.add(index, entity)
//        }else{
//            data.add(entity)
//        }
//
//    }
//
//
//    private fun doesDataContainEntity(entity: NoteNetworkEntity): Int? {
//        for((index,value) in data.withIndex()){
//            if(value.id.equals(entity.id))
//                return index
//        }
//        return null
//    }
//
//    override suspend fun deleteNote(primaryKey: String): Task<Void> {
//
//    }
//
//    override suspend fun updateNote(
//        primaryKey: String,
//        newTitle: String,
//        newBody: String?
//    ): Task<Void> {
//
//    }
//
//    override suspend fun findUpdatedNotes(previousSyncTimestamp: Long): Task<QuerySnapshot> {
//
//    }
//
//    override suspend fun insertNotes(notes: List<Note>): List<Task<Void>> {
//
//    }
//}
//
//
//
//















