package com.codingwithmitch.notes.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codingwithmitch.cleannotes.presentation.BaseApplication
import com.codingwithmitch.cleannotes.util.printLogD
import com.codingwithmitch.notes.R
import com.codingwithmitch.notes.datasource.cache.repository.NoteCacheDataSourceImpl
import com.codingwithmitch.notes.di.DaggerNoteComponent
import javax.inject.Inject


class ListActivity : AppCompatActivity() {

    @Inject
    lateinit var noteCache: NoteCacheDataSourceImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        val appComponent = (application as BaseApplication).appComponent
        DaggerNoteComponent.factory()
            .create(appComponent)
            .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        printLogD("ListActivity", "NoteCache: ${noteCache}")
    }
}


















