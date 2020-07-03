# jda-kotlin-command

A small Kotlin library to create Guild commands for Discord using [JDA](https://github.com/DV8FromTheWorld/JDA) and [Coroutines](https://github.com/Kotlin/kotlinx.coroutines).

## Dependencies
- JDA
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)

## Setup

```kotlin
val jda_version = "4.1.1_137"
val coroutines_version = "1.3.5"

repositories {
    jcenter()

    maven { url = "http://nexus.devsrsouza.com.br/repository/maven-public/" }
}

dependencies {
    implementation("br.com.devsrsouza:jda-kotlin-command:2.0.0")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    implementation("net.dv8tion:JDA:$jda_version")
}
```

## Sample

```kotlin
import br.com.devsrsouza.jda.command.*
import br.com.devsrsouza.jda.command.utils.on

suspend fun <T> RestAction<T>.await() = suspendCoroutine<T> { continuation ->
    queue(
        continuation::resume,
        continuation::resumeWithException
    )
}

val WHITE_CHECK_MARK = "\u2705"

jda.commands("!") {
    command("test") {
        // Send a message to the guild channel
        val botMessage = channel.sendMessage(
            "Hi ${member.asMention}, please click at CHECK MARK!"
        ).await()
    
        // Add a White check mark reaction to be usaged as a button
        botMessage.addReaction(WHITE_CHECK_MARK).await()

        // Setup is a block that you can use `on<T>()` to listen
        // to events at the lifecycle of the command execution.
        // After the command gets finish by any reason, the events
        // will be unregistered.
        setup {
            // Preventing to add any other Reaction to the Message
            // using Coroutines Flow
            on<GuildMessageReactionAddEvent>()
                .filter { it.messageIdLong == botMessage.idLong }
                .filterNot { it.reactionEmote.isEmoji && it.reactionEmote.emoji == WHITE_CHECK_MARK }
                .onEach { it.reaction.removeReaction(it.user).await() }
                .launchIn(GlobalScope)
        }

        // This block will be executed when the command finish by any reason
        // In case of completion or in case of fail (fail {})
        onDispose {
            // Wait 3 seconds and delete the bot and user message
            delay(3000)
            botMessage.delete().queue()
            message.delete().queue()
        }

        // Get the first reaction to a WHITE_CHECK_MARK for a user that is not a bot
        // with a timeout of 10 seconds
        val reaction = withTimeoutOrNull(10000) {
            jda.on<GuildMessageReactionAddEvent>()
                .filter { it.messageIdLong == botMessage.idLong }
                .filter { !it.user.isBot }
                .filter { it.reactionEmote.isEmoji && it.reactionEmote.emoji == WHITE_CHECK_MARK }
                .first()
        } ?: fail {
            // fail throws a exception that is handle by the command framework
            // meaning that your code below will not run if `fail {}` get called
            // this block will be executed when it fails
            botMessage.editMessage("Timeout :(").await()
        }

        botMessage.editMessage("Thanks for clicking!").await()
    }
}
```

![](https://media.giphy.com/media/kgUvHExCmCcO3QFs4Z/giphy.gif)

### Organized version

`Commands.kt`
```kotlin
jda.commands("!") {
    registerTest()
}
```

`TestCommand.kt`
```kotlin
fun CommandHolder.registerTest() = command("test") {
    // Send a message to the guild channel
    val botMessage = channel.sendMessage(
        "Hi ${member.asMention}, please click at CHECK MARK!"
    ).await()

    // Add a White check mark reaction to be usaged as a button
    botMessage.addReaction(WHITE_CHECK_MARK).await()

    // Setup is a block that you can use `on<T>()` to listen
    // to events at the lifecycle of the command execution.
    // After the command gets finish by any reason, the events
    // will be unregistered.
    setup {
        // Preventing to add any other Reaction to the Message
        // using Coroutines Flow
        on<GuildMessageReactionAddEvent>()
            .filter { it.messageIdLong == botMessage.idLong }
            .filterNot { it.reactionEmote.isEmoji && it.reactionEmote.emoji == WHITE_CHECK_MARK }
            .onEach { it.reaction.removeReaction(it.user).await() }
            .launchIn(GlobalScope)
    }

    // This block will be executed when the command finish by any reason
    // In case of completion or in case of fail (fail {})
    onDispose {
        // Wait 3 seconds and delete the bot and user message
        delay(3000)
        botMessage.delete().queue()
        message.delete().queue()
    }

    // Get the first reaction to a WHITE_CHECK_MARK for a user that is not a bot
    // with a timeout of 10 seconds
    val reaction = withTimeoutOrNull(10000) {
        jda.on<GuildMessageReactionAddEvent>()
            .filter { it.messageIdLong == botMessage.idLong }
            .filter { !it.user.isBot }
            .filter { it.reactionEmote.isEmoji && it.reactionEmote.emoji == WHITE_CHECK_MARK }
            .first()
    } ?: fail {
        // fail throws a exception that is handle by the command framework
        // meaning that your code below will not run if `fail {}` get called
        // this block will be executed when it fails
        botMessage.editMessage("Timeout :(").await()
    }

    botMessage.editMessage("Thanks for clicking!").await()
}
```