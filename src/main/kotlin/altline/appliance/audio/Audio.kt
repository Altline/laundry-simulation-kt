package altline.appliance.audio

object Audio {

    fun init() {
        Sound.values().forEach {
            it.clip.open()
        }
    }

    fun close() {
        Sound.values().forEach {
            it.clip.close()
        }
    }

    fun play(sound: Sound) {
        sound.clip.play()
    }

}