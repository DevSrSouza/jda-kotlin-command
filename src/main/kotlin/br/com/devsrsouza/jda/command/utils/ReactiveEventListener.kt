package br.com.devsrsouza.jda.command.utils

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import reactor.core.publisher.Flux

inline fun <reified T : GenericEvent> ReactiveEventListener.on() = on(T::class.java)

interface ReactiveEventListener : EventListener {
    fun dispose()

    fun <T : GenericEvent?> on(type: Class<T>): Flux<T>
}

class ReactiveEventListenerImpl(
    private val reactiveEventManager: ReactiveEventManager = ReactiveEventManager()
) : ReactiveEventListener {

    override fun onEvent(event: GenericEvent) {
        if(!reactiveEventManager.scheduler.isDisposed)
            reactiveEventManager.handle(event)
    }

    override fun dispose() {
        reactiveEventManager.scheduler.dispose()
    }

    override fun <T : GenericEvent?> on(type: Class<T>): Flux<T> {
        return reactiveEventManager.on(type)
    }

}