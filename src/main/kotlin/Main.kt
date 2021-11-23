import LavaAudioPlayer.LavaAudioPlayer
import dev.kord.core.Kord

private val BOT_TOKEN = try {
    System.getenv("BOT_TOKEN")
} catch (error: Exception) {
    throw RuntimeException("Failed to load bot token. Make sure the BOT_TOKEN environment variable is set", error)
}


suspend fun main(args: Array<String>) {
    val greetingsVid = ClassLoader.getSystemResource("greetings.mp4")
    val greetingsAudio = ClassLoader.getSystemResource("greetings.mp3")

    val client = Kord(BOT_TOKEN)
    val audioPlayer = LavaAudioPlayer()
    val bot = Bot(client, greetingsVid, greetingsAudio, audioPlayer)
    bot.start()
}
