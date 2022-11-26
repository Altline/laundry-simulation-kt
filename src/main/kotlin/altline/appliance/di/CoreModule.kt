package altline.appliance.di

import altline.appliance.data.World
import altline.appliance.ui.MainViewModel
import altline.appliance.ui.mapper.LaundryMapper
import org.koin.dsl.module

val coreModule = module {

    factory { World() }

    factory { MainViewModel(get(), get()) }

    factory { LaundryMapper() }

}