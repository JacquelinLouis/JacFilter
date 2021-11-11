package com.jac.jacfilter.filter

import android.content.Context
import com.jac.jacfilter.R

/**
 * Manager implementation over preferences.
 * Return preferences' values or default ones if none is found.
 */
class PreferencesManager(context: Context) {

    companion object {
        /** Shared preferences' name. */
        private val SHARED_PREF_NAME = PreferencesManager::class.java.name
        /** Shared preferences enabled state. */
        private const val ENABLED_STATE = "ENABLED_STATE"
        /** Shared preferences window's opacity. */
        private const val WINDOW_OPACITY = "WINDOW_OPACITY"
        /** Factor to multiply/divide window's opacity to convert to alpha value. */
        private const val OPACITY_FACTOR = 100F
    }

    /** Android shared preferences used to store to and get preferences values from. */
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    /** The default enabled state to apply if none is found. */
    private var defaultEnabled = context.resources.getBoolean(R.bool.default_enabled)

    /** Enabled state. */
    var enabled: Boolean get() = sharedPreferences.getBoolean(ENABLED_STATE, defaultEnabled)
        set(value) = sharedPreferences.edit().putBoolean(ENABLED_STATE, value).apply()

    /** The default opacity to apply if none is found. */
    private val defaultOpacity = context.resources.getInteger(R.integer.default_opacity)

    /** Opacity value. */
    var opacity: Float get() = sharedPreferences.getFloat(WINDOW_OPACITY, defaultOpacity.toFloat()) / OPACITY_FACTOR
        set(value) = sharedPreferences.edit().putFloat(WINDOW_OPACITY, value * OPACITY_FACTOR).apply()
}