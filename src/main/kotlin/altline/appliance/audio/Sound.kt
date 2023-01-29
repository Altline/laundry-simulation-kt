package altline.appliance.audio

import com.adonax.audiocue.AudioCue

enum class Sound(
    fileName: String,
    polyphony: Int = 1
) {

    PowerOn("power_on"),
    PowerOff("power_off"),

    TumbleStart("tumble_start"),
    TumbleStop("tumble_stop"),
    TumbleLoop("tumble_loop");

    val clip: AudioCue = getSound(fileName, polyphony)

    private fun getSound(name: String, polyphony: Int): AudioCue {
        return AudioCue.makeStereoCue(javaClass.getResource("/sounds/$name.wav"), polyphony)
    }
}