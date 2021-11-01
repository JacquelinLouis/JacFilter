package com.jac.jacfilter

import android.Manifest.permission.SYSTEM_ALERT_WINDOW
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.jac.jacfilter.filter.FilterSettingsFragment

/** Main activity class that allow the user to check and set filter configuration. */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val isAllowed = Settings.canDrawOverlays(this)
        val shouldRequest = !isAllowed && shouldShowRequestPermissionRationale(SYSTEM_ALERT_WINDOW)
        if (shouldRequest) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<PermissionRequestFragment>(R.id.main_activity_fragment_container)
            }
            return
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<FilterSettingsFragment>(R.id.main_activity_fragment_container)
        }
    }
}