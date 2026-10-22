package com.example.miaplicacion;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Implementacin de un Started Service segn la Infografa de Concurrencia en Android:
 * "SERVICIOS (SERVICES): Componentes sin interfaz que se ejecutan indefinidamente.
 * STARTED SERVICE: Se ejecuta hasta que se detiene explcitamente (ej. descarga o sensores)."
 */
public class SensorBackgroundService extends Service implements SensorEventListener {

    private static final String TAG = "SensorBackgroundService";
    public static final String ACTION_SENSOR_BROADCAST = "com.example.miaplicacion.SENSOR_DATA_UPDATE";
    public static final String EXTRA_LOG_MESSAGE = "extra_log_message";
    public static final String EXTRA_SAMPLE_COUNT = "extra_sample_count";

    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private Sensor accelerometerSensor;
    private boolean isRunning = false;
    private int sampleCount = 0;
    private Thread workerThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SensorBackgroundService onCreate()");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SensorBackgroundService onStartCommand() - Started Service activado.");
        Toast.makeText(this, "Servicio en Segundo Plano Iniciado (Started Service)", Toast.LENGTH_SHORT).show();

        if (!isRunning) {
            isRunning = true;
            sampleCount = 0;

            // Registrar sensores para escucha en segundo plano
            if (sensorManager != null) {
                if (rotationSensor != null) {
                    sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
                if (accelerometerSensor != null) {
                    sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            }

            // Hilo secundario para emisin peridica de telemetra sin sobrecargar el UI Thread
            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning) {
                        try {
                            Thread.sleep(2000);
                            if (!isRunning) break;

                            sampleCount++;
                            String logMsg = "Muestra #" + sampleCount + " procesada en segundo plano por Started Service.";
                            Log.d(TAG, logMsg);

                            // Enviar emisin broadcast para que la Activity pueda reflejarlo si est visible
                            Intent broadcastIntent = new Intent(ACTION_SENSOR_BROADCAST);
                            broadcastIntent.putExtra(EXTRA_LOG_MESSAGE, logMsg);
                            broadcastIntent.putExtra(EXTRA_SAMPLE_COUNT, sampleCount);
                            sendBroadcast(broadcastIntent);

                        } catch (InterruptedException e) {
                            Log.d(TAG, "WorkerThread interrumpido.");
                            break;
                        }
                    }
                }
            });
            workerThread.start();
        }

        // Si el sistema mata el servicio por memoria, reiniciarlo cuando haya recursos disponibles
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SensorBackgroundService onDestroy() - Deteniendo servicio y liberando sensores.");
        isRunning = false;

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        if (workerThread != null) {
            workerThread.interrupt();
        }

        Toast.makeText(this, "Servicio en Segundo Plano Detenido", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Al ser un Started Service, no enlaza interfaz de comunicacin directa (retorna null)
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Recoleccin de telemetra continua fuera de la Activity
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Procesamiento de bajo consumo
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Gestin de calibracin del sensor
    }
}
