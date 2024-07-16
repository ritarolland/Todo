package com.example.todo.di

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import com.example.todo.ui.mainScreen.MainScreenFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object NavigationModule {

    @Provides
    @FragmentScoped
    fun provideNavController(fragment: Fragment): NavHostController {
        return NavHostController(fragment.requireContext())
    }
}