package br.com.devsrsouza.jda.command

interface CommandHolder : WithJDA {
    fun registerCommand(name: String, command: Command)
}