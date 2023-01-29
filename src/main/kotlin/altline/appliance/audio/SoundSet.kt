package altline.appliance.audio

enum class SoundSet(
    val mainSound: Sound,
    val startSound: Sound? = null,
    val stopSound: Sound? = null
) {
    Tumble(
        mainSound = Sound.TumbleLoop,
        startSound = Sound.TumbleStart,
        stopSound = Sound.TumbleStop
    )
}