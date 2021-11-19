package LavaAudioPlayer

import IAudioPlayer
import PlayerEventType
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking

class LavaAudioPlayer : IAudioPlayer {

    private val playerManager: DefaultAudioPlayerManager = DefaultAudioPlayerManager()
    private val player: AudioPlayer
    private val scheduler: TrackScheduler

    init {
        AudioSourceManagers.registerLocalSource(playerManager)
        AudioSourceManagers.registerRemoteSources(playerManager)
        player = playerManager.createPlayer()
        scheduler = TrackScheduler(player)
        player.addListener(scheduler)
    }

    override val audioData: ByteArray?
        get() = player.provide()?.data

    override val onPlayerEvent: Flow<PlayerEventType> = scheduler.onPlayerEvent

    override fun initializePlayer() {
        TODO("Not yet implemented")
    }

    override fun loadLocalSoundFile(audioPath: String) {
        playerManager.loadItem(audioPath, AudioPlayerSendHandler(scheduler))
    }
}