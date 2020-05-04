package com.codingwithmitch.cleannotes.business.interactors

import com.codingwithmitch.cleannotes.business.interactors.common.DeleteNoteTest
import com.codingwithmitch.cleannotes.business.interactors.network_sync.SyncDeletedNotesTest
import com.codingwithmitch.cleannotes.business.interactors.network_sync.SyncNotesTest
import com.codingwithmitch.cleannotes.business.interactors.notedetail.UpdateNoteTest
import com.codingwithmitch.cleannotes.business.interactors.notelist.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.Suite

@InternalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    DeleteNoteTest::class,
    SyncDeletedNotesTest::class,
    SyncNotesTest::class,
    UpdateNoteTest::class,
    DeleteMultipleNotesTest::class,
    GetNumNotesTest::class,
    InsertNewNoteTest::class,
    RestoreDeletedNoteTest::class,
    SearchNotesTest::class
)
class InteractorsTestSuite