package altline.appliance.audio

import com.adonax.audiocue.AudioCue
import com.adonax.audiocue.AudioCueInstanceEvent
import com.adonax.audiocue.AudioCueListener
import kotlinx.coroutines.*
import kotlin.coroutines.resume

object Audio {

    private val soundJobMap = mutableMapOf<PlayingSound.Set, Job>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun init() {
        SoundClip.values().forEach {
            it.cue.open()
        }
    }

    fun close() {
        SoundClip.values().forEach {
            it.cue.close()
        }
    }

    fun play(sound: Sound): PlayingSound {
        return when (sound) {
            is SoundClip -> play(soundClip = sound)
            is SoundSet -> play(soundSet = sound)
        }
    }

    fun play(soundClip: SoundClip): PlayingSound {
        val instanceId = soundClip.cue.play()
        return PlayingSound.Single(soundClip, instanceId)
    }

    fun play(soundSet: SoundSet, skipStartSound: Boolean = false): PlayingSound {
        val playingSound = PlayingSound.Set(soundSet)

        coroutineScope.launch {
            if (soundSet.startSound != null && !skipStartSound) {
                playAndWait(soundSet.startSound)
            }

            playAndWait(soundSet.mainSound, loop = true)
        }.also { job ->
            soundJobMap[playingSound] = job
        }

        return playingSound
    }

    fun stop(playingSound: PlayingSound, skipStopSound: Boolean = false) {
        when (playingSound) {
            is PlayingSound.Single -> {
                playingSound.instanceId.takeIf { it != -1 }?.let { id ->
                    with(playingSound.sound.cue) {
                        if (getIsPlaying(id)) {
                            stop(id)
                            releaseInstance(id)
                        }
                    }
                }
            }

            is PlayingSound.Set -> {
                soundJobMap[playingSound]?.let { job ->
                    job.cancel()
                    soundJobMap.remove(playingSound)
                }

                if (playingSound.soundSet.stopSound != null && !skipStopSound) {
                    play(playingSound.soundSet.stopSound)
                }
            }
        }
    }

    private suspend fun playAndWait(sound: SoundClip, loop: Boolean = false) {
        val instanceId = sound.cue.play(1.0, 0.0, 1.0, if (loop) -1 else 0)
        if (instanceId != -1) awaitSound(sound, instanceId)

    }

    private suspend fun awaitSound(sound: SoundClip, instanceId: Int) {
        suspendCancellableCoroutine { cont ->
            val audioCueListener = object : AudioCueListener {
                override fun instanceEventOccurred(event: AudioCueInstanceEvent?) {
                    if (event?.type == AudioCueInstanceEvent.Type.STOP_INSTANCE && event.instanceID == instanceId) {
                        sound.cue.removeAudioCueListener(this)
                        cont.resume(Unit)
                    }
                }

                override fun audioCueOpened(now: Long, threadPriority: Int, bufferSize: Int, source: AudioCue?) {
                    /* no-op */
                }

                override fun audioCueClosed(now: Long, source: AudioCue?) {
                    /* no-op */
                }
            }

            sound.cue.addAudioCueListener(audioCueListener)

            cont.invokeOnCancellation {
                sound.cue.removeAudioCueListener(audioCueListener)
                sound.cue.stop(instanceId)
                sound.cue.releaseInstance(instanceId)
            }
        }
    }
}