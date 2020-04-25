package br.com.devsrsouza.jda.command.implementation

import br.com.devsrsouza.jda.command.CommandExecutor
import br.com.devsrsouza.jda.command.lifecycle.CommandLifecycleListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class CommandExecutorImpl(
    override val jda: JDA,
    override val guild: Guild,
    override val member: Member,
    override val channel: TextChannel,
    override val message: Message,
    override val label: String,
    override val args: Array<String>
) : CommandExecutor {
    val lifecyles = mutableListOf<CommandLifecycleListener>()

    override fun registerLifecycle(lifecycleListener: CommandLifecycleListener) {
        lifecyles.add(lifecycleListener)

        jda.addEventListener(lifecycleListener)
    }
}