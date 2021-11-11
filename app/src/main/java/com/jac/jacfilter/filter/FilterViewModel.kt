package com.jac.jacfilter.filter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jac.jacfilter.R

class FilterViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        /** Opacity factor to multiply to match service value with the UI one. */
        private const val OPACITY_FACTOR = 100F
    }

    /** Opacity internal mutable live value. */
    private var mutableOpacityLiveData = MutableLiveData<Int>()
    /** Opacity public live value (read-only). */
    val opacityLiveData: LiveData<Int> get() = mutableOpacityLiveData

    /** Enabled internal mutable live value. */
    var mutableEnabledLiveData = MutableLiveData<Boolean>()
    /** Enabled public live value (read-only) */
    val enabledLiveData: LiveData<Boolean> = mutableEnabledLiveData

    /** Opacity service instance, bound on view model retrieving. */
    @SuppressLint("StaticFieldLeak") // Service is bound on ViewModel retrieving and unbound on release.
    private var filterService: FilterService? = null

    private val filterServiceListener = object:FilterService.Listener {
        override fun onOpacityChanged() {
            mutableOpacityLiveData.value = filterService?.opacity?.times(OPACITY_FACTOR)?.toInt()
        }

        override fun onEnabledChanged() {
            mutableEnabledLiveData.value = filterService?.enabled
        }

    }

    /** Opacity service connection listener. */
    private val filterServiceConnection = object:ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            filterService = (service as FilterService.LocalBinder).getService().also {
                it.register(filterServiceListener)
            }
            filterServiceListener.onEnabledChanged()
            filterServiceListener.onOpacityChanged()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            filterService?.run {
                unregister(filterServiceListener)
                filterService = null
            }
        }
    }

    init {
        application.bindService(Intent(application, FilterService::class.java),
            filterServiceConnection, Context.BIND_AUTO_CREATE)
    }

    /**
     * Enable/disable the overlay.
     * @param value the enabled state to set.
     */
    fun setEnabled(value: Boolean) { filterService?.enabled = value }

    /**
     * Set filter's opacity.
     * @param value the opacity value to apply.
     */
    fun setOpacity(value: Int) { filterService?.opacity = value.toFloat() / OPACITY_FACTOR }

    override fun onCleared() {
        getApplication<Application>().unbindService(filterServiceConnection)
    }
}