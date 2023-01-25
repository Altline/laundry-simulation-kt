package altline.appliance.audio

import com.adonax.audiocue.AudioCue

enum class Sound(
    fileName: String
) {

    TumbleStart("tumble_start");

    val clip: AudioCue = getSound(fileName)

    private fun getSound(name: String, polyphony: Int = 1): AudioCue {
        return AudioCue.makeStereoCue(javaClass.getResource("/sounds/$name.wav"), polyphony)
    }
}