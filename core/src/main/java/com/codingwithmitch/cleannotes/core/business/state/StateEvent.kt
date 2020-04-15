package com.codingwithmitch.cleannotes.core.business.state

interface StateEvent {

    fun errorInfo(): String

    fun eventName(): String

    fun shouldDisplayProgressBar(): Boolean
}