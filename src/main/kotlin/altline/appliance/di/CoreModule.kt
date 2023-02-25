package altline.appliance.di

import altline.appliance.data.World
import altline.appliance.ui.MainViewModel
import altline.appliance.ui.WasherSoundPlayer
import altline.appliance.ui.mapper.*
import org.koin.dsl.module

val coreModule = module {

    factory { World() }

    factory { MainViewModel(get(), get(), get(), get(), get()) }

    factory { LaundryMapper(get()) }
    factory { WasherMapper(get()) }
    factory { WasherInfoMapper(get(), get()) }
    factory { ColorMapper() }
    factory { StringMapper() }

    factory { WasherSoundPlayer() }
}