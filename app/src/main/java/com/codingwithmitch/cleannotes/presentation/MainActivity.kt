package com.codingwithmitch.cleannotes.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codingwithmitch.cleannotes.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(
            Intent(
                this,
                Class.forName(
                    "com.codingwithmitch.notes.presentation.ListActivity"
                ))
        )
    }
}
