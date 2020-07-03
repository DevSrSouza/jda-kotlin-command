package br.com.devsrsouza.jda.command.utils

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener

inline fun <reified T : GenericEvent> ReactiveEventListener.on() = on(T::class.java)

interface ReactiveEventListener : EventListener {
    fun dispose()

    fun <T : GenericEvent?> on(type: Class<T>): Flow<T>
}

class ReactiveEventListenerImpl : ReactiveEventListener {

    private val eventChannel = BroadcastChannel<GenericEvent>(2)

    override fun onEvent(event: GenericEvent) {
        if(!eventChannel.isClosedForSend)
            eventChannel.sendBlocking(event)
    }

    override fun dispose() {
        eventChannel.close()
    }

    override fun <T : GenericEvent?> on(type: Class<T>): Flow<T> {
        return eventChannel.openSubscription().consumeAsFlow()
            .filter { type.isInstance(it) }
            .map { it as T }
    }

}