package com.painandpanic.blossombuddy.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionOnboardingScreen(
    navigateToHome: () -> Unit
) {
    Column() {
        Text(text = "Enable Your Full Experience!")
        Text(text = "At BlossomBuddy, we are committed to delivering a seamless and interactive user experience. To unlock the full potential of our app, we request certain permissions that allow us to integrate your real-world experiences with our digital environment. ")
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Grant camera access")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Grant gallery access")
        }
    }
}