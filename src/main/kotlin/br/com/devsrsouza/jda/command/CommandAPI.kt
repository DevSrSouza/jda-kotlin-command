package br.com.devsrsouza.jda.command

import br.com.devsrsouza.jda.command.implementation.CommandImpl
import br.com.devsrsouza.jda.command.implementation.CommandListDefinitionImpl
import br.com.devsrsouza.jda.command.implementation.lifecycle.DisposeCommandLifecycleImpl
import br.com.devsrsouza.jda.command.implementation.lifecycle.SetupCommandLifecycleImpl
import br.com.devsrsouza.jda.command.lifecycle.SetupCommandLifecycle
import net.dv8tion.jda.api.JDA

inline fun JDA.commands(
    prefix: String,
    block: CommandListDefinition.() -> Unit
): CommandListDefinition {
    return CommandListDefinitionImpl(this, prefix)
        .apply(block)
        .also {
            addEventListener(it)
        }
}

fun CommandHolder.command(
    name: String,
    executor: suspend CommandExecutor.() -> Unit
): Command {
    return CommandImpl(jda, name, executor).also {
        registerCommand(name, it)
    }
}

suspend fun CommandExecutor.fail(
    execute: FailExecuteCallback = {}
): Nothing = throw CommandFailException(execute)


inline fun CommandExecutor.setup(
    block: SetupCommandLifecycle.() -> Unit
) {
    val setup = SetupCommandLifecycleImpl(jda)
    setup.block()
    registerLifecycle(setup)
}

fun CommandExecutor.onDispose(
    onDispose: suspend () -> Unit
) {
    registerLifecycle(DisposeCommandLifecycleImpl(jda, onDispose))
}
