package com.jac.jacfilter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.jac.jacfilter.filter.FilterViewModel

/** Main activity class that allow the user to check and set filter configuration. */
class MainActivity : AppCompatActivity() {

    /** Opacity value as string. */
    private lateinit var opacityValueTextView: TextView
    /** Opacity value and configuration as seekbar. */
    private lateinit var opacitySelectionSeekBar: SeekBar
    /** Filter activation/deactivation as toggle button. */
    private lateinit var enableOpacityToggleButton: ToggleButton

    /** Filter view model. */
    private val filterViewModel: FilterViewModel by viewModels()

    /** Observer over opacity value. */
    private val opacityObserver =
        Observer<Int> { t ->
            opacityValueTextView.text = t.toString()
            opacitySelectionSeekBar.progress = t
        }

    /** Opacity seekbar change listener. */
    private val onSeekBarChangeListener = object:OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            filterViewModel.setOpacity(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // Do nothing
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // Do nothing
        }
    }

    /** Toggle overlay activation/deactivation change listener. */
    private val onToggleButtonChangeListener = CompoundButton.OnCheckedChangeListener {
            _, isChecked -> filterViewModel.setEnabled(isChecked)
    }

    /** Launcher to request overlay permission. */
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (!it) {
            Toast.makeText(this, R.string.permission_denial_text, Toast.LENGTH_SHORT).show()
            finish()
            return@registerForActivityResult
        }
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        opacityValueTextView = findViewById(R.id.opacity_value_text_view)
        opacitySelectionSeekBar = findViewById(R.id.opacity_selection_seek_bar)
        enableOpacityToggleButton = findViewById(R.id.enable_opacity)
    }

    private fun init() {
        filterViewModel.opacityLiveData.observe(this, opacityObserver)
        opacitySelectionSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener)

        enableOpacityToggleButton.isEnabled = filterViewModel.enabledLiveData.value == true
        enableOpacityToggleButton.setOnCheckedChangeListener(onToggleButtonChangeListener)
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.SYSTEM_ALERT_WINDOW)
            return;
        }
        init()
    }

    override fun onDestroy() {
        super.onDestroy()

        opacitySelectionSeekBar.setOnSeekBarChangeListener(null)
        filterViewModel.opacityLiveData.removeObserver(opacityObserver)
    }
}