package com.example.todo.data

import android.content.ContentResolver
import android.provider.Settings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceNameRepository @Inject constructor(private val contentResolver: ContentResolver) {
    fun getDeviceName(): String {
        return Settings.Global.getString(contentResolver, Settings.Global.DEVICE_NAME)
    }
}