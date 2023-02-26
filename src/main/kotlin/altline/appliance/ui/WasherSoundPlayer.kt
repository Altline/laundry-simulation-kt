package altline.appliance.ui

import altline.appliance.audio.Audio
import altline.appliance.audio.PlayingSound
import altline.appliance.audio.SoundClip
import altline.appliance.audio.SoundSet
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.isNegligible
import altline.appliance.measure.isNotNegligible
import altline.appliance.substance.isNotEmpty
import altline.appliance.washing.laundry.HouseholdLaundryWasherScanner
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*

class WasherSoundPlayer {

    companion object {
        val TumbleRpmCutoff = 300 * rpm
        val SpinSlowRpmCutoff = 1100 * rpm
    }

    private val playingSounds = mutableListOf<Pair<SoundOrder, PlayingSound>>()

    private var spinStartTime: Long = 0
    private var wasDoorLocked = false

    /**
     * Play a one-shot sound clip.
     */
    fun playClip(soundClip: SoundClip) {
        Audio.play(soundClip)
    }

    /**
     * Determines which [SoundClip]s and [SoundSet]s should be playing based on the state of the [washer]. If the washer
     * state has changed since the previous update, new sounds will start playing, old sound sets will stop, and the
     * rest of the sound sets will continue looping.
     */
    fun updateSounds(washer: StandardLaundryWasherBase) {
        playOneShotClips(washer)
        applySounds(buildSoundList(washer))
    }

    private fun playOneShotClips(washer: StandardLaundryWasherBase) {
        if (wasDoorLocked) {
            if (!washer.doorLocked) {
                playClip(SoundClip.DoorUnlock)
                wasDoorLocked = false
            }
        } else {
            if (washer.doorLocked) {
                playClip(SoundClip.DoorLock)
                wasDoorLocked = true
            }
        }
    }

    private fun buildSoundList(washer: StandardLaundryWasherBase): List<SoundOrder> {
        return buildList {
            fun MutableList<SoundOrder>.addSound(
                soundSet: SoundSet,
                skipStopSound: Boolean = false
            ) = add(SoundOrder(soundSet, skipStopSound))

            (washer.scanner as? HouseholdLaundryWasherScanner)?.run {

                if (spinSpeed.isNegligible()) {
                    spinStartTime = System.currentTimeMillis()
                }

                if (fillingPreWash) addSound(SoundSet.PreFill)
                if (fillingDetergent) addSound(SoundSet.MainFill)
                if (fillingSoftener) addSound(SoundSet.SoftenerFill)
                if (draining && spinSpeed < TumbleRpmCutoff) addSound(
                    if (washLiquid?.isNotEmpty() == true) SoundSet.DrainFlow
                    else SoundSet.DrainMain
                )
                if (spinSpeed.isNotNegligible()) {
                    when {
                        spinSpeed < TumbleRpmCutoff -> addSound(SoundSet.Tumble)
                        spinSpeed < SpinSlowRpmCutoff -> addSound(SoundSet.SpinSlow)
                        else -> {
                            if (System.currentTimeMillis() < spinStartTime + SoundClip.SpinSlowStart.cue.microsecondLength / 1000) {
                                addSound(SoundSet.SpinSlow, skipStopSound = true)
                            } else {
                                addSound(SoundSet.SpinFast)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun applySounds(soundOrders: List<SoundOrder>) {
        val toPlay = soundOrders.toMutableList()

        playingSounds.iterator().run {
            while (hasNext()) {
                val (soundOrder, playingSound) = next()
                if (soundOrder in soundOrders) toPlay.remove(soundOrder)
                else {
                    Audio.stop(playingSound, skipStopSound = soundOrder.skipStopSound)
                    remove()
                }
            }
        }

        toPlay.forEach { soundOrder ->
            val playingSound = Audio.play(soundOrder.soundSet)
            playingSounds += soundOrder to playingSound
        }
    }

    private data class SoundOrder(
        val soundSet: SoundSet,
        val skipStopSound: Boolean = false
    )
}