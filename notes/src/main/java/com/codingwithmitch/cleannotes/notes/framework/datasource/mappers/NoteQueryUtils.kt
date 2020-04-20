package com.codingwithmitch.cleannotes.notes.framework.datasource.mappers

import com.codingwithmitch.cleannotes.notes.framework.datasource.model.NoteEntity
import com.codingwithmitch.notes.datasource.cache.db.NoteDao


const val NOTE_ORDER_ASC: String = ""
const val NOTE_ORDER_DESC: String = "-"
const val NOTE_FILTER_TITLE = "title"
const val NOTE_FILTER_DATE_CREATED = "created_at"

const val ORDER_BY_ASC_DATE_UPDATED = NOTE_ORDER_ASC + NOTE_FILTER_DATE_CREATED
const val ORDER_BY_DESC_DATE_UPDATED = NOTE_ORDER_DESC + NOTE_FILTER_DATE_CREATED
const val ORDER_BY_ASC_TITLE = NOTE_ORDER_ASC + NOTE_FILTER_TITLE
const val ORDER_BY_DESC_TITLE = NOTE_ORDER_DESC + NOTE_FILTER_TITLE

const val NOTE_PAGINATION_PAGE_SIZE = 30

suspend fun NoteDao.returnOrderedQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<NoteEntity> {

    when{

        filterAndOrder.contains(ORDER_BY_DESC_DATE_UPDATED) ->{
            return searchNotesOrderByDateDESC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_ASC_DATE_UPDATED) ->{
            return searchNotesOrderByDateASC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_DESC_TITLE) ->{
            return searchNotesOrderByTitleDESC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_ASC_TITLE) ->{
            return searchNotesOrderByTitleASC(
                query = query,
                page = page)
        }
        else ->
            return searchNotesOrderByDateDESC(
                query = query,
                page = page
            )
    }
}

















