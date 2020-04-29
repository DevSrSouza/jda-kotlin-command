package br.com.devsrsouza.jda.command.implementation

import br.com.devsrsouza.jda.command.Command
import br.com.devsrsouza.jda.command.CommandExecutorCallback
import br.com.devsrsouza.jda.command.CommandFailException
import br.com.devsrsouza.jda.command.lifecycle.CommandLifecycle
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

class CommandImpl(
    override val jda: JDA,
    override val name: String,
    override var executor: CommandExecutorCallback?
) : Command {

    private val subCommands: MutableMap<String, Command> = mutableMapOf()

    override fun registerCommand(name: String, command: Command) {
        subCommands.put(name, command)
    }

    override suspend fun handleCommand(
        guild: Guild,
        member: Member,
        channel: TextChannel,
        message: Message,
        label: String,
        args: Array<String>
    ) {
        val command = args.getOrNull(0)?.let { subCommands[it] }
        val newArgs = if(args.isNotEmpty()) args.copyOfRange(1, args.size) else emptyArray()

        if(command != null) {
            command.handleCommand(
                guild,
                member,
                channel,
                message,
                label,
                newArgs
            )
        } else if(executor != null) {
            val commandExecutor = CommandExecutorImpl(
                jda,
                guild,
                member,
                channel,
                message,
                label,
                newArgs
            )

            val event: CommandLifecycle = try {
                executor?.invoke(commandExecutor)
                CommandLifecycle.COMPLETE
            } catch (e: CommandFailException) {
                e.execute()
                CommandLifecycle.FAIL
            }

            for (lifecyle in commandExecutor.lifecyles) {
                jda.removeEventListener(lifecyle)
                lifecyle.onEvent(event)
            }
        }

    }

}