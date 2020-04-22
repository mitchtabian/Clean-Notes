package com.codingwithmitch.cleannotes.business.state

interface StateEvent {

    fun errorInfo(): String

    fun eventName(): String

    fun shouldDisplayProgressBar(): Boolean
}