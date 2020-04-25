package br.com.devsrsouza.jda.command

import br.com.devsrsouza.jda.command.lifecycle.CommandLifecycleListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

interface CommandExecutor : WithJDA {
    override val jda: JDA
    val guild: Guild
    val member: Member
    val channel: TextChannel
    val message: Message

    val label: String
    val args: Array<String>

    fun registerLifecycle(lifecycleListener: CommandLifecycleListener)
}



