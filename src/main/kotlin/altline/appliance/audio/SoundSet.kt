package altline.appliance.audio

import altline.appliance.audio.SoundClip.*

enum class SoundSet(
    val mainSound: SoundClip,
    val startSound: SoundClip? = null,
    val stopSound: SoundClip? = null
) : Sound {
    PreFill(
        mainSound = PreFillLoop,
        startSound = PreFillStart,
        stopSound = PreFillStop
    ),

    MainFill(
        mainSound = MainFillLoop,
        startSound = MainFillStart,
        stopSound = MainFillStop
    ),

    SoftenerFill(
        mainSound = SoftenerFillLoop,
        startSound = SoftenerFillStart,
        stopSound = SoftenerFillStop
    ),

    Tumble(
        mainSound = TumbleLoop,
        startSound = TumbleStart,
        stopSound = TumbleStop
    )
}