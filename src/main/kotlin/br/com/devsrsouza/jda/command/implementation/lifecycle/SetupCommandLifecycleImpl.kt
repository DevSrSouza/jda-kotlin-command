package br.com.devsrsouza.jda.command.implementation.lifecycle

import br.com.devsrsouza.jda.command.utils.ReactiveEventListener
import br.com.devsrsouza.jda.command.utils.ReactiveEventListenerImpl
import br.com.devsrsouza.jda.command.lifecycle.CommandLifecycle
import br.com.devsrsouza.jda.command.lifecycle.SetupCommandLifecycle
import net.dv8tion.jda.api.JDA

class SetupCommandLifecycleImpl(
    override val jda: JDA
) : SetupCommandLifecycle, ReactiveEventListener by ReactiveEventListenerImpl() {

    override suspend fun onEvent(event: CommandLifecycle) {
        dispose()
    }

}