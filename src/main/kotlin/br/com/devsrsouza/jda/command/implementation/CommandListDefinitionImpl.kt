package br.com.devsrsouza.jda.command.implementation

import br.com.devsrsouza.jda.command.Command
import br.com.devsrsouza.jda.command.CommandExecutorCallback
import br.com.devsrsouza.jda.command.CommandListDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class CommandListDefinitionImpl(
    override val jda: JDA,
    val prefix: String,
    override var executor: CommandExecutorCallback? = null
) : CommandListDefinition, Command by CommandImpl(jda, prefix, executor) {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    override fun onEvent(event: GenericEvent) {
        if(event !is GuildMessageReceivedEvent || event.isWebhookMessage)  return
        val raw = event.message.contentRaw

        if (!raw.startsWith(prefix, true)) return

        val args = raw.substring(name.length)
            .trim()
            .replace("  ", " ")
            .split(" ")

        scope.launch {
            handleCommand(
                event.guild,
                event.member!!,
                event.channel,
                event.message,
                raw,
                args.toTypedArray()
            )
        }
    }



}