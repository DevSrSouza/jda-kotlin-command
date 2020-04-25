package br.com.devsrsouza.jda.command

import java.lang.RuntimeException

typealias FailExecuteCallback = suspend () -> Unit

class CommandFailException(
    val execute: FailExecuteCallback
) : RuntimeException()