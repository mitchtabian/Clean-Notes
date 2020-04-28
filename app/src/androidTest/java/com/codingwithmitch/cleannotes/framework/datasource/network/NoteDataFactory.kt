package com.codingwithmitch.cleannotes.framework.datasource.network

import android.app.Application
import android.content.res.AssetManager
import com.codingwithmitch.cleannotes.business.domain.model.Note
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream

class NoteDataFactory
constructor(
    private val application: Application
){

    fun produceListOfNotes(): List<Note>{
        val notes: List<Note> = Gson()
            .fromJson(
                getNotesFromFile("note_list.json"),
                object: TypeToken<List<Note>>() {}.type
            )
        return notes
    }

    fun produceEmptyListOfNotes(): List<Note>{
        return ArrayList()
    }

    fun getNotesFromFile(fileName: String): String? {
        return readJSONFromAsset(fileName)
    }

    private fun readJSONFromAsset(fileName: String): String? {
        var json: String? = null
        json = try {
            val inputStream: InputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use{it.readText()}
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}













