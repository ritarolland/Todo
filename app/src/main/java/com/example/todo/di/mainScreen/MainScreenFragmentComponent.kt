package com.example.todo.di.mainScreen

import com.example.todo.ui.mainScreen.MainScreenFragment
import dagger.Subcomponent

@MainScreenFragmentScope
@Subcomponent
interface MainScreenFragmentComponent {
    fun inject(fragment: MainScreenFragment)
}