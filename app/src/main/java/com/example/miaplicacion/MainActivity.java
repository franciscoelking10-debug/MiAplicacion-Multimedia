package com.example.miaplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad Principal - MiAplicacion
 * 
 * Curso: Programación Android (IEI-093)
 * Actividad 1.3: Uso de layouts, text y buttons en una app
 * 
 * Esta clase administra la lógica de interacción de los elementos visuales definidos
 * en activity_main.xml, demostrando el uso de ConstraintLayout, LinearLayout,
 * TableLayout, TextViews y Buttons.
 */
public class MainActivity extends AppCompatActivity {

    // =========================================================================
    // Vistas de la interfaz de usuario
    // =========================================================================
    
    // Encabezado
    private Button btnResetGlobal;

    // Sección 1: LinearLayout (Formulario)
    private EditText etNombre;
    private EditText etCarrera;
    private Button btnSaludar;
    private Button btnLimpiar;

    // Sección 2: TableLayout
    private TableLayout tableAsignaturas;
    private Button btnToggleTable;
    private boolean isTableVisible = true;

    // Sección 3: TextViews y Botones Dinámicos
    private TextView tvDynamicText;
    private TextView tvCounter;
    private Button btnIncrementar;
    private Button btnDecrementar;
    private Button btnCambiarTexto;
    private Button btnExpandirTexto;

    // Sección 4: Actividad 1.4 Navegación
    private Button btnNavAct14Widgets;
    private Button btnNavAct14Register;
    private Button btnNavAct14Courses;
    // Sección 5: Navegación Actividad 2.1 (Intents y Eventos)
    private Button btnNavAct21Inicio;
    private Button btnNavAct21Resultado;


    // Variables de estado
    private int counter = 0;
    private int messageIndex = 0;

    // Mensajes informativos sobre Android Layouts para rotar en tvDynamicText
    private final String[] informativeMessages = {
        "ConstraintLayout permite crear diseños complejos y planos sin anidar vistas innecesarias.",
        "LinearLayout organiza los elementos en una única dirección (Vertical u Horizontal).",
        "TableLayout distribuye sus elementos secundarios en filas (TableRow) y columnas tipo matriz.",
        "Los TextViews con Autosizing ajustan automáticamente el tamaño de fuente según el contenedor.",
        "Los Buttons en Android capturan eventos del usuario mediante la interfaz OnClickListener."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Vincula el diseño XML con la actividad Java
        setContentView(R.layout.activity_main);

        // Inicializar todas las referencias a los componentes visuales
        initializeViews();

        // Configurar los manejadores de eventos (listeners) para los botones
        setupListeners();
    }

    /**
     * Obtiene las referencias de las vistas declaradas en el layout XML mediante su ID.
     */
    private void initializeViews() {
        // Encabezado
        btnResetGlobal = findViewById(R.id.btnResetGlobal);

        // Sección LinearLayout
        etNombre = findViewById(R.id.etNombre);
        etCarrera = findViewById(R.id.etCarrera);
        btnSaludar = findViewById(R.id.btnSaludar);
        btnLimpiar = findViewById(R.id.btnLimpiar);

        // Sección TableLayout
        tableAsignaturas = findViewById(R.id.tableAsignaturas);
        btnToggleTable = findViewById(R.id.btnToggleTable);

        // Sección TextViews y Botones
        tvDynamicText = findViewById(R.id.tvDynamicText);
        tvCounter = findViewById(R.id.tvCounter);
        btnIncrementar = findViewById(R.id.btnIncrementar);
        btnDecrementar = findViewById(R.id.btnDecrementar);
        btnCambiarTexto = findViewById(R.id.btnCambiarTexto);
        btnExpandirTexto = findViewById(R.id.btnExpandirTexto);

        // Sección 4: Navegación Actividad 1.4
        btnNavAct14Widgets = findViewById(R.id.btnNavAct14Widgets);
        btnNavAct14Register = findViewById(R.id.btnNavAct14Register);
        btnNavAct14Courses = findViewById(R.id.btnNavAct14Courses);
        // Sección 5: Navegación Actividad 2.1
        btnNavAct21Inicio = findViewById(R.id.btnNavAct21Inicio);
        btnNavAct21Resultado = findViewById(R.id.btnNavAct21Resultado);

    }

