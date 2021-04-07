package com.codingwithmitch.cleannotes.framework.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashViewModel
@Inject
constructor(
    private val noteNetworkSyncManager: NoteNetworkSyncManager
): ViewModel(){

    init {
        syncCacheWithNetwork()
    }

    fun hasSyncBeenExecuted() = noteNetworkSyncManager.hasSyncBeenExecuted

    private fun syncCacheWithNetwork(){
        noteNetworkSyncManager.executeDataSync(viewModelScope)
    }

}
















