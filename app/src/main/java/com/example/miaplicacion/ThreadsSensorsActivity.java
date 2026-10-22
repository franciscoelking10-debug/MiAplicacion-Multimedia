package com.example.miaplicacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Actividad 2.2: Threads y Sensores
 * Asignatura: Programacin Android (IEI-093) - Santo Toms
 * Profesor: Oscar Monrdez Saguas
 * Estudiante: Francisco Urquieta
 *
 * Implementa de manera integral:
 * 1. Descarga de imagen en segundo plano con Thread + mImageView.post() (Cdigo de Ejemplo 1 de la Pauta).
 * 2. Vector de Rotacin con SensorManager (Cdigo de Ejemplo 2 de la Pauta) y Acelermetro con Shake Detection.
 * 3. Tareas asincrnicas con AsyncTask implementando los 4 mtodos del ciclo de vida (onPreExecute, doInBackground, onProgressUpdate, onPostExecute).
 * 4. Gestin de sensores en segundo plano mediante HandlerThread (conforme a la Infografa).
 * 5. Started Service para procesamiento autnomo (SensorBackgroundService).
 */
public class ThreadsSensorsActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "ThreadsSensorsActivity";

    // Mdulo 1: Threads e Imagen
    private EditText etImageUrl;
    private Button btnDownloadThread;
    private Button btnSimulateANR;
    private ImageView mImageView;
    private ProgressBar pbImageLoading;
    private TextView tvThreadStatus;
    private TextView tvThreadTelemetry;

    // Mdulo 2: AsyncTask
    private Button btnStartAsyncTask;
    private Button btnCancelAsyncTask;
    private ProgressBar pbAsyncTask;
    private TextView tvAsyncTaskStatus;
    private TextView tvAsyncTaskDetails;
    private SensorDataProcessorTask asyncTaskInstance;

    // Mdulo 3: Sensores
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private Sensor accelerometerSensor;
    private TextView tvSensorRotationValues;
    private TextView tvSensorAngles;
    private TextView tvSensorAccelValues;
    private TextView tvShakeStatus;
    private ImageView ivCompassDial;
    private ImageView ivCompassNeedle;
    private View viewShakeIndicator;

    // Mdulo 4: Background Service
    private Button btnStartService;
    private Button btnStopService;
    private TextView tvServiceStatus;

    // Gestin en segundo plano para sensores (Infografa)
    private HandlerThread sensorWorkerThread;
    private Handler sensorWorkerHandler;

    // Control de sacudida (Shake detection)
    private long lastShakeTime = 0;
    private static final float SHAKE_THRESHOLD = 15.0f;

    // Receptor Broadcast para el Started Service
    private final BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SensorBackgroundService.ACTION_SENSOR_BROADCAST.equals(intent.getAction())) {
                String logMsg = intent.getStringExtra(SensorBackgroundService.EXTRA_LOG_MESSAGE);
                int count = intent.getIntExtra(SensorBackgroundService.EXTRA_SAMPLE_COUNT, 0);
                if (tvServiceStatus != null) {
                    tvServiceStatus.setText("Estado: Servicio Activo (" + count + " muestras registradas)\n" + logMsg);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads_sensors);

        Log.d(TAG, "onCreate() - Inicializando componentes de Actividad 2.2");

        initViews();
        initSensorManager();
        initSensorBackgroundThread();
        setupListeners();
    }

    /**
     * Enlaza todas las vistas del layout activity_threads_sensors.xml
     */
    private void initViews() {
        // Encabezado
        findViewById(R.id.btnVolverHub).setOnClickListener(v -> finish());

        // Mdulo 1: Descarga con Thread
        etImageUrl = findViewById(R.id.etImageUrl);
        btnDownloadThread = findViewById(R.id.btnDownloadThread);
        btnSimulateANR = findViewById(R.id.btnSimulateANR);
        mImageView = findViewById(R.id.mImageView);
        pbImageLoading = findViewById(R.id.pbImageLoading);
        tvThreadStatus = findViewById(R.id.tvThreadStatus);
        tvThreadTelemetry = findViewById(R.id.tvThreadTelemetry);

        // Mdulo 2: AsyncTask
        btnStartAsyncTask = findViewById(R.id.btnStartAsyncTask);
        btnCancelAsyncTask = findViewById(R.id.btnCancelAsyncTask);
        pbAsyncTask = findViewById(R.id.pbAsyncTask);
        tvAsyncTaskStatus = findViewById(R.id.tvAsyncTaskStatus);
        tvAsyncTaskDetails = findViewById(R.id.tvAsyncTaskDetails);

        // Mdulo 3: Sensores
        tvSensorRotationValues = findViewById(R.id.tvSensorRotationValues);
        tvSensorAngles = findViewById(R.id.tvSensorAngles);
        tvSensorAccelValues = findViewById(R.id.tvSensorAccelValues);
        tvShakeStatus = findViewById(R.id.tvShakeStatus);
        ivCompassDial = findViewById(R.id.ivCompassDial);
        ivCompassNeedle = findViewById(R.id.ivCompassNeedle);
        viewShakeIndicator = findViewById(R.id.viewShakeIndicator);

        // Mdulo 4: Background Service
        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);
        tvServiceStatus = findViewById(R.id.tvServiceStatus);
    }

    /**
     * Inicializa el SensorManager y adquiere los sensores requeridos por la pauta.
     * CDIGO DE EJEMPLO 2 (Pauta Oficial):
     * private SensorManager sensorManager;
     * private Sensor sensor;
     * sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
     * sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
     */
    private void initSensorManager() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            // Cdigo de Ejemplo 2 exacto de la pauta
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

            // Sensor complementario para deteccin de movimiento e inercia
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (rotationSensor == null) {
                Log.w(TAG, "Sensor TYPE_ROTATION_VECTOR no disponible fsicamente (ej: emulador bsico).");
                tvSensorRotationValues.setText("Sensor de Rotacin: No presente fsicamente en el emulador.");
                tvSensorAngles.setText("Modo simulado activado para pruebas en aula.");
            }
            if (accelerometerSensor == null) {
                Log.w(TAG, "Sensor TYPE_ACCELEROMETER no disponible fsicamente.");
                tvSensorAccelValues.setText("Acelermetro: No disponible en este dispositivo.");
            }
        } else {
            Toast.makeText(this, "SensorManager no est disponible en este dispositivo", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Inicializa un HandlerThread secundario para el procesamiento continuo de datos
     * de sensores segn la Infografa:
     * "GESTIN DE SENSORES EN SEGUNDO PLANO: Lectura constante requiere ejecucin fuera del Main Thread".
     */
    private void initSensorBackgroundThread() {
        sensorWorkerThread = new HandlerThread("SensorBackgroundWorker");
        sensorWorkerThread.start();
        sensorWorkerHandler = new Handler(sensorWorkerThread.getLooper());
    }

    /**
     * Configura todos los manejadores de eventos (OnClickListeners).
     */
    private void setupListeners() {

        // =====================================================================
        // CDIGO DE EJEMPLO 1 (Pauta Oficial):
        // Descarga de imagen en subproceso con Thread y sincronizacin con mImageView.post()
        // =====================================================================
        btnDownloadThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String targetUrl = etImageUrl.getText().toString().trim();
                final long startTime = System.currentTimeMillis();

                pbImageLoading.setVisibility(View.VISIBLE);
                tvThreadStatus.setText("Estado: Subproceso Thread en ejecucin...");
                tvThreadTelemetry.setText("Ejecutando en hilo secundario (Background)...");
                btnDownloadThread.setEnabled(false);

                // Cdigo estricto de la pauta:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String threadName = Thread.currentThread().getName();
                        Log.d(TAG, "Thread secundario iniciado: " + threadName);

                        // Operacin pesada / Descarga fuera del UI Thread
                        final Bitmap bitmap = NetworkImageHelper.loadImageFromNetwork(targetUrl);
                        final long duration = System.currentTimeMillis() - startTime;

                        // Sincronizacin con el hilo principal mediante mImageView.post()
                        mImageView.post(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImageBitmap(bitmap);
                                pbImageLoading.setVisibility(View.GONE);
                                btnDownloadThread.setEnabled(true);
                                tvThreadStatus.setText("? Imagen actualizada exitosamente en UI Thread!");
                                tvThreadTelemetry.setText("Subproceso: " + threadName + " | Duracin: " + duration + " ms | UI Thread: " + Thread.currentThread().getName());
                                Toast.makeText(ThreadsSensorsActivity.this, "Descarga completada en subproceso sin congelar la app", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });

        // =====================================================================
        // Demostracin Didctica de Riesgo ANR (Infografa)
        // =====================================================================
        btnSimulateANR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ThreadsSensorsActivity.this)
                        .setTitle("Advertencia Didctica sobre ANR")
                        .setMessage("Segn la Infografa de Concurrencia de la ctedra:\n\n" +
                                " 'EL HILO PRINCIPAL (UI THREAD): Controla la interfaz grfica y responde a eventos inmediatos.'\n" +
                                " 'TAREA PESADA (Bloqueo): Produce un ANR (Application Not Responding) cerrando la app si no responde en ~5 segundos.'\n\n" +
                                "Por esta razn, nunca se deben ejecutar descargas o algoritmos pesados directamente en el UI Thread, sino delegarlos a subprocesos secundarios (Threads o AsyncTask) como lo implementa el botn verde.")
                        .setPositiveButton("Entendido", null)
                        .show();
            }
        });

        // =====================================================================
        // Mdulo 2: Operaciones Asincrnicas con AsyncTask
        // =====================================================================
        btnStartAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (asyncTaskInstance == null || asyncTaskInstance.getStatus() == AsyncTask.Status.FINISHED) {
                    asyncTaskInstance = new SensorDataProcessorTask();
                    asyncTaskInstance.execute(50); // Procesar 50 muestras de telemetra
                } else {
                    Toast.makeText(ThreadsSensorsActivity.this, "La tarea ya est en ejecucin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (asyncTaskInstance != null && asyncTaskInstance.getStatus() == AsyncTask.Status.RUNNING) {
                    asyncTaskInstance.cancel(true);
                    Toast.makeText(ThreadsSensorsActivity.this, "Solicitud de cancelacin enviada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // =====================================================================
        // Mdulo 4: Started Service
        // =====================================================================
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(ThreadsSensorsActivity.this, SensorBackgroundService.class);
                startService(serviceIntent);
                tvServiceStatus.setText("Estado: Iniciando Started Service en segundo plano...");
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(ThreadsSensorsActivity.this, SensorBackgroundService.class);
                stopService(serviceIntent);
                tvServiceStatus.setText("Estado: Servicio detenido por el usuario.");
            }
        });
    }

    // =========================================================================
    // CICLO DE VIDA DE SENSORES (SensorEventListener)
    // =========================================================================

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() - Registrando listeners de sensores");

        // Registrar receptor del servicio
        IntentFilter filter = new IntentFilter(SensorBackgroundService.ACTION_SENSOR_BROADCAST);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(serviceReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(serviceReceiver, filter);
        }

        // Registrar sensores con SensorManager
        if (sensorManager != null) {
            if (rotationSensor != null) {
                sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI);
            }
            if (accelerometerSensor != null) {
                sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() - Desregistrando sensores para ahorrar batera");

        try {
            unregisterReceiver(serviceReceiver);
        } catch (Exception ignored) {}

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() - Liberando hilos de trabajo");

        if (sensorWorkerThread != null) {
            sensorWorkerThread.quitSafely();
        }
        if (asyncTaskInstance != null && !asyncTaskInstance.isCancelled()) {
            asyncTaskInstance.cancel(true);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null || event.values == null) return;

        // Delegar el procesamiento crudo de clculos pesados al HandlerThread secundario (Infografa)
        final SensorEvent eventCopy = event;
        if (sensorWorkerHandler != null) {
            sensorWorkerHandler.post(new Runnable() {
                @Override
                public void run() {
                    processSensorDataInBackground(eventCopy);
                }
            });
        }
    }

    /**
     * Procesa los datos de sensores fuera del Main Thread para asegurar fluidez de 60 FPS
     */
    private void processSensorDataInBackground(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // El vector de rotacin contiene [x*sin(?/2), y*sin(?/2), z*sin(?/2), cos(?/2)]
            float[] rotationMatrix = new float[9];
            float[] orientationAngles = new float[3];

            // Convertir vector a matriz de rotacin
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            // Obtener orientacin tridimensional (Azimut, Pitch, Roll)
            SensorManager.getOrientation(rotationMatrix, orientationAngles);

            final float azimuth = (float) Math.toDegrees(orientationAngles[0]);
            final float pitch = (float) Math.toDegrees(orientationAngles[1]);
            final float roll = (float) Math.toDegrees(orientationAngles[2]);

            final float vx = event.values[0];
            final float vy = event.values[1];
            final float vz = event.values[2];
            final float vw = event.values.length > 3 ? event.values[3] : 0.0f;

            // Retornar al UI Thread para actualizar vistas grficas
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvSensorRotationValues.setText(String.format(Locale.getDefault(),
                            "Vector (X, Y, Z, W):\n[%.3f, %.3f, %.3f, %.3f]", vx, vy, vz, vw));

                    tvSensorAngles.setText(String.format(Locale.getDefault(),
                            "Azimut (Brjula): %.1f\nInclinacin Pitch: %.1f | Roll: %.1f",
                            (azimuth + 360) % 360, pitch, roll));

                    // Rotar dinmicamente la aguja de la brjula
                    if (ivCompassNeedle != null) {
                        ivCompassNeedle.setRotation(-azimuth);
                    }
                }
            });

        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float ax = event.values[0];
            final float ay = event.values[1];
            final float az = event.values[2];

            // Clculo de magnitud de aceleracin
            float acceleration = (float) Math.sqrt(ax * ax + ay * ay + az * az);

            // Deteccin de sacudida (Shake Detection)
            boolean isShaken = false;
            long currentTime = System.currentTimeMillis();
            if (acceleration > SHAKE_THRESHOLD && (currentTime - lastShakeTime > 800)) {
                lastShakeTime = currentTime;
                isShaken = true;
            }

            final boolean finalIsShaken = isShaken;
            final float finalAcc = acceleration;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvSensorAccelValues.setText(String.format(Locale.getDefault(),
                            "Aceleracin (X, Y, Z):\n[%.2f, %.2f, %.2f] m/s (Total: %.2f G)",
                            ax, ay, az, finalAcc / 9.81f));

                    if (finalIsShaken) {
                        tvShakeStatus.setText("? SACUDIDA DETECTADA! (Sensor Acelermetro activado)");
                        tvShakeStatus.setTextColor(getResources().getColor(R.color.state_active));
                        viewShakeIndicator.setBackgroundColor(getResources().getColor(R.color.state_active));
                    }
                }
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged() para sensor: " + sensor.getName() + " -> Precisin: " + accuracy);
    }

    // =========================================================================
    // IMPLEMENTACIN DE ASYNCTASK (Infografa: Ciclo de 4 Etapas)
    // =========================================================================

    /**
     * Implementa AsyncTask con sus 4 mtodos fundamentales:
     * 1. onPreExecute() (UI Thread: Configuracin inicial)
     * 2. doInBackground() (Background Thread: Procesamiento pesado)
     * 3. onProgressUpdate() (UI Thread: Actualizar progreso)
     * 4. onPostExecute() (UI Thread: Manejar resultados finales)
     */
    private class SensorDataProcessorTask extends AsyncTask<Integer, Integer, String> {

        private long taskStartTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskStartTime = System.currentTimeMillis();

            // Etapa 1: UI Thread
            pbAsyncTask.setVisibility(View.VISIBLE);
            pbAsyncTask.setProgress(0);
            btnStartAsyncTask.setEnabled(false);
            btnCancelAsyncTask.setEnabled(true);

            tvAsyncTaskStatus.setText("Etapa 1: onPreExecute() [UI Thread] - Preparando tarea asincrnica...");
            tvAsyncTaskDetails.setText("Iniciando procesamiento de telemetra en subproceso de fondo.");
            Log.d(TAG, "AsyncTask: onPreExecute() en " + Thread.currentThread().getName());
        }

        @Override
        protected String doInBackground(Integer... params) {
            // Etapa 2: Background Thread
            int totalSteps = (params != null && params.length > 0) ? params[0] : 50;
            Log.d(TAG, "AsyncTask: doInBackground() iniciado en " + Thread.currentThread().getName() + " para " + totalSteps + " pasos.");

            for (int i = 1; i <= totalSteps; i++) {
                if (isCancelled()) {
                    Log.d(TAG, "AsyncTask: doInBackground() cancelado en el paso " + i);
                    return "Tarea cancelada por el usuario en el paso " + i;
                }

                try {
                    // Simular procesamiento pesado matemtico / filtrado de datos de sensores
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    return "Tarea interrumpida";
                }

                int progressPercent = (int) (((float) i / totalSteps) * 100);
                // Publicar progreso para transferir control a onProgressUpdate()
                publishProgress(progressPercent, i, totalSteps);
            }

            long totalTime = System.currentTimeMillis() - taskStartTime;
            return "Procesamiento completado con xito! (" + totalSteps + " muestras procesadas en " + totalTime + " ms)";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Etapa 3: UI Thread
            if (values != null && values.length >= 3) {
                int percent = values[0];
                int current = values[1];
                int total = values[2];

                pbAsyncTask.setProgress(percent);
                tvAsyncTaskStatus.setText("Etapa 3: onProgressUpdate() [UI Thread] - Progreso: " + percent + "%");
                tvAsyncTaskDetails.setText("Procesando muestra " + current + " de " + total + "...");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Etapa 4: UI Thread
            btnStartAsyncTask.setEnabled(true);
            btnCancelAsyncTask.setEnabled(false);

            tvAsyncTaskStatus.setText("Etapa 4: onPostExecute() [UI Thread] - Concluido");
            tvAsyncTaskDetails.setText(result);
            Toast.makeText(ThreadsSensorsActivity.this, "AsyncTask finalizado satisfactoriamente", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "AsyncTask: onPostExecute() en " + Thread.currentThread().getName());
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
            btnStartAsyncTask.setEnabled(true);
            btnCancelAsyncTask.setEnabled(false);
            pbAsyncTask.setProgress(0);

            tvAsyncTaskStatus.setText("AsyncTask Cancelado");
            tvAsyncTaskDetails.setText("La operacin fue abortada limpiamente.");
            Toast.makeText(ThreadsSensorsActivity.this, "AsyncTask cancelado", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "AsyncTask: onCancelled()");
        }
    }
}
