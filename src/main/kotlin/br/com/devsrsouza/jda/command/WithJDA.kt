package br.com.devsrsouza.jda.command

import net.dv8tion.jda.api.JDA

interface WithJDA {
    val jda: JDA
}
