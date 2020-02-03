package com.example.qrreaderscan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
                if (ActivityCompat.checkSelfPermission(this@CameraActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA))
                        ;
                        requestPermissions(arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_CAMERA)
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

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource!!.stop()
            }
        })

        // preparo el detector de QR
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}


            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.getDetectedItems()

                if (barcodes.size() > 0) {

                    // obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString()
                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token

                        val code = barcodes.valueAt(0)
                        val type = barcodes.valueAt(0).valueFormat

                        //Log.i(LOG_TAG, type.toString() + " - " + Barcode.CONTACT_INFO)

                        when (type) {
                            Barcode.CONTACT_INFO -> {
                                //Log.i(LOG_TAG, code.contactInfo.name.first)
                                //Log.i(LOG_TAG, code.contactInfo.name.last)


                                for (index in 0 until code.contactInfo.phones.size) {
                                    when (code.contactInfo.phones[index].type) {
                                        Barcode.Phone.HOME -> Log.i(LOG_TAG, "HOME: " + code.contactInfo.phones[index].number)
                                        Barcode.Phone.MOBILE -> Log.i(LOG_TAG, "MOBILE: " + code.contactInfo.phones[index].number)
                                        Barcode.Phone.CONTENTS_FILE_DESCRIPTOR -> Log.i(LOG_TAG, "CONTENTS_FILE_DESCRIPTOR: " + code.contactInfo.phones[index].number)
                                        Barcode.Phone.PARCELABLE_WRITE_RETURN_VALUE -> Log.i(LOG_TAG, "PARCELABLE_WRITE_RETURN_VALUE: " + code.contactInfo.phones[index].number)
                                        Barcode.Phone.FAX -> Log.i(LOG_TAG, "FAX: " + code.contactInfo.phones[index].number)
                                        Barcode.Phone.WORK -> Log.i(LOG_TAG, "WORK: " + code.contactInfo.phones[index].number)
                                        Barcode.Phone.UNKNOWN -> Log.i(LOG_TAG, "UNKNOWN: " + code.contactInfo.phones[index].number)
                                    }

                                }



                                //Log.i(LOG_TAG, code.contactInfo.phones[0].number)
                            }
                            Barcode.EMAIL -> Log.i(LOG_TAG, code.email.address)
                            Barcode.ISBN -> Log.i(LOG_TAG, code.rawValue)
                            Barcode.PHONE -> Log.i(LOG_TAG, code.phone.number)
                            Barcode.PRODUCT -> Log.i(LOG_TAG, code.rawValue)
                            Barcode.SMS -> Log.i(LOG_TAG, code.sms.message)
                            Barcode.TEXT -> Log.i(LOG_TAG, code.rawValue)
                            Barcode.URL -> Log.i(LOG_TAG, "url: " + code.url.url)
                            Barcode.WIFI -> Log.i(LOG_TAG, code.wifi.ssid)
                            Barcode.GEO -> Log.i(LOG_TAG, code.geoPoint.lat.toString() + ":" + code.geoPoint.lng)
                            Barcode.CALENDAR_EVENT -> Log.i(LOG_TAG, code.calendarEvent.description)
                            Barcode.DRIVER_LICENSE -> Log.i(LOG_TAG, code.driverLicense.licenseNumber)
                            else -> Log.i(LOG_TAG, code.rawValue)
                        }


                    if (token != tokenanterior) {

                        // guardamos el ultimo token proceado
                        tokenanterior = token

                        val code = barcodes.valueAt(0)

                        textView2?.text = token



                        Thread(object : Runnable {
                            override fun run() {
                                try {
                                    synchronized(this) {
                                        // limpiamos el token
                                        tokenanterior = ""
                                    }
                                } catch (e: InterruptedException) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!")
                                    e.printStackTrace()
                                }

                            }
                        }).start()

                    }
                }
            }
        })

    }
}
