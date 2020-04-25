package com.codingwithmitch.cleannotes.framework.presentation.common

class NoteNetworkSyncManager {

    var hasBeenExecuted = false
        private set // Allow external read but not write

    fun executeDataSync(){
        if(hasBeenExecuted){
            return
        }
        hasBeenExecuted = true

    }

}





















