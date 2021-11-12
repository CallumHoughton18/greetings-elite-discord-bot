import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.kord.common.annotation.KordVoice
import dev.kord.core.Kord
import dev.kord.core.any
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.on
import dev.kord.rest.NamedFile
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.URL


// Maybe dictionary of enum to method to enact would work better?
enum class GreetingsType {
    GREET_SENDER, GREET_MENTIONED_USERS, GREET_ALL, NONE
}
@OptIn(KordVoice::class)
class Bot(private val client: Kord, greetingsVideoURL: URL, greetingsAudioURL: URL) {
    // If message == @greetinsbot 'greet' then play generic greetings video
    // if message contains greetings at member, then greet specific member
    // if message == "@greetingsbot join and greet"
    private val personalGreetings = arrayOf("hello", "greet me", "greetings", "greet")
    private val otherUserGreetings = arrayOf("greet them", "greet", "say hello to")
    private val allGreetings = "greet all"

    init {
        client.on<dev.kord.core.event.message.MessageCreateEvent> {
            val users = message.mentionedUsers
            if (!users.any { x -> x.isBot }) return@on
            val greetingsType = parseGreetingsType(message)

            message.channel.createMessage {
                val videoInputStream = greetingsVideoURL.openStream()
                this.files.add(0, NamedFile("greetings.mp4", videoInputStream))
                when (greetingsType) {
                    GreetingsType.GREET_SENDER -> {
                        content = "Greetings ${message.author?.mention}!"
                    }
                    GreetingsType.GREET_MENTIONED_USERS -> TODO()
                    GreetingsType.GREET_ALL -> TODO()
                    GreetingsType.NONE -> TODO()
                }
            }

            val userVoiceChannel = message.getAuthorAsMember()?.getVoiceState()?.channelId!!
            val channel = client.getChannelOf<VoiceChannel>(userVoiceChannel)!!

            var connection: VoiceConnection? = null
            val playerManager = DefaultAudioPlayerManager()
            AudioSourceManagers.registerLocalSource(playerManager)
            AudioSourceManagers.registerRemoteSources(playerManager)
            val player = playerManager.createPlayer()
            val scheduler = TrackScheduler(player) { runBlocking { connection?.leave()} }
            player.addListener(scheduler)
            playerManager.loadItem(greetingsAudioURL.path, AudioPlayerSendHandler(scheduler))

            connection = channel.connect {
                audioProvider { AudioFrame.fromData(player.provide()?.data) }
            }
        }
    }

    private suspend fun parseGreetingsType(message: Message): GreetingsType {
        val msgContentLower = message.content.lowercase().replace(Regex("<.*?>"), "").trim()

        if (msgContentLower == allGreetings) return GreetingsType.GREET_ALL
        // if mentions contain any other users AND message content matches expected greetings text
        if (message.mentionedUsers.any { user -> !user.isBot }
            && otherUserGreetings.any{greetings -> msgContentLower == greetings}) {
            return GreetingsType.GREET_MENTIONED_USERS
        }
        if (personalGreetings.any{greetings -> msgContentLower == greetings}) return GreetingsType.GREET_SENDER
        return GreetingsType.NONE
    }

    suspend fun start() {
        client.login()
    }
}