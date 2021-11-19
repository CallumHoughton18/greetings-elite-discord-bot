import LavaAudioPlayer.AudioPlayerSendHandler
import LavaAudioPlayer.TrackScheduler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.kord.common.annotation.KordVoice
import dev.kord.core.Kord
import dev.kord.core.any
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.rest.NamedFile
import dev.kord.rest.builder.message.create.UserMessageCreateBuilder
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.net.URL


// Maybe dictionary of enum to method to enact would work better?
enum class GreetingsType {
    GREET_SENDER, GREET_MENTIONED_USERS, GREET_ALL, NONE
}
@OptIn(KordVoice::class)
class Bot(private val client: Kord, greetingsVideoURL: URL, greetingsAudioURL: URL, audioPlayer: IAudioPlayer) {
    // If message == @greetinsbot 'greet' then play generic greetings video
    // if message contains greetings at member, then greet specific member
    // if message == "@greetingsbot join and greet"
    private val personalGreetings = arrayOf("hello", "greet me", "greetings", "greet")
    private val otherUserGreetings = arrayOf("greet them", "greet", "say hello to")
    private val allGreetings = "greet all"

    init {
        client.on<MessageCreateEvent> {
            val users = message.mentionedUsers
            if (!users.any { x -> x.isBot }) return@on
            val greetingsType = parseGreetingsType(message)

            launch(Dispatchers.IO)
            {
                val videoInputStream = greetingsVideoURL.openStream()
                message.channel.createMessage {
                    createGreetingsMessage(this, videoInputStream, greetingsType, this@on)
                }

                val userVoiceChannel = message.getAuthorAsMember()?.getVoiceState()?.channelId!!
                val channel = client.getChannelOf<VoiceChannel>(userVoiceChannel)!!

                val connection: VoiceConnection?

                audioPlayer.loadLocalSoundFile(greetingsAudioURL.path)

                connection = channel.connect {
                    audioProvider { AudioFrame.fromData(audioPlayer.audioData) }
                }

                launch {
                    audioPlayer.onPlayerEvent.collect {
                        when(it) {
                            PlayerEventType.FINISH -> connection.leave()
                        }
                    }
                }
            }
        }
    }

    private fun createGreetingsMessage(
        userMessageCreateBuilder: UserMessageCreateBuilder,
        videoInputStream: InputStream,
        greetingsType: GreetingsType,
        messageCreateEvent: MessageCreateEvent
    ) {
        userMessageCreateBuilder.files.add(0, NamedFile("greetings.mp4", videoInputStream))
        when (greetingsType) {
            GreetingsType.GREET_SENDER -> {
                userMessageCreateBuilder.content = "GREETINGS ${messageCreateEvent.message.author?.mention}!"
            }
            GreetingsType.GREET_MENTIONED_USERS -> "GREETINGS "
            GreetingsType.GREET_ALL -> TODO()
            GreetingsType.NONE -> TODO()
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