package com.jac.jacfilter

import android.Manifest
import android.app.Activity
import android.provider.Settings
import androidx.fragment.app.Fragment

class SettingsManager {

    companion object {

        /**
         * Check if the application should request permissions to user or not.
         * @param fragment the caller to get context from.
         * @return true if the application should request permissions from user, false otherwise.
         */
        fun shouldRequestPermissions(fragment: Fragment): Boolean {
            val isAllowed = Settings.canDrawOverlays(fragment.requireContext())
            return !isAllowed && fragment.shouldShowRequestPermissionRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)
        }
    }
}