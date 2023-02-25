package altline.appliance.ui

import altline.appliance.audio.Audio
import altline.appliance.audio.PlayingSound
import altline.appliance.audio.SoundClip
import altline.appliance.audio.SoundSet
import altline.appliance.measure.Spin
import altline.appliance.measure.isNegligible
import altline.appliance.measure.isNotNegligible
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.phase.DetergentFillPhase
import altline.appliance.washing.laundry.washCycle.phase.DrainPhase
import altline.appliance.washing.laundry.washCycle.phase.SoftenerFillPhase
import altline.appliance.washing.laundry.washCycle.phase.SpinPhase
import io.nacular.measured.units.*

class WasherSoundPlayer {

    private val playingSounds = mutableListOf<Pair<SoundOrder, PlayingSound>>()

    private var spinStartTime: Long = 0

    /**
     * Play a one-shot sound clip.
     */
    fun playClip(soundClip: SoundClip) {
        Audio.play(soundClip)
    }

    /**
     * Determines which [SoundSet]s should be playing based on the state of the [washer]. If the washer state has
     * changed since the previous update, new sounds will start playing, old sounds will stop, and the rest will
     * continue looping.
     */
    fun updateSounds(washer: StandardLaundryWasherBase) {
        applySounds(buildSoundList(washer))
    }

    private fun buildSoundList(washer: StandardLaundryWasherBase): List<SoundOrder> {
        return buildList {
            val currentSpinSpeed = washer.scanner?.spinSpeed ?: (0 * Spin.rpm)
            val phase = washer.selectedWashCycle.activeStage?.activePhase

            if (currentSpinSpeed.isNegligible()) {
                spinStartTime = System.currentTimeMillis()
            }

            val phaseBasedSound: SoundOrder? = when (phase) {
                is DetergentFillPhase -> SoundOrder(SoundSet.MainFill)
                is SoftenerFillPhase -> SoundOrder(SoundSet.SoftenerFill)
                is DrainPhase -> {
                    when (phase.sections.find { it.active }) {
                        is DrainPhase.FocusedDrainSection -> SoundOrder(SoundSet.DrainFlow)
                        is DrainPhase.WashDrainSection -> SoundOrder(SoundSet.DrainMain)
                        else -> null
                    }
                }

                is SpinPhase -> {
                    when (currentSpinSpeed) {
                        in (0 * Spin.rpm)..(1001 * Spin.rpm) -> SoundOrder(SoundSet.SpinSlow)
                        else -> {
                            if (System.currentTimeMillis() < spinStartTime + 12000) {
                                SoundOrder(SoundSet.SpinSlow, skipStopSound = true)
                            } else {
                                SoundOrder(SoundSet.SpinFast)
                            }
                        }
                    }
                }

                else -> null
            }
            phaseBasedSound?.let { add(it) }

            if (phase !is SpinPhase && currentSpinSpeed.isNotNegligible()) {
                add(SoundOrder(SoundSet.Tumble))
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