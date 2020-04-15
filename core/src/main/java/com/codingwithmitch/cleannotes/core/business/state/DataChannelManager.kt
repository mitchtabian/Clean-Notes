package com.codingwithmitch.cleannotes.core.business.state


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithmitch.cleannotes.core.util.printLogD
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
abstract class DataChannelManager<ViewState> {

    private val dataChannel: ConflatedBroadcastChannel<DataState<ViewState>> =  ConflatedBroadcastChannel()
    private var channelScope: CoroutineScope? = null
    private val stateEventManager: StateEventManager = StateEventManager()

    val messageStack = MessageStack()

    val shouldDisplayProgressBar = stateEventManager.shouldDisplayProgressBar

    init {
        dataChannel
            .asFlow()
            .onEach{ dataState ->
                dataState.data?.let { data ->
                    handleNewData(data)
                    removeStateEvent(dataState.stateEvent)
                }
                dataState.stateMessage?.let { stateMessage ->
                    handleNewStateMessage(stateMessage)
                    removeStateEvent(dataState.stateEvent)
                }
            }
            .launchIn(CoroutineScope(Main))
    }

    fun setupChannel(){
        cancelJobs()
        setupNewChannelScope(CoroutineScope(IO))
    }

    abstract fun handleNewData(data: ViewState)

    private fun offerToDataChannel(dataState: DataState<ViewState>){
        dataChannel.let {
            if(!it.isClosedForSend){
                it.offer(dataState)
            }
        }
    }

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>>
    ){
//        if(!isStateEventActive(stateEvent) && isMessageStackEmpty()){
        if(!isStateEventActive(stateEvent)){
            addStateEvent(stateEvent)
            jobFunction
                .onEach { dataState ->
                    offerToDataChannel(dataState)
                }
                .launchIn(getChannelScope())
        }
    }

    private fun isMessageStackEmpty(): Boolean {
        if(messageStack.isStackEmpty()){
            printLogD("DataChannelManager", "MessageStack is empty.")
            return true
        }
        else{
            printLogD("DataChannelManager", "MessageStack is NOT emtpy.")
            return false
        }
    }

    private fun handleNewStateMessage(stateMessage: StateMessage){
        appendStateMessage(stateMessage)
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun clearStateMessage(index: Int = 0){
        messageStack.removeAt(index)
    }

    // for debugging
    fun getActiveJobs() = stateEventManager.getActiveJobNames()

    private fun clearActiveStateEventCounter()
            = stateEventManager.clearActiveStateEventCounter()

    private fun addStateEvent(stateEvent: StateEvent)
            = stateEventManager.addStateEvent(stateEvent)

    private fun removeStateEvent(stateEvent: StateEvent?)
            = stateEventManager.removeStateEvent(stateEvent)

    private fun isStateEventActive(stateEvent: StateEvent)
            = stateEventManager.isStateEventActive(stateEvent)


//    private fun clearActiveStateEventCounter(){
//        _activeStateEvents.clear()
//        syncNumActiveStateEvents()
//    }
//
//    private fun addStateEvent(stateEvent: StateEvent){
//        printLogD("DCM",
//            "adding state event: ${stateEvent.eventName()}")
//        _activeStateEvents.add(stateEvent)
//        syncNumActiveStateEvents()
//    }
//
//    private fun removeStateEvent(stateEvent: StateEvent?){
//        printLogD("DCM",
//            "removing state event: ${stateEvent?.eventName()}")
//        _activeStateEvents.remove(stateEvent)
//        syncNumActiveStateEvents()
//    }
//
//    private fun isStateEventActive(stateEvent: StateEvent): Boolean{
//        for(event in _activeStateEvents){
//            if(stateEvent.eventName().equals(event.eventName())){
//                return true
//            }
//        }
//        return false
//    }
//
//    private fun syncNumActiveStateEvents(){
//        _numActiveJobs.value = _activeStateEvents.size
//    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return isStateEventActive(stateEvent)
    }

    fun getChannelScope(): CoroutineScope {
        return channelScope?: setupNewChannelScope(CoroutineScope(IO))
    }

    private fun setupNewChannelScope(coroutineScope: CoroutineScope): CoroutineScope{
        channelScope = coroutineScope
        return channelScope as CoroutineScope
    }

    fun cancelJobs(){
        if(channelScope != null){
            if(channelScope?.isActive == true){
                channelScope?.cancel()
            }
            channelScope = null
        }
        clearActiveStateEventCounter()
    }

}