    /**
     * Asocia los eventos OnClickListener a cada botón de la interfaz.
     */
    private void setupListeners() {
        // ---------------------------------------------------------------------
        // 1. Botón Saludar (LinearLayout)
        // ---------------------------------------------------------------------
        btnSaludar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaludar();
            }
        });

        // ---------------------------------------------------------------------
        // 2. Botón Limpiar Formulario (LinearLayout)
        // ---------------------------------------------------------------------
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNombre.setText("");
                etCarrera.setText("");
                etNombre.requestFocus();
                Toast.makeText(MainActivity.this, getString(R.string.toast_fields_cleared), Toast.LENGTH_SHORT).show();
            }
        });

        // ---------------------------------------------------------------------
        // 3. Botón Alternar Visibilidad de la Tabla (TableLayout)
        // ---------------------------------------------------------------------
        btnToggleTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleToggleTable();
            }
        });

        // ---------------------------------------------------------------------
        // 4. Botón Incrementar Contador
        // ---------------------------------------------------------------------
        btnIncrementar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                updateCounterDisplay();
            }
        });

        // ---------------------------------------------------------------------
        // 5. Botón Decrementar Contador
        // ---------------------------------------------------------------------
        btnDecrementar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    counter--;
                }
                updateCounterDisplay();
            }
        });

        // ---------------------------------------------------------------------
        // 6. Botón Cambiar Mensaje Dinámico
        // ---------------------------------------------------------------------
        btnCambiarTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageIndex = (messageIndex + 1) % informativeMessages.length;
                tvDynamicText.setText(informativeMessages[messageIndex]);
                counter++;
                updateCounterDisplay();
            }
        });

        // ---------------------------------------------------------------------
        // 7. Botón Expandir Texto (Demostración Autosizing)
        // ---------------------------------------------------------------------
        btnExpandirTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String largo = "Demostración de Auto-Ajuste de Texto (Autosizing TextView): " +
                        "A medida que este texto se hace más extenso y contiene múltiples líneas informativas sobre el desarrollo en Android, " +
                        "el TextView reduce dinámicamente su tamaño en sp para mantenerse visible dentro del contenedor sin desbordarse.";
                tvDynamicText.setText(largo);
                counter++;
                updateCounterDisplay();
            }
        });

        // ---------------------------------------------------------------------
        // 8. Botón Reiniciar Todo (ConstraintLayout Header)
        // ---------------------------------------------------------------------
        btnResetGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
            }
        });

        // ---------------------------------------------------------------------
        // 9. Navegación hacia Actividad 1.4 y Desafíos Prácticos
        // ---------------------------------------------------------------------
        btnNavAct14Widgets.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity2.class)));
        btnNavAct14Register.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
        btnNavAct14Courses.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CourseListActivity.class)));
        // ---------------------------------------------------------------------
        // 10. Navegación hacia Actividad 2.1 (Inicio y AResultado)
        // ---------------------------------------------------------------------
        if (btnNavAct21Inicio != null) {
            btnNavAct21Inicio.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Inicio.class)));
        }
        
        // ---------------------------------------------------------------------
        // 11. Navegación hacia Actividad 2.2 (Threads y Sensores)
        // ---------------------------------------------------------------------
        Button btnNavAct22 = findViewById(R.id.btnNavAct22ThreadsSensors);
        if (btnNavAct22 != null) {
            btnNavAct22.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ThreadsSensorsActivity.class)));
        }

        if (btnNavAct21Resultado != null) {
            btnNavAct21Resultado.setOnClickListener(v -> {
                Intent demoIntent = new Intent(MainActivity.this, AResultado.class);
                demoIntent.putExtra("STNombre", "Demostración Santo Tomás");
                startActivity(demoIntent);
            });
        }

        // ---------------------------------------------------------------------
        // 12. Navegación hacia Actividad 2.4 (Elementos Multimedia)
        // ---------------------------------------------------------------------
        Button btnNavAct24 = findViewById(R.id.btnNavAct24Multimedia);
        if (btnNavAct24 != null) {
            btnNavAct24.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MultimediaActivity.class)));
        }

    }

    /**
     * Valida los campos del formulario y muestra un saludo personalizado.
     */
    private void handleSaludar() {
        String nombre = etNombre.getText().toString().trim();
        String carrera = etCarrera.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("Por favor ingresa tu nombre");
            etNombre.requestFocus();
            Toast.makeText(this, getString(R.string.toast_empty_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(carrera)) {
            carrera = "Informática";
        }

        String mensajeBienvenida = getString(R.string.toast_welcome, nombre, carrera);
        Toast.makeText(this, mensajeBienvenida, Toast.LENGTH_LONG).show();

        // Actualizar el texto dinámico para reflejar el usuario activo
        tvDynamicText.setText("¡Hola " + nombre + "! Has interactuado exitosamente con los componentes de la aplicación.");
        counter++;
        updateCounterDisplay();
    }

    /**
     * Alterna la visibilidad del TableLayout para demostrar el control de visibilidad en tiempo de ejecución.
     */
    private void handleToggleTable() {
        if (isTableVisible) {
            tableAsignaturas.setVisibility(View.GONE);
            btnToggleTable.setText("Mostrar Tabla de Asignaturas");
            isTableVisible = false;
        } else {
            tableAsignaturas.setVisibility(View.VISIBLE);
            btnToggleTable.setText(getString(R.string.btn_toggle_table));
            isTableVisible = true;
            Toast.makeText(this, getString(R.string.toast_table_info), Toast.LENGTH_SHORT).show();
        }
        counter++;
        updateCounterDisplay();
    }

    /**
     * Actualiza el TextView que muestra el contador de interacciones.
     */
    private void updateCounterDisplay() {
        String textoContador = getString(R.string.label_counter_prefix) + counter;
        tvCounter.setText(textoContador);
    }

    /**
     * Restablece todos los campos, textos, visibilidades y contadores al estado inicial.
     */
    private void resetAll() {
        etNombre.setText("");
        etCarrera.setText("");
        etNombre.setError(null);
        counter = 0;
        messageIndex = 0;
        updateCounterDisplay();

        tableAsignaturas.setVisibility(View.VISIBLE);
        btnToggleTable.setText(getString(R.string.btn_toggle_table));
        isTableVisible = true;

        tvDynamicText.setText(getString(R.string.dynamic_text_default));

        Toast.makeText(this, getString(R.string.toast_reset), Toast.LENGTH_SHORT).show();
    }
}
