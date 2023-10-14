package com.painandpanic.blossombuddy.ui.core.permissions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.painandpanic.blossombuddy.R

@Composable
fun PermissionDialog(
    permissionName: String,
    rationale: String,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(
                    id = R.string.permission_dialog_title,
                    permissionName.lowercase().capitalize(Locale.current)
                )
            )
        },
        text = {
           Text(text = rationale)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
           Button(onClick = if (isPermanentlyDeclined) onGoToAppSettingsClick else onOkClick) {
               Text(
                   text = stringResource(
                       id = if (isPermanentlyDeclined) R.string.permission_dialog_go_to_app_settings_label
                       else R.string.permission_dialog_ok_label
                   )
               )
           }
        },
        dismissButton = {
          Button(onClick = onDismiss) {
            Text(text = stringResource(id = R.string.permission_dialog_dismiss_label))
          }
        },
        modifier = modifier
    )
}



