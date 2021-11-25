import LavaAudioPlayer.LavaAudioPlayer
import dev.kord.core.Kord
import java.io.File
import java.net.URL
import java.nio.file.Path

private val BOT_TOKEN = try {
    System.getenv("BOT_TOKEN")
} catch (error: Exception) {
    throw RuntimeException("Failed to load bot token. Make sure the BOT_TOKEN environment variable is set", error)
}

// LavaPlayer can't play local sound files when compiled into a jar
// so an AWFUL work around is just pipe the audio from YouTube ?!?!?!
private val GREETINGS_AUDIO_LINK = try {
    System.getenv("GREETINGS_AUDIO_LINK")
} catch (error: Exception) {
    throw RuntimeException("Failed to load greetings video link. Make sure the GREETINGS_AUDIO_LINK environment variable is set",
    error)
}

suspend fun main(args: Array<String>) {
    val greetingsVid = ClassLoader.getSystemResource("greetings.mp4")
    @Suppress("BlockingMethodInNonBlockingContext")

    val client = Kord(BOT_TOKEN)
    val audioPlayer = LavaAudioPlayer()
    val bot = Bot(client, greetingsVid, GREETINGS_AUDIO_LINK, audioPlayer)
    bot.start()
}
