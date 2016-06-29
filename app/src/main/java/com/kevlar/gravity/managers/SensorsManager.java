package com.kevlar.gravity.managers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.kevlar.gravity.MainActivity;

public class SensorsManager{

    private Sensor sensor;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;


    public SensorsManager(MainActivity activity, Sensor sensor, SensorEventListener SEL){

        this.sensor = sensor;
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorEventListener = SEL;

        registerListener();
    }

    public boolean registerListener(){

        if(sensor == null)
            return false;
        else
            return sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);

    }

    public void unregisterListener(){

        sensorManager.unregisterListener(sensorEventListener);
    }
}
