package kz.validol.hacknu.scanner


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import kz.validol.hacknu.R
import kz.validol.hacknu.book.BookActivity
import kz.validol.hacknu.book.BookActivity.Companion.BOOK_ISNB
import org.koin.android.ext.android.inject
import org.koin.standalone.KoinComponent

class ScannerFragment : Fragment(), KoinComponent {

    private lateinit var qrCodeView: DecoratedBarcodeView
    private val api: Api by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr_code_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
//        val builder = AlertDialog.Builder(context)
//        val view = LayoutInflater.from(context).inflate(R.layout.qr_code_response,null)
//        val closeBtn = view.findViewById<Button>(R.id.close)
//        builder.setView(view)
//        val dialog = builder.create()
//        closeBtn.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        qrCodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        qrCodeView.pause()
    }


    private fun bindView(view: View) = with(view) {
        qrCodeView = findViewById(R.id.zxing_barcode_scanner)
        qrCodeView.apply {
            resume()
            decodeContinuous(object: BarcodeCallback {
                @SuppressLint("CheckResult")
                override fun barcodeResult(result: BarcodeResult?) {
                    api.changeReader(result.toString(), App.user?.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d("result",it.toString())
                            val intent = Intent(context, BookActivity::class.java)
                            intent.putExtra(BOOK_ISNB,it.toString())
                            startActivity(intent)
                        },{
                            Log.d("error",it.message)
                        })

                }

                override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {

                }

            })
        }
//        closeScanner = findViewById(R.id.closeScanner)
//        closeScanner.setOnClickListener {
//            (activity as BaseActivity).onBackPressed()
//        }
    }

    companion object {
        fun newInstance(data: Bundle? = null): ScannerFragment {
            val fragment = ScannerFragment()
            fragment.arguments = data
            return fragment
        }
    }
}
