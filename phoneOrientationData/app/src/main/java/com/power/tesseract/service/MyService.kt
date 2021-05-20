package com.power.tesseract.service

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.power.tesseract.IMyAidlInterface
import java.lang.StringBuilder

class MyService : Service(), SensorEventListener {

    companion object {
        //        val mSensorData = MutableLiveData<FloatArray>()
//        val mSensorDataXAxis = MutableLiveData<FloatArray>()
//        val mSensorDataYAxis = MutableLiveData<FloatArray>()
//        val mSensorDataZAxis = MutableLiveData<FloatArray>()
//        val mSensorDataAccuracy = MutableLiveData<FloatArray>()
        var mSensorData: String? = null
        var mSensorDataXAxis: String? = null
        var mSensorDataYAxis: String? = null
        var mSensorDataZAxis: String? = null
        var mSensorDataAccuracy: String? = null
        const val NO_DATA_FOUNT = "No Data Found"
    }

    private val SENSOR_DELAY = 8 * 1000
    private var mSensorManager: SensorManager? = null
    private var mRotationSensor: Sensor? = null
    private val mServiceImpl: MyImpl by lazy { MyImpl() }

    private fun createSensorManager() {
        if (mSensorManager == null) {
            mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            mRotationSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            mRotationSensor?.let {
                addRotationSensorListener()
            }
        }
    }

    private fun addRotationSensorListener() {
        mSensorManager?.registerListener(
            this,
            mRotationSensor,
            SENSOR_DELAY
        )
    }

    override fun onBind(intent: Intent?): IBinder {
        return mServiceImpl
    }

    inner class MyImpl : IMyAidlInterface.Stub() {
        override fun requestValues(): String {
            createSensorManager()
            return mSensorData ?: NO_DATA_FOUNT
        }

        override fun requestXAxis(): String {
            return mSensorDataXAxis ?: NO_DATA_FOUNT
        }

        override fun requestYAxis(): String {
            return mSensorDataYAxis ?: NO_DATA_FOUNT
        }

        override fun requestZAxis(): String {
            return mSensorDataZAxis ?: NO_DATA_FOUNT
        }

        override fun requestAccuracy(): String {
            return mSensorDataAccuracy ?: NO_DATA_FOUNT
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                val customBuilder = StringBuilder()
                val x = it.values[0].toString()
                val y = it.values[1].toString()
                val z = it.values[2].toString()
                customBuilder.append(" X = $x")
                customBuilder.append(" Y = $y")
                customBuilder.append(" Z = $z")
                mSensorData = customBuilder.toString()

                mSensorDataXAxis = x
                mSensorDataYAxis = y
                mSensorDataZAxis = z
                mSensorDataAccuracy = it.accuracy.toString()
            }

        }
//        if (event != null) {
//            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
//                val values = event.values
//                Log.d("Value::X", "" + values[0])
//                Log.d("Value::Y", "" + values[1])
//                Log.d("Value::Z", "" + values[2])
//                // x = values[0]
//                // y = values[1]
//                // z = values[2]
////                event.accuracy
//                // Send data to service
//            }
//        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

}