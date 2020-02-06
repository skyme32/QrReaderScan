package com.example.qrreaderscan

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.qrreaderscan.bean.Code
import com.example.qrreaderscan.ui.detail.Detail
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector


class CameraActivity : AppCompatActivity() {

    private val myPermissionsRequestCamera = 1
    private lateinit var svBarcode: SurfaceView
    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var code: Barcode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        initQR2()

    }


    private fun initQR2() {

        var taskHandler = Handler()
        var runnable = Runnable {
            cameraSource.stop()


            addStringType()


            startActivity(intent)
            taskHandler.removeCallbacksAndMessages(null)
            finish()
        }


        svBarcode = findViewById(R.id.camera_view)

        detector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        detector.setProcessor(object : Detector.Processor<Barcode> {

            override fun release() {}

            override fun receiveDetections(detect: Detector.Detections<Barcode>?) {
                val barcodes = detect?.detectedItems
                if (barcodes!!.size() > 0) {

                    code = barcodes.valueAt(0)
                    taskHandler.post(runnable)
                }
            }
        })

        cameraSource = CameraSource.Builder(this, detector).setRequestedPreviewSize(1024, 768)
            .setRequestedFps(30f).setAutoFocusEnabled(true).build()

        svBarcode.holder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                if (ContextCompat.checkSelfPermission(
                        this@CameraActivity,
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    cameraSource.start(svBarcode.holder)
                } else {
                    ActivityCompat.requestPermissions(
                        this@CameraActivity,
                        arrayOf(android.Manifest.permission.CAMERA),
                        myPermissionsRequestCamera
                    )
                }
            }

        })

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == myPermissionsRequestCamera) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                cameraSource.start(svBarcode.holder)
            else Toast.makeText(this, "scanner", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        detector.release()
        cameraSource.stop()
        cameraSource.release()
    }


    private fun addStringType() {

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

                this.intent = Intent(applicationContext, Detail::class.java)
                MainActivity.codes.add(codeAt)
            }
            Barcode.CALENDAR_EVENT -> {
                //Log.d("answer", "Status" + code.calendarEvent.status)
                //Log.d("answer", "CALENDAR_EVENT-" + code.calendarEvent.organizer)
            }
            Barcode.CONTACT_INFO -> {

            }
            Barcode.EMAIL -> {
                val mailto = "mailto:" + code.email?.address +
                        "&subject=" + Uri.encode(code.email?.subject) +
                        "&body=" + Uri.encode(code.email?.body)

                this.intent = Intent(Intent.ACTION_SENDTO)
                this.intent.data = Uri.parse(mailto)
            }
            Barcode.URL -> {
                this.intent = Intent(Intent.ACTION_VIEW)
                this.intent.data = Uri.parse(code.url?.url)
            }
            else -> {
                this.intent = Intent(applicationContext, Detail::class.java)
            }


        }

    }
}
