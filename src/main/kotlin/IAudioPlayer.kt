import kotlinx.coroutines.flow.Flow

enum class PlayerEventType {
    FINISH
}
interface IAudioPlayer {
    val audioData: ByteArray?
    val onPlayerEvent: Flow<PlayerEventType>
    fun initializePlayer()
    fun loadLocalSoundFile(audioPath: String)
}