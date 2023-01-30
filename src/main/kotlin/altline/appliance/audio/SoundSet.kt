package altline.appliance.audio

enum class SoundSet(
    val mainSound: SoundClip,
    val startSound: SoundClip? = null,
    val stopSound: SoundClip? = null
) : Sound {
    PreFill(
        mainSound = SoundClip.PreFillLoop,
        startSound = SoundClip.PreFillStart,
        stopSound = SoundClip.PreFillStop
    ),

    MainFill(
        mainSound = SoundClip.MainFillLoop,
        startSound = SoundClip.MainFillStart,
        stopSound = SoundClip.MainFillStop
    ),

    SoftenerFill(
        mainSound = SoundClip.SoftenerFillLoop,
        startSound = SoundClip.SoftenerFillStart,
        stopSound = SoundClip.SoftenerFillStop
    ),

    Tumble(
        mainSound = SoundClip.TumbleLoop,
        startSound = SoundClip.TumbleStart,
        stopSound = SoundClip.TumbleStop
    )
}