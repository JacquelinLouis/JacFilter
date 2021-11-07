package com.jac.jacfilter.filter

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jac.jacfilter.R

class FilterSettingsFragment: Fragment(R.layout.fragment_filter_settings) {

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
        }

    private val enabledObserver =
        Observer<Boolean> { t ->
            enableOpacityToggleButton.isChecked = t
        }

    /** Opacity seekbar change listener. */
    private val onSeekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            filterViewModel.setOpacity(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // Do nothing
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // seekBar?.progress?.also { filterViewModel.setOpacity(it) }
        }
    }

    /** Toggle overlay activation/deactivation change listener. */
    private val onToggleButtonChangeListener = CompoundButton.OnCheckedChangeListener {
            _, isChecked -> filterViewModel.setEnabled(isChecked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        opacityValueTextView = view.findViewById(R.id.opacity_value_text_view)
        opacitySelectionSeekBar = view.findViewById(R.id.opacity_selection_seek_bar)
        enableOpacityToggleButton = view.findViewById(R.id.enable_opacity)

        filterViewModel.opacityLiveData.observe(requireActivity(), opacityObserver)
        opacitySelectionSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener)

        filterViewModel.enabledLiveData.observe(requireActivity(), enabledObserver)
        enableOpacityToggleButton.setOnCheckedChangeListener(onToggleButtonChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        opacitySelectionSeekBar.setOnSeekBarChangeListener(null)
        filterViewModel.opacityLiveData.removeObserver(opacityObserver)
    }
}