package com.jac.jacfilter

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/** Explain to user why the permission is required. */
class PermissionRequestFragment: Fragment(R.layout.fragment_request_permission) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.fragment_request_permission_button_text_view).setOnClickListener {
            // requestPermissionLauncher.launch(SYSTEM_ALERT_WINDOW)
            requireActivity().startActivity(Intent(ACTION_MANAGE_OVERLAY_PERMISSION))
        }
    }

    override fun onStart() {
        super.onStart()
        if (!SettingsManager.shouldRequestPermissions(this))
            findNavController().navigate(R.id.action_request_permission_to_filter_settings)
    }
}