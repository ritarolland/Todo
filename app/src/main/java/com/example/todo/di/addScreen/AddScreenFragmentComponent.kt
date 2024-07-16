package com.example.todo.di.addScreen

import com.example.todo.ui.addScreen.AddScreenFragment
import dagger.Subcomponent

@AddScreenFragmentScope
@Subcomponent
interface AddScreenFragmentComponent {
    fun inject(fragment: AddScreenFragment)
}