package com.example.practica06

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.util.Log
import com.example.practica06.databinding.ActivityMainBinding
import android.hardware.SensorEventListener as SensorEventListener

class MainActivity : AppCompatActivity(), SensorEventListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var themeImperial:MediaPlayer
    private lateinit var themeForce:MediaPlayer
    private var band1: Boolean = false
    private var band2: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        themeImperial = MediaPlayer.create(this, R.raw.imperial)
        themeForce = MediaPlayer.create(this, R.raw.force)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContentView(binding.root)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event!!.values[0]
        val y = event!!.values[1]
        val z = event!!.values[2]
        Log.d("VALORES","X: $x Y: $y Z: $z")
        if(x <= 6 && x >= -6){
            binding.layout.setBackgroundColor(Color.WHITE)
            try{
                themeImperial.stop()
                themeImperial.prepare()
                band1 = false
                themeForce.stop()
                themeForce.prepare()
                band2 = false

            }catch(e: Exception){
                Log.e("ERROR",e.toString())
            }
        }else if(x > 6){
            binding.layout.setBackgroundColor(Color.RED)
            if(band1){
                themeImperial.start()
            }
            band1 = true
        }else{
            binding.layout.setBackgroundColor(Color.BLUE)
            if(band2){
                themeForce.start()
            }
            band2 = true
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onResume(){
        super.onResume()
        accelerometer?.also { sen ->
            sensorManager.registerListener(this, sen, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}