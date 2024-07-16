package com.example.todo.di.activity

import com.example.todo.MainActivity
import com.example.todo.di.addScreen.AddScreenFragmentComponent
import com.example.todo.di.mainScreen.MainScreenFragmentComponent
import dagger.Subcomponent

@Subcomponent(modules = [MainActivityModule::class])
@MainActivityScope
interface MainActivityComponent {
    fun mainActivity(): MainActivity
    fun inject(activity: MainActivity)
    fun mainScreenFragmentComponent(): MainScreenFragmentComponent
    fun addScreenFragmentComponent(): AddScreenFragmentComponent
}
