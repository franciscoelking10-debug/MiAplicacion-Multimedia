package com.example.miaplicacion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Pantalla Receptora (AResultado)
 * Actividad 2.1: Programación de Actividades y Eventos
 *
 * Demuestra:
 * 1. Desempaquetado del dato transportado mediante Intent Explícito (getStringExtra("STNombre")).
 * 2. Visualización del resultado.
 * 3. Invocación de Intents Implícitos (Compartir y Navegador Web).
 * 4. Control del ciclo de vida y navegación hacia atrás (finish()).
 */
public class AResultado extends AppCompatActivity {

    private static final String TAG = "IEI093_AResultado";

    private TextView tvResultadoNombre;
    private TextView tvDetalleTecnico;
    private ImageView ivResultadoFoto;
    private Button btnCompartirResultado;
    private Button btnBuscarWebResultado;
    private Button btnVolverInicio;

    private String stNombreRecibido = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aresultado);
        Log.d(TAG, "onCreate() invocado en AResultado.");

        // Inicializar vistas
        tvResultadoNombre = findViewById(R.id.tvResultadoNombre);
        tvDetalleTecnico = findViewById(R.id.tvDetalleTecnico);
        ivResultadoFoto = findViewById(R.id.ivResultadoFoto);
        btnCompartirResultado = findViewById(R.id.btnCompartirResultado);
        btnBuscarWebResultado = findViewById(R.id.btnBuscarWebResultado);
        btnVolverInicio = findViewById(R.id.btnVolverInicio);

        // Desempaquetado del dato transferido vía Intent Explícito (Diapositiva 12)
        Intent intentRecibido = getIntent();
        if (intentRecibido != null && intentRecibido.hasExtra("STNombre")) {
            stNombreRecibido = intentRecibido.getStringExtra("STNombre");
        } else {
            stNombreRecibido = "Sin nombre especificado";
        }

        // Mostrar el nombre en el TextView correspondiente
        tvResultadoNombre.setText(stNombreRecibido);

        // Mostrar la foto capturada o la de muestra
        if (Inicio.capturedPhotoBitmap != null) {
            ivResultadoFoto.setImageBitmap(Inicio.capturedPhotoBitmap);
        } else {
            ivResultadoFoto.setImageResource(R.drawable.fotografo_ejemplo);
        }

        // Detalle didáctico para la revisión docente
        String detalle = "DETALLE DE LA COMUNICACIÓN:\n"
                + "• Intent Explícito: Inicio.class -> AResultado.class\n"
                + "• Clave Extra: 'STNombre'\n"
                + "• Valor Recibido: '" + stNombreRecibido + "'\n"
                + "• Código de extracción: getIntent().getStringExtra('STNombre');";
        tvDetalleTecnico.setText(detalle);

        // 1. Intent Implícito: Compartir el resultado (Diapositiva 10)
        btnCompartirResultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Foto: " + stNombreRecibido);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "¡Mira la foto '" + stNombreRecibido + "' capturada en MiAplicacion!");
                startActivity(Intent.createChooser(shareIntent, "Compartir en:"));
            }
        });

        // 2. Intent Implícito: Buscar información en Google (Diapositiva 9)
        btnBuscarWebResultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchUrl = "https://www.google.com/search?q=" + Uri.encode(stNombreRecibido + " fotografia");
                Intent searchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
                startActivity(searchIntent);
            }
        });

        // 3. Volver a Inicio
        btnVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finaliza esta Activity y retorna a Inicio en el stack
            }
        });

        Toast.makeText(this, "Datos recibidos correctamente de Inicio", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() invocado en AResultado.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() invocado en AResultado.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() invocado en AResultado.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() invocado en AResultado.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() invocado en AResultado.");
    }
}
