package com.example.qrreaderscan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.qrreaderscan.bean.Code
import com.example.qrreaderscan.ui.detail.Detail
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.IOException


class CameraActivity : AppCompatActivity() {

    private var cameraSource: CameraSource? = null
    private var cameraView: SurfaceView? = null
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1
    private var token = ""
    private var tokenanterior = ""
    private val LOG_TAG = "Barcode Scanner API"
    private var shortAnimationDuration: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        cameraView = findViewById<View>(R.id.camera_view) as SurfaceView?
        initQR();
    }


    fun initQR() {

        // creo el detector qr
        val barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.DATA_MATRIX or Barcode.QR_CODE)
            .build()

        // creo la camara
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            //.setRequestedPreviewSize(320, 320)
            .setAutoFocusEnabled(true) //you should add this feature
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1600, 1024)
            .setRequestedFps(15.0f)
            .build()

        // listener de ciclo de vida de la camara
        cameraView!!.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {

                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(
                        this@CameraActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA
                            )
                        )
                        ;
                        requestPermissions(
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_CAMERA
                        )
                    }
                    return
                } else {
                    try {
                        cameraSource!!.start(cameraView!!.holder)
                    } catch (ie: IOException) {
                        //Log.e("CAMERA SOURCE", ie.message)
                    }

                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource!!.stop()
            }
        })

        // preparo el detector de QR
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems

                if (barcodes.size() > 0) {

                    val code = barcodes.valueAt(0)
                    addStringType(code)
                    //var codeAt = Code(code)
                    //  Log.d("answer", codeAt.toString())

                    textView2?.text = code.displayValue
                    textView2?.apply {
                        alpha = 0f
                        visibility = View.VISIBLE
                        animate()
                            .alpha(1f)
                            .setDuration(shortAnimationDuration.toLong())
                            .setListener(null)
                    }



                }
            }
        })

    }

    private fun addStringType(code: Barcode) {

        var codeAt = Code()
        codeAt.apply {
            txtType = getString(R.string.type_wifi)
            title = code.displayValue
            type = code.valueFormat
        }

        when (code.valueFormat) {
            Barcode.WIFI -> {
                codeAt.img = R.drawable.ic_wifi
                codeAt.tables?.add(
                    if (code.wifi.ssid.isNullOrEmpty()) {
                        null.toString()
                    } else {
                        code.wifi.ssid
                    }
                )
                codeAt.tables?.add(
                    if (code.wifi.password.isNullOrEmpty()) {
                        null.toString()
                    } else {
                        code.wifi.password
                    }
                )
                codeAt.tables?.add(
                    if (code.wifi.encryptionType.toString().isNullOrEmpty()) {
                        null.toString()
                    } else {
                        code.wifi.ssid
                    }
                )


                val intent = Intent(applicationContext, Detail::class.java)
                intent.putExtra("TAG", codeAt.tables?.get(0))
                startActivity(intent)



            }
            Barcode.CALENDAR_EVENT -> {
                //Log.d("answer", "Status" + code.calendarEvent.status)
                //Log.d("answer", "CALENDAR_EVENT-" + code.calendarEvent.organizer)
            }
            Barcode.CONTACT_INFO -> {

            }
            else -> {

            }
        }

    }
}
