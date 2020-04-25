package br.com.devsrsouza.jda.command

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

typealias CommandExecutorCallback = suspend CommandExecutor.() -> Unit

interface Command : CommandHolder {
    val name: String
    var executor: CommandExecutorCallback?

    suspend fun handleCommand(
        guild: Guild,
        member: Member,
        channel: TextChannel,
        message: Message,
        label: String,
        args: Array<String>
    )

    fun executor(executor: CommandExecutorCallback) {
        this.executor = executor
    }
}