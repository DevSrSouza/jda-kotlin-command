package br.com.devsrsouza.jda.command.lifecycle

interface DisposeCommandLifecycle : CommandLifecycleListener {
    val onDispose: suspend () -> Unit
}