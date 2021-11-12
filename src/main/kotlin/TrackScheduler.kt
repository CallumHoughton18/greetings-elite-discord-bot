import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque

class TrackScheduler(private val player: AudioPlayer, private val onEnd: () -> Unit) : AudioEventAdapter() {
    private val queue: BlockingDeque<AudioTrack> = LinkedBlockingDeque()

    fun queue(track: AudioTrack) {
        if (!player.startTrack(track, true)) {
            queue.offer(track)
        }
    }

    fun push(track: AudioTrack) {
        if (!player.startTrack(track, true)) {
            queue.offerFirst(track)
        }
    }

    private fun next() {
        player.startTrack(queue.poll(), false)
    }

    fun skip() {
            val track = player.playingTrack?.makeClone()
            player.stopTrack()
            player.startTrack(track, false)
        }


    fun clearAll() {
        queue.clear()
        player.stopTrack()
    }

    fun iterator(): Iterator<AudioTrack> {
        return queue.iterator()
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        if (endReason == AudioTrackEndReason.FINISHED) {
            onEnd()
            return;
        }
    }

    fun clear() {
        queue.clear()
    }

    fun shuffle() {
        //TODO this can probably be improved/may not be optimal
        val list = queue.toMutableList()
        queue.clear()
        list.shuffle()
        queue.addAll(list)
    }

    fun size(): Int {
        return queue.size
    }

    fun remove(i: Int): AudioTrack? {
        val list = queue.toMutableList()
        val removed = list.removeAt(i - 1)
        queue.clear()
        queue.addAll(list)
        return removed
    }

    fun elevate(i: Int): AudioTrack? {
        val list = queue.toMutableList()
        val elevated = list.removeAt(i - 1)
        queue.clear()
        queue.add(elevated)
        queue.addAll(list)
        return elevated
    }
}