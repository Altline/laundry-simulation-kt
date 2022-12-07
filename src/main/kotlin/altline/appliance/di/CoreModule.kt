package altline.appliance.di

import altline.appliance.data.World
import altline.appliance.ui.MainViewModel
import altline.appliance.ui.mapper.LaundryMapper
import altline.appliance.ui.mapper.WasherInfoMapper
import org.koin.dsl.module

val coreModule = module {

    factory { World() }

    factory { MainViewModel(get(), get(), get()) }

    factory { LaundryMapper() }
    factory { WasherInfoMapper() }

}