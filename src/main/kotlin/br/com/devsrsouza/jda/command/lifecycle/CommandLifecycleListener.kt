package br.com.devsrsouza.jda.command.lifecycle

import br.com.devsrsouza.jda.command.WithJDA
import net.dv8tion.jda.api.hooks.EventListener

enum class CommandLifecycle {
    COMPLETE, FAIL
}

interface CommandLifecycleListener : WithJDA, EventListener {
    suspend fun onEvent(event: CommandLifecycle)
}