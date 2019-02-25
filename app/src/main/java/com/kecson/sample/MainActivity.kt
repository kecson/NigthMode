package com.kecson.sample

import android.app.UiModeManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.service.quicksettings.TileService
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import om.kecson.sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uiMode = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        cb_card_mode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                uiMode.enableCarMode(0)
            } else {
                uiMode.disableCarMode(0)
            }
        }
        cb_card_mode.visibility = View.GONE
        cb_night_mode.setOnCheckedChangeListener { _, isChecked ->
            changeNightMode(isChecked)
            updateTileState()
        }


    }

    private fun updateTileState() {
        val componentName = ComponentName(applicationContext, MyTileService::class.java)
        TileService.requestListeningState(this@MainActivity, componentName)
    }

    private fun changeNightMode(isChecked: Boolean) {
        val uiMode = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        uiMode.nightMode = if (isChecked) {
            UiModeManager.MODE_NIGHT_YES
        } else {
            UiModeManager.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        delegate.setLocalNightMode(uiMode.nightMode)
        delegate.applyDayNight()
    }

    private fun isNightMode(): Boolean {
        val uiMode = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiMode.nightMode == UiModeManager.MODE_NIGHT_YES
    }

    override fun onResume() {
        super.onResume()
        cb_night_mode.isChecked = isNightMode()
    }
}