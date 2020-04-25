package br.com.devsrsouza.jda.command.implementation.lifecycle

import br.com.devsrsouza.jda.command.lifecycle.CommandLifecycle
import br.com.devsrsouza.jda.command.lifecycle.DisposeCommandLifecycle
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent

class DisposeCommandLifecycleImpl(
    override val jda: JDA,
    override val onDispose: suspend () -> Unit
) : DisposeCommandLifecycle {
    override suspend fun onEvent(event: CommandLifecycle) {
        onDispose()
    }

    override fun onEvent(event: GenericEvent) {
        // ignore
    }
}