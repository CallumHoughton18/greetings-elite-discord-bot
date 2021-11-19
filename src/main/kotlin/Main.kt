import LavaAudioPlayer.LavaAudioPlayer
import dev.kord.core.Kord

private val BOT_TOKEN = try {
    ClassLoader.getSystemResource("bot-token.txt").readText().trim()
} catch (error: Exception) {
    throw RuntimeException("Failed to load bot token. Make sure to create a file named bot-token.txt in" +
            " src/main/resources and paste the bot token into that file.", error)
}


suspend fun main(args: Array<String>) {
    val greetingsVid = ClassLoader.getSystemResource("greetings.mp4")
    val greetingsAudio = ClassLoader.getSystemResource("greetings.mp3")

    val client = Kord(BOT_TOKEN)
    val audioPlayer = LavaAudioPlayer()
    val bot = Bot(client, greetingsVid, greetingsAudio, audioPlayer)
    bot.start()
}
