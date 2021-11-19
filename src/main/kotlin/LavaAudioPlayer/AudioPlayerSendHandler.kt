package LavaAudioPlayer

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame
import java.nio.ByteBuffer

class AudioPlayerSendHandler(private val scheduler: TrackScheduler) : AudioLoadResultHandler {
    private val buffer: ByteBuffer = ByteBuffer.allocate(1024)
    private val frame: MutableAudioFrame = MutableAudioFrame()

    init {
        frame.setBuffer(buffer)
    }

    override fun trackLoaded(track: AudioTrack?) {
        scheduler.queue(track!!)
    }

    override fun playlistLoaded(playlist: AudioPlaylist?) {
    }

    override fun noMatches() {
    }

    override fun loadFailed(exception: FriendlyException?) {
    }
}