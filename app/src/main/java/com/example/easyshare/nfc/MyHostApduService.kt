package com.example.easyshare.nfc
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHostApduService : HostApduService() {
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        Log.i(TAG, "Testing received ")
        val link = extras?.getString("link")
        return link!!.toByteArray(Charsets.UTF_8)
    }

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "NFC Deactivated: $reason")
    }

    companion object {
        private const val TAG = "MyHostApduService"
    }
}
