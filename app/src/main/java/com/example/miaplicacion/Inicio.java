package com.example.miaplicacion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Actividad 2.1: Programación de Actividades y Eventos
 * Asignatura: Programación Android (IEI-093)
 * Docente: Oscar Monárdez Saguas
 * Institución: Instituto Profesional Santo Tomás
 *
 * Esta clase implementa:
 * 1. Captura de eventos mediante View.OnClickListener.
 * 2. Invocación de la Cámara usando un Intent Implícito (MediaStore.ACTION_IMAGE_CAPTURE).
 * 3. Paso de parámetros ("STNombre") a otra Activity mediante un Intent Explícito.
 * 4. Trazabilidad completa del Ciclo de Vida (Lifecycle).
 */
public class Inicio extends AppCompatActivity {

    private static final String TAG = "IEI093_Inicio";
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    // Vistas principales solicitadas en la pauta
    private ImageView imageView;
    private EditText ETNombreFoto;
    private Button btnFoto;
    private Button BTAceptar;

    // Vistas adicionales pedagógicas
    private Button btnCompartirInicio;
    private Button btnWebInicio;
    private Button btnVolverMenu;
    private TextView tvLifecycleStatus;

    // Bitmap estático en memoria para compartir la captura con AResultado
    public static Bitmap capturedPhotoBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Log.d(TAG, "onCreate() invocado: La Activity ha sido creada en memoria.");

        // Inicialización de componentes según los IDs exactos de la pauta
        imageView = (ImageView) findViewById(R.id.imageView);
        ETNombreFoto = (EditText) findViewById(R.id.ETNombreFoto);
        btnFoto = (Button) findViewById(R.id.btnFoto);
        tvLifecycleStatus = findViewById(R.id.tvLifecycleStatus);
        btnCompartirInicio = findViewById(R.id.btnCompartirInicio);
        btnWebInicio = findViewById(R.id.btnWebInicio);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);

        // ---------------------------------------------------------------------
        // 1. Botón "HACER FOTO" (Intent Implícito de Cámara)
        // ---------------------------------------------------------------------
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerFoto();
            }
        });

        // ---------------------------------------------------------------------
        // 2. Botón "ACEPTAR" (Código oficial solicitado textualmente en la pauta)
        // ---------------------------------------------------------------------
        final Button BTAceptar = (Button) findViewById(R.id.BTAceptar);
        BTAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ETNombre = (EditText) findViewById(R.id.ETNombreFoto);
                String StNombre = ETNombre.getText().toString();

                // Validación para evitar campos vacíos
                if (TextUtils.isEmpty(StNombre.trim())) {
                    ETNombre.setError("Por favor, ingresa el nombre de la foto");
                    ETNombre.requestFocus();
                    Toast.makeText(Inicio.this, "Debe ingresar el nombre de la foto", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Código solicitado en la pauta:
                // Intent sIntent = new Intent(Inicio.this, AResultado.class);
                // sIntent.putExtra("STNombre", StNombre);
                // startActivity(sIntent);
                Intent sIntent = new Intent(Inicio.this, AResultado.class);
                sIntent.putExtra("STNombre", StNombre);
                startActivity(sIntent);
            }
        });

        // ---------------------------------------------------------------------
        // 3. Intents Implícitos Adicionales (Diapositivas 8, 9 y 10)
        // ---------------------------------------------------------------------
        if (btnCompartirInicio != null) {
            btnCompartirInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    compartirTexto();
                }
            });
        }

        if (btnWebInicio != null) {
            btnWebInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirEnlaceWeb();
                }
            });
        }

        if (btnVolverMenu != null) {
            btnVolverMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        updateLifecycleDisplay("onCreate()");
    }

    /**
     * Intent Implícito: Invoca la aplicación de cámara nativa del sistema.
     */
    private void hacerFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Log.e(TAG, "Cámara nativa no disponible en este entorno: " + e.getMessage());
            Toast.makeText(this, "Simulando captura de foto de muestra", Toast.LENGTH_SHORT).show();
            imageView.setImageResource(R.drawable.fotografo_ejemplo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null && extras.get("data") != null) {
                capturedPhotoBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(capturedPhotoBitmap);
                Toast.makeText(this, "¡Foto capturada exitosamente!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Intent Implícito: Delegando compartir al sistema operativo con createChooser.
     * Basado exactamente en la Diapositiva 10.
     */
    private void compartirTexto() {
        String texto = ETNombreFoto.getText().toString().trim();
        if (TextUtils.isEmpty(texto)) {
            texto = "Fotografía Atardecer Santo Tomás";
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "¡Hola mundo! Compartiendo: " + texto);
        startActivity(Intent.createChooser(i, "Compartir en:"));
    }

    /**
     * Intent Implícito: Abrir enlace en el navegador web (Diapositiva 9).
     */
    private void abrirEnlaceWeb() {
        String url = "https://developer.android.com/guide/components/activities/intro-activities?hl=es";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    // =========================================================================
    // Métodos del Ciclo de Vida (Lifecycle) - Diapositivas 6 y 7
    // =========================================================================

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() invocado: La Activity ahora es visible para el usuario.");
        updateLifecycleDisplay("onStart() - Visible");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() invocado: La Activity está en primer plano y lista para interactuar.");
        updateLifecycleDisplay("onResume() - En primer plano (Interactiva)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() invocado: La Activity pierde el foco.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() invocado: La Activity ya no es visible.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() invocado: Reiniciando Activity tras estar en segundo plano.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() invocado: Liberando recursos y destruyendo Activity.");
    }

    private void updateLifecycleDisplay(String estado) {
        if (tvLifecycleStatus != null) {
            tvLifecycleStatus.setText("Ciclo de vida actual: " + estado);
        }
    }
}
