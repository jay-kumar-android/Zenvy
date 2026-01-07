package com.example.zenvy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * [Purpose] - Application class for Hilt dependency injection setup
 * Architecture Layer: Application
 * 
 * WHY: Required by Hilt to initialize DI container at app startup
 */
@HiltAndroidApp
class ZenvyApplication : Application()
