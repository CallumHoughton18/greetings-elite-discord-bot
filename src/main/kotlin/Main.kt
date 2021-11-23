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

suspend fun main(args: Array<String>) {
    val greetingsVid = ClassLoader.getSystemResource("greetings.mp4")
    @Suppress("BlockingMethodInNonBlockingContext")
    // LavaPlayer can't play local sound files when compiled into a jar
    // so an AWFUL work around is just pipe the audio from YouTube ?!?!?!
    // Would never choose to do this in Kotlin again imo
    // hard coded URL because I cba working on this anymore
    val greetingsAudio = "https://www.youtube.com/watch?v=c_w2Nh-mxH4"

    val client = Kord(BOT_TOKEN)
    val audioPlayer = LavaAudioPlayer()
    val bot = Bot(client, greetingsVid, greetingsAudio, audioPlayer)
    bot.start()
}
