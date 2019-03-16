package kz.validol.hacknu

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.net.Uri
import com.vk.sdk.VKSdk
import kz.validol.hacknu.entities.User
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        startKoin(this, appModules)
    }

    companion object {
        @JvmStatic
        var instance: App? = null
            private set

        var loadingDialog: Dialog? = null
        var fcmDeviceId = ""
        var facebookToken = ""
        var user: User? = null

        fun hideProgress() {
            loadingDialog?.dismiss()
        }

        fun showProgress(context: Context) {
            loadingDialog?.dismiss()
            loadingDialog = Dialog(context)
            loadingDialog?.apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setContentView(R.layout.custom_progress_bar)
                show()
            }
        }

        var profilePhotoUri: Uri? = null
    }
}