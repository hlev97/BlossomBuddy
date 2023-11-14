package com.painandpanic.blossombuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.painandpanic.blossombuddy.ui.theme.BlossomBuddyTheme
import com.painandpanic.blossombuddy.navigation.BlossomNavGraph
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {
    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlossomBuddyTheme {
                KoinAndroidContext {
                    BlossomNavGraph()
                }
            }
        }
    }
}

