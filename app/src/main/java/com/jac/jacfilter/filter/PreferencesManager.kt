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
        /** Shared preferences window's opacity. */
        private const val WINDOW_OPACITY = "WINDOW_OPACITY"
        /** Factor to multiply/divide window's opacity to convert to alpha value. */
        private const val OPACITY_FACTOR = 100F
    }

    /** Android shared preferences used to store to and get preferences values from. */
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    /** The default opacity to apply if none is found. */
    private val defaultOpacity = context.resources.getInteger(R.integer.default_opacity)

    /** Opacity value. */
    var opacity: Float get() = sharedPreferences.getFloat(WINDOW_OPACITY, defaultOpacity.toFloat() / OPACITY_FACTOR)
        set(value) = sharedPreferences.edit().putInt(WINDOW_OPACITY, (value * OPACITY_FACTOR).toInt()).apply()
}