package com.kecson.sample

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.support.annotation.RequiresApi
import android.util.Log

/**
 * @author chenKeSheng
 * @version V1.0
 * @date 2019-02-25 12:42
 */
@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("Registered")
class MyTileService : TileService() {

    override fun onClick() {
        val isNightMode = isNightMode()
        val newNightNode = isNightMode.not()
        Log.w("MyTileService", "onClick")
        setNightMode(newNightNode)
        updateQsTile(newNightNode)
    }

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        updateQsTile()
//        return super.onStartCommand(intent, flags, startId)
//    }

    override fun onStartListening() {
        updateQsTile()
    }

    private fun updateQsTile(nightMode: Boolean = isNightMode()) {
        Log.w("MyTileService", "updateQsTile nightMode=$nightMode")
        qsTile?.state = if (nightMode) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }
        qsTile?.updateTile()
    }

    private fun isNightMode(): Boolean {
        val uiMode = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiMode.nightMode == UiModeManager.MODE_NIGHT_YES
    }

    private fun setNightMode(nightNode: Boolean) {
        Log.w("MyTileService", "setNightMode nightMode=$nightNode")
        val uiMode = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        uiMode.nightMode = if (nightNode) {
            UiModeManager.MODE_NIGHT_YES
        } else {
            UiModeManager.MODE_NIGHT_NO
        }
    }

    /**
     * 系统与您的磁贴之间的第一次交互是用户首次将磁贴添加到其快速设置窗格时。
     * 此时，系统将绑定到您的TileSevice并调用onTileAdded（）。这是进行任何一次性初始化的适当位置。
     */
    override fun onTileAdded() {
        updateQsTile()
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
    }

    override fun onBind(intent: Intent): IBinder? {
        updateQsTile()
        return super.onBind(intent)
    }
}
