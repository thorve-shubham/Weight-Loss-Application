package com.cabcta10.weightlossapplication.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.PermissionStatus

class PermissionsViewModel : ViewModel() {
    val visiblePermissionDialogQueue = mutableStateListOf<String>(

    )

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeLast()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted) {
            visiblePermissionDialogQueue.add(0, permission)
        }
    }
}