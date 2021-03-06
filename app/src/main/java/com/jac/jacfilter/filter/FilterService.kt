package com.jac.jacfilter.filter

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.IBinder
import android.view.View.inflate
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.jac.jacfilter.R

class FilterService: Service() {

    interface Listener {
        fun onOpacityChanged()

        fun onEnabledChanged()
    }

    /** Local bind for app's internal usage only. */
    inner class LocalBinder: Binder() {
        /**
         * Get service.
         * @return the filter service instance.
         */
        fun getService(): FilterService = this@FilterService
    }

    /** Data class containing the window overlay manager and layout. */
    private data class WindowOverlay(val windowManager: WindowManager,
                                     val viewOverlay: ConstraintLayout)

    /** User's preferences manager. */
    private lateinit var preferencesManager: PreferencesManager

    /** Notification manager. */
    private val notificationManager = FilterNotificationManager()

    /** The window overlay to use over the screen. */
    private var windowOverlay: WindowOverlay? = null

    /** Overlay configuration. */
    private val viewOverlayParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    and WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSPARENT
        )
    }

    /** Local binder instance, to return the same over multiple bindings. */
    private val localBinder = LocalBinder()

    private val listeners = HashSet<Listener>()

    override fun onCreate() {
        preferencesManager = PreferencesManager(this)
        opacity = preferencesManager.opacity
        enabled = preferencesManager.enabled
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        enabled = false
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return localBinder
    }

    fun register(listener: Listener) = listeners.add(listener)

    fun unregister(listener: Listener) = listeners.remove(listener)

    /** Set window opacity. */
    var opacity: Float get() = preferencesManager.opacity
        set(value) {
            if (enabled) {
                windowOverlay?.run {
                    viewOverlay.alpha = value
                    windowManager.updateViewLayout(viewOverlay, viewOverlayParams)
                }
            }
            preferencesManager.opacity = value
            listeners.forEach(Listener::onOpacityChanged)
        }

    /** Enable/disable the window overlay. */
    var enabled: Boolean get() = windowOverlay != null
        set(value){
            if (value) {
                windowOverlay?:let {
                    windowOverlay = WindowOverlay(
                        getSystemService(WINDOW_SERVICE) as WindowManager,
                        inflate(this, R.layout.view_overlay, null) as ConstraintLayout
                    ).apply {
                        notificationManager.enable(this@FilterService)
                        viewOverlay.alpha = 1F - preferencesManager.opacity
                        windowManager.addView(viewOverlay, viewOverlayParams)
                    }
                }
            } else {
                windowOverlay?.apply {
                    windowManager.removeView(viewOverlay)
                    windowOverlay = null
                    notificationManager.disable(this@FilterService)
                }
            }
            preferencesManager.enabled = value
            listeners.forEach(Listener::onEnabledChanged)
        }
}