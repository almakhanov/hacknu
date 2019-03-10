package kz.validol.hacknu

import android.app.Application
import android.app.Dialog
import android.content.Context
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)
    }

    companion object {
        @JvmStatic var instance: App? = null
            private set

        var loadingDialog: Dialog?= null
        var fcmDeviceId = ""
        var facebookToken = ""

        fun hideProgress(){
            loadingDialog?.dismiss()
        }

        fun showProgress(context: Context){
            loadingDialog?.dismiss()
            loadingDialog = Dialog(context)
            loadingDialog?.apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setContentView(R.layout.custom_progress_bar)
                show()
            }
        }
    }
}