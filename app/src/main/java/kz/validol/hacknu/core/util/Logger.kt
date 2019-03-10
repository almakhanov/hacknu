package kz.validol.hacknu.core.util

import android.util.Log

object Logger {

    fun msg(tag: String, msg: Any?) {
        Log.i(tag, "$msg")
    }

    fun msg(msg: Any?) {
        msg("MSG", "$msg")
    }

    fun api(msg: String?) {
        msg("API", "$msg")
    }
}